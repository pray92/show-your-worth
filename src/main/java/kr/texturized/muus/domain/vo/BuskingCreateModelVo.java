package kr.texturized.muus.domain.vo;

import kr.texturized.muus.domain.entity.Busking;

import java.util.ArrayList;
import java.util.List;

/**
 * 버스킹을 생성하는데 필요한 엔티티 집합을 추상화한 인터페이스 Model DTO.
 */
public record BuskingCreateModelVo(
    Busking busking,
    List<String> keywords,
    List<String> imagePaths
) {

    public static BuskingCreateModelVo of(
        final Busking busking,
        final List<String> keywords,
        final List<String> imagePaths
    ) {
        return new BuskingCreateModelVo(busking, new ArrayList<>(keywords), new ArrayList<>(imagePaths));
    }
}
