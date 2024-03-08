package kr.texturized.muus.domain.vo;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

    public static BuskingUpdateVo of(
            final Long buskingId,
            final Double latitude,
            final Double longitude,
            final String title,
            final String description,
            final LocalDateTime managedStartTime,
            final LocalDateTime managedEndTime,
            final String[] keywords
    ) {
        return new BuskingUpdateVo(
                buskingId,
                latitude,
                longitude,
                title,
                description,
                managedStartTime,
                managedEndTime,
                Arrays.asList(keywords)
        );
    }
}
