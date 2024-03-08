package kr.texturized.muus.domain.vo;

import kr.texturized.muus.presentation.api.request.BuskingUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 버스킹 업데이트 Vo
 */
public record BuskingUpdateVo(
        Long buskingId,
        Double latitude,
        Double longitude,
        String title,
        String description,
        LocalDateTime managedStartTime,
        LocalDateTime managedEndTime,
        List<String> keywords
) {

    public static BuskingUpdateVo of(final Long buskingId, final BuskingUpdateRequest request) {
        return new BuskingUpdateVo(
                buskingId,
                request.latitude(),
                request.longitude(),
                request.title(),
                request.description(),
                request.managedStartTime(),
                request.managedEndTime(),
                request.keywords()
        );
    }
}
