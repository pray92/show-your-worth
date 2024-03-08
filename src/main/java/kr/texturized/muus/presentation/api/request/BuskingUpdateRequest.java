package kr.texturized.muus.presentation.api.request;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 버스킹 업데이트 Request
 */
public record BuskingUpdateRequest(
        Double latitude,
        Double longitude,
        String title,
        String description,
        LocalDateTime managedStartTime,
        LocalDateTime managedEndTime,
        List<String> keywords
) {
}
