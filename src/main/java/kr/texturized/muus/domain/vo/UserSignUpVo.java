package kr.texturized.muus.domain.vo;

import kr.texturized.muus.presentation.api.request.UserSignUpRequest;

/**
 * VO for Sign-up Service.
 *
 * @param accountId Account ID
 * @param password Password
 * @param nickname Nickname
 */
public record UserSignUpVo(
    String accountId,
    String password,
    String nickname
) {

    public static UserSignUpVo of(UserSignUpRequest request) {
        return new UserSignUpVo(request.accountId(), request.password(), request.nickname());
    }

}
