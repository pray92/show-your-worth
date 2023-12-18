package kr.texturized.muus.presentation.api.request;

import java.time.LocalDateTime;
import java.util.List;

import kr.texturized.muus.domain.vo.BuskingCreateVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request for busking create, update.
 */
public record BuskingCreateRequest(
    String title,
    Double latitude,
    Double longitude,
    List<String> keywords,
    String description,
    LocalDateTime managedStartTime,
    LocalDateTime managedEndTime
){
}
