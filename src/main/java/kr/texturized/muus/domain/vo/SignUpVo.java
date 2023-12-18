package kr.texturized.muus.domain.vo;

import kr.texturized.muus.presentation.api.request.SignUpRequest;

/**
 * VO for Sign-up Service.
 *
 * @param accountId Account ID
 * @param password Password
 * @param nickname Nickname
 */
public record SignUpVo(
    String accountId,
    String password,
    String nickname
) {

    public static SignUpVo of(SignUpRequest request) {
        return new SignUpVo(request.accountId(), request.password(), request.nickname());
    }

}
