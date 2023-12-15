package kr.texturized.muus.domain.vo;

import java.time.LocalDateTime;
import java.util.List;

import kr.texturized.muus.presentation.api.request.BuskingCreateRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * Vo for dto of request to service of busking creation.
 *
 * @param title title
 * @param imageFiles raw image
 * @param latitude latitude
 * @param longitude longitude
 * @param keywords keywords
 * @param description description
 * @param managedStartTime managedStartTime
 * @param managedEndTime managedEndTime
 */
public record BuskingCreateVo(
    Long userId,
    String title,
    List<MultipartFile> imageFiles,
    Double latitude,
    Double longitude,
    List<String> keywords,
    String description,
    LocalDateTime managedStartTime,
    LocalDateTime managedEndTime
) {

    public static BuskingCreateVo of(
        final Long userId,
        final BuskingCreateRequest request,
        final MultipartFile[] imageFiles
    ) {
        return new BuskingCreateVo(
                userId,
                request.title(),
                List.of(imageFiles),
                request.latitude(),
                request.longitude(),
                request.keywords(),
                request.description(),
                request.managedStartTime(),
                request.managedEndTime()
        );
    }
}
