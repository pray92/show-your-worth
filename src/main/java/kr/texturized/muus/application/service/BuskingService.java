package kr.texturized.muus.application.service;

import java.util.List;

import kr.texturized.muus.common.coordinate.CoordinateCalculator;
import kr.texturized.muus.common.storage.PostImageStorage;
import kr.texturized.muus.dao.BuskingDao;
import kr.texturized.muus.domain.vo.*;
import kr.texturized.muus.infrastructure.mapper.BuskingMapper;
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

    private final BuskingDao buskingDao;
    private final PostImageStorage postImageStorage;

    private final CoordinateCalculator coordinateCalculator;
    private final BuskingMapper buskingMapper;


    /**
     * Create the busking.
     *
     * @param userId Host ID of busking in DB
     * @param vo VO for busking creation.
     * @return Created busking ID
     */
    @Transactional
    public Long create(final Long userId, final CreateBuskingVo vo) {
        final List<String> uploadedPaths = uploadImagesThenGetUploadedPaths(userId, vo.imageFiles());
        final BuskingVo createVo = dto(uploadedPaths, vo);
        return buskingDao.create(userId, createVo);
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

    private BuskingVo dto(final List<String> imagePaths, final CreateBuskingVo vo) {
        return new BuskingVo(
                vo.title(),
                imagePaths,
                vo.latitude(),
                vo.longitude(),
                vo.keywords(),
                vo.description(),
                vo.managedStartTime(),
                vo.managedEndTime()
        );
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
