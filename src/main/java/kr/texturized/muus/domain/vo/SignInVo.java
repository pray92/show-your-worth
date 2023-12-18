package kr.texturized.muus.domain.vo;

import kr.texturized.muus.presentation.api.request.SignInRequest;

/**
 * VO for Sign-in Service.
 *
 * @param accountId Account ID
 * @param password Password
 */
public record SignInVo(
    String accountId,
    String password
) {

    public static SignInVo of(SignInRequest request) {
        return new SignInVo(request.accountId(), request.password());
    }
}
