package kr.texturized.muus.application.service;

import java.util.ArrayList;
import java.util.List;

import kr.texturized.muus.common.coordinate.CoordinateCalculator;
import kr.texturized.muus.common.storage.PostImageStorage;
import kr.texturized.muus.dao.BuskingDao;
import kr.texturized.muus.domain.entity.*;
import kr.texturized.muus.domain.entity.fk.ImageFk;
import kr.texturized.muus.domain.entity.fk.KeywordFk;
import kr.texturized.muus.domain.exception.UserNotFoundException;
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

    @Transactional
    public Long create(final BuskingCreateVo vo) {

        final User user = userMapper.findById(vo.userId()).orElseThrow(() -> new UserNotFoundException(vo.userId()));

        final Busking busking = Busking.builder()
                    .host(user)
                    .title(vo.title())
                    .description(vo.description())
                    .latitude(vo.latitude())
                    .longitude(vo.longitude())
                    .managedStartTime(vo.managedStartTime())
                    .managedEndTime(vo.managedEndTime())
                .build();

        final List<Keyword> keywords = vo.keywords().stream()
                .map(keyword -> Keyword.builder()
                            .id(new KeywordFk(busking.getId(), PostCategoryEnum.BUSKING))
                            .keyword(keyword)
                        .build())
                .toList();

        final List<String> uploadedPaths = uploadImagesThenGetUploadedPaths(vo.userId(), vo.imageFiles());
        final List<Image> images = new ArrayList<>(uploadedPaths.size());
        for (int order = 0; order < uploadedPaths.size(); ++order) {
             images.add(Image.builder()
                        .id(ImageFk.builder()
                             .postId(busking.getId())
                             .postType(PostCategoryEnum.BUSKING)
                             .uploadOrder(order)
                             .build())
                        .path(uploadedPaths.get(order))
                     .build());
        }

        final BuskingCreateModelVo modelVo = BuskingCreateModelVo.of(busking, keywords, images);

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

}
