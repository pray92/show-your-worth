package kr.texturized.muus.domain.vo;

/**
 * 유저 프로필 조회 결과 Vo.
 *
 * 해당 유저가 활성 중인 버스킹이 있다면 해당 버스킹의 ID도 반환해요.
 */
public record UserProfileResultVo(String nickname, String profileImagePath, Long buskingId) {
}
