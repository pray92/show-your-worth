package kr.texturized.muus.application.service;

import java.util.List;
import kr.texturized.muus.common.storage.PostImageStorage;
import kr.texturized.muus.dao.BuskingDao;
import kr.texturized.muus.dao.BuskingFindDao;
import kr.texturized.muus.domain.vo.BuskingMapVo;
import kr.texturized.muus.domain.vo.BuskingVo;
import kr.texturized.muus.domain.vo.CreateBuskingVo;
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
    private final BuskingFindDao buskingFindDao;


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

    /**
     * Get 'active' buskings list for map.
     *
     * @param latitude latitude
     * @param longitude longitude
     * @param widthMeter range of width(meter)
     * @param heightMeter range of height(meter)
     * @return list of active busking information for map
     */
    public List<BuskingMapVo> getActiveBuskingsInMap(
            final double latitude,
            final double longitude,
            final double widthMeter,
            final double heightMeter
    ) {
        return buskingFindDao.getActiveBuskingsInMap(latitude, longitude, widthMeter, heightMeter);
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

}
