package kr.texturized.muus.presentation.api.request;

/**
 * Request for sign-in.
 */
public record UserSignInRequest(
    String accountId,
    String password
) {
}
