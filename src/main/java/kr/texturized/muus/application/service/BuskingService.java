package kr.texturized.muus.application.service;

import java.util.List;

import kr.texturized.muus.application.service.exception.InvalidAccountException;
import kr.texturized.muus.application.service.exception.MismatchedPostAndUserException;
import kr.texturized.muus.common.coordinate.CoordinateCalculator;
import kr.texturized.muus.common.storage.PostImageStorage;
import kr.texturized.muus.dao.BuskingDao;
import kr.texturized.muus.domain.entity.*;
import kr.texturized.muus.application.service.exception.BuskingProfileNotFoundException;
import kr.texturized.muus.domain.vo.*;
import kr.texturized.muus.infrastructure.mapper.BuskingMapper;
import kr.texturized.muus.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static java.util.stream.Collectors.*;

/**
 * Service for Busking Create, Update and Delete.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BuskingService {

    private final UserMapper userMapper;

    private final BuskingDao buskingDao;
    private final BuskingMapper buskingMapper;

    private final CoordinateCalculator coordinateCalculator;

    private final PostImageStorage postImageStorage;

    /**
     * 버스킹을 생성해요.
     *
     * NOTE: 코드 정책으로 서비스 단계에서 엔티티를 생성하는 것으로 정했으나,
     * Busking 엔티티의 ID가 있어야 초기화 가능한 키워드 및 이미지 엔티티의 한계로 인해
     * Busking 엔티티만 서비스에서 생성하고, 해당 서브 엔티티들은 DAO에서 처리하게 하고 Raw Data를 DTO에 담아 보내요.
     *
     * @param vo 버스킹 생성에 필요한 데이터 집합 Vo
     * @return 생성된 버스킹 ID
     */
    @Transactional
    public Long create(final BuskingCreateVo vo) {
        final User user = userMapper.findByAccountId(vo.accountId()).orElseThrow(InvalidAccountException::new);

        final Busking busking = Busking.builder()
                    .host(user)
                    .title(vo.title())
                    .description(vo.description())
                    .latitude(vo.latitude())
                    .longitude(vo.longitude())
                    .managedStartTime(vo.managedStartTime())
                    .managedEndTime(vo.managedEndTime())
                .build();
        final List<String> uploadedPaths = uploadImagesThenGetUploadedPaths(user.getId(), vo.imageFiles());

        final BuskingCreateModelVo modelVo = BuskingCreateModelVo.of(busking, vo.keywords(), uploadedPaths);

        return buskingDao.create(modelVo);
    }


    /**
     * Upload and return successfully uploaded image's path.
     *
     * @param userId User ID for creating busking
     * @param multipartFiles Image files for busking
     * @return relative paths of uploaded images
     */
    private List<String> uploadImagesThenGetUploadedPaths(final Long userId, final List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .filter(partFile -> null != partFile.getContentType() && partFile.getContentType().startsWith("image/"))
                .map(partFile -> {
                    final String uploadedPath = postImageStorage.upload(userId, partFile);
                    log.info("Image is uploaded on: {}", uploadedPath);
                    return uploadedPath;
                })
                .filter(path -> !path.isEmpty())
                .collect(toList());
    }

    /**
     * 특정 범위의 활동 예정 및 진행 중인 버스킹 조회
     *
     * @param vo 특정 범위에 대한 VO
     * @return 활동 예정 및 진행 중인 버스킹 목록
     */
    public List<BuskingSearchResultVo> search(final BuskingSearchVo vo) {
        final double latitude = vo.latitude();
        final double longitude = vo.longitude();
        final double latitudeRange = coordinateCalculator.meterToLatitude(vo.widthMeter());
        final double longitudeRange = coordinateCalculator.meterToLatitude(vo.heightMeter());

        return buskingMapper.search(latitude, longitude, latitudeRange, longitudeRange);
    }

    /**
     * 버스킹 프로필 조회
     *
     * @param buskingId 조회할 버스킹 ID
     * @return 버스킹 프로필 조회 결과 VO
     */
    public BuskingProfileResultVo profile(final Long buskingId) {
        return buskingMapper.profile(buskingId).orElseThrow(BuskingProfileNotFoundException::new);
    }

    /**
     * 버스킹 정보 업데이트
     *
     * @param vo 버스킹 업데이트에 필요한 정보 Vo
     * @return 변경된 버스킹 ID
     */
    @Transactional
    public Long update(final BuskingUpdateVo vo) {
        final BuskingUpdateModelVo modelVo = BuskingUpdateModelVo.of(
                vo.buskingId(),
                vo.latitude(),
                vo.longitude(),
                vo.title(),
                vo.description(),
                vo.managedStartTime(),
                vo.managedEndTime(),
                vo.keywords());

        return buskingDao.updateBusking(modelVo);
    }

    /**
     * 해당 유저가 버스킹을 만들었는지 확인해요.
     *
     * @param buskingId 버스킹 ID
     * @param accountId 유저 계정
     */
    public void validateBuskingMadeByUser(final Long buskingId, final String accountId) {
        if (!buskingMapper.isBuskingMadeByUser(buskingId, accountId)) {
           throw new MismatchedPostAndUserException();
        }
    }
}
