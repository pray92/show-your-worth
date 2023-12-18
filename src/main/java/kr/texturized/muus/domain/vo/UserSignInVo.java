package kr.texturized.muus.domain.vo;

import kr.texturized.muus.presentation.api.request.UserSignInRequest;

/**
 * VO for Sign-in Service.
 *
 * @param accountId Account ID
 * @param password Password
 */
public record UserSignInVo(
    String accountId,
    String password
) {

    public static UserSignInVo of(UserSignInRequest request) {
        return new UserSignInVo(request.accountId(), request.password());
    }
}
