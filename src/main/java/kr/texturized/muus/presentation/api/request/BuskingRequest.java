package kr.texturized.muus.presentation.api.request;

import java.time.LocalDateTime;
import java.util.List;

import kr.texturized.muus.domain.vo.CreateBuskingVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request for busking create, update.
 */
public record BuskingRequest (
    String title,
    Double latitude,
    Double longitude,
    List<String> keywords,
    String description,
    LocalDateTime managedStartTime,
    LocalDateTime managedEndTime
){
    public CreateBuskingVo toDto(final MultipartFile[] imageFiles) {
        return new CreateBuskingVo(
                this.title(),
                List.of(imageFiles),
                this.latitude(),
                this.longitude(),
                this.keywords(),
                this.description(),
                this.managedStartTime(),
                this.managedEndTime()
        );
    }
}
