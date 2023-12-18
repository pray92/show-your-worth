package kr.texturized.muus.presentation.api.response;

import kr.texturized.muus.domain.vo.BuskingSearchResultVo;

import java.util.List;

/**
 * 버스킹 조회 요청에 대한 응답.
 *
 * 조회 시 해당 위치에 표시하고, 이후 선택 시 해당 ID를 기반으로 다시 조회하기 위해 간소화된 데이터만 반환합니다.
 */
public record BuskingSearchResponse(List<BuskingSearchResultVo> results) {

    public static BuskingSearchResponse of(final List<BuskingSearchResultVo> results) {
        return new BuskingSearchResponse(results);
    }
}
