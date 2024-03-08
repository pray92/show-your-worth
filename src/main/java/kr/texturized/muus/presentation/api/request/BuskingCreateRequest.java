package kr.texturized.muus.presentation.api.request;

import java.time.LocalDateTime;
import java.util.List;

import kr.texturized.muus.domain.vo.BuskingCreateVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request for busking create
 */
public record BuskingCreateRequest(
    Double latitude,
    Double longitude,
    String title,
    String description,
    LocalDateTime managedStartTime,
    LocalDateTime managedEndTime,
    List<String> keywords
){
}
