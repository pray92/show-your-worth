package kr.texturized.muus.domain.vo;

import kr.texturized.muus.domain.entity.Busking;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 버스킹 정보 업데이트 Vo
 */
public record BuskingUpdateModelVo (
        Long buskingId,
        Double latitude,
        Double longitude,
        String title,
        String description,
        LocalDateTime managedStartTime,
        LocalDateTime managedEndTime,
        List<String> keywords
) {

    public static BuskingUpdateModelVo of(
            final Long buskingId,
            final Double latitude,
            final Double longitude,
            final String title,
            final String description,
            final LocalDateTime managedStartTime,
            final LocalDateTime managedEndTime,
            final List<String> keywords
    ) {
        return new BuskingUpdateModelVo(
                buskingId,
                latitude,
                longitude,
                title,
                description,
                managedStartTime,
                managedEndTime,
                List.copyOf(keywords));
    }
}
