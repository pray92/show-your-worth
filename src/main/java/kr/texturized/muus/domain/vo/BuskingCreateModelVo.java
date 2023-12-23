package kr.texturized.muus.domain.vo;

import kr.texturized.muus.domain.entity.Busking;
import kr.texturized.muus.domain.entity.Image;
import kr.texturized.muus.domain.entity.Keyword;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
