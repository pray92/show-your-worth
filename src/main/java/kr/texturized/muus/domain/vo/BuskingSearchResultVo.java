package kr.texturized.muus.domain.vo;

/**
 * 현재 활동 및 예정인 버스킹 VO
 *
 * @param id 버스킹 ID
 * @param latitude 위도
 * @param longitude 경도
 */
public record BuskingSearchResultVo(Long id, Double latitude, Double longitude) {

}
