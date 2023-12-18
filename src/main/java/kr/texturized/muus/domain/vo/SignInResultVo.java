package kr.texturized.muus.domain.vo;

import kr.texturized.muus.domain.entity.User;
import kr.texturized.muus.domain.entity.UserTypeEnum;

/**
 * 로그인 결과 VO
 *
 * @param accountId 계정 ID
 * @param userType 유저 타입
 */
public record SignInResultVo(
    String accountId,
    UserTypeEnum userType
) {

    public static SignInResultVo of(final User user) {
        return new SignInResultVo(user.getAccountId(), user.getUserType());
    }
}
