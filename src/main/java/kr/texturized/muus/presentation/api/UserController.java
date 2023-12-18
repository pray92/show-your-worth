package kr.texturized.muus.presentation.api;

import javax.validation.Valid;

import kr.texturized.muus.application.service.UserSignFacade;
import kr.texturized.muus.common.error.exception.BusinessException;
import kr.texturized.muus.common.error.exception.ErrorCode;
import kr.texturized.muus.common.util.SignInCheck;
import kr.texturized.muus.common.util.ValidationConstants;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.domain.vo.SignInResultVo;
import kr.texturized.muus.domain.vo.SignInVo;
import kr.texturized.muus.domain.vo.SignUpVo;
import kr.texturized.muus.presentation.api.request.SignInRequest;
import kr.texturized.muus.presentation.api.request.SignUpRequest;
import kr.texturized.muus.presentation.api.response.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for User.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserSignFacade userSignFacade;

    /**
     * API for validation of account id.
     *
     * @param accountId account id to use
     * @return Available response.
     */
    @GetMapping("/validate/account")
    public ResponseEntity<String> validateAccount(@RequestParam final String accountId) {
        validatePattern(
            accountId,
            ValidationConstants.ACCOUNT_PATTERN,
            ValidationConstants.ACCOUNT_PATTERN_INVALID_MESSAGE
        );
        userSignFacade.checkDuplicatedAccountId(accountId);

        return ResponseEntity.status(HttpStatus.OK).body("사용 가능해요.");
    }

    /**
     * API for validation of password.
     *
     * @param password to use
     * @return Available response.
     */
    @GetMapping("/validate/password")
    public ResponseEntity<String> validatePassword(@RequestParam final String password) {
        validatePattern(
            password,
            ValidationConstants.PASSWORD_PATTERN,
            ValidationConstants.PASSWORD_PATTERN_INVALID_MESSAGE
        );

        return ResponseEntity.status(HttpStatus.OK).body("사용 가능해요.");
    }

    /**
     * API for validation of nickname.
     *
     * @param nickname to use, it validates using bean validation.
     * @return Available response.
     */
    @GetMapping("/validate/nickname")
    public ResponseEntity<String> validateNickname(@RequestParam final String nickname) {
        validatePattern(
            nickname,
            ValidationConstants.NICKNAME_PATTERN,
            ValidationConstants.NICKNAME_PATTERN_INVALID_MESSAGE
        );
        userSignFacade.checkDuplicatedNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK).body("사용 가능해요.");
    }

    /**
     * Sign-up.
     *
     * @param request request including information for sign-up
     * @return DB identical id of signed-up account
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Long> signUp(@RequestBody @Valid final SignUpRequest request) {
        final SignUpVo signUpVo = SignUpVo.of(request);
        final Long userId = userSignFacade.signUp(signUpVo);

        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }

    /**
     * Sign-in.<br>
     * <br>
     * NOTE: No validation for login.
     * Some account id and password may not sign-in because of the change of account and password policy.
     * Let it log-in just checking information in DB.
     *
     * @param request request including information for sign-in
     * @return result(Could be token) and user type
     */
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody final SignInRequest request) {
        final SignInVo signInVo = SignInVo.of(request);
        final SignInResultVo resultVo = userSignFacade.signIn(signInVo);

        return ResponseEntity.status(HttpStatus.OK).body(SignInResponse.of(resultVo));
    }

    /**
     * Sign-out.
     *
     * @return Message for success
     */
    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut() {
        userSignFacade.signOut();

        return ResponseEntity.status(HttpStatus.OK).body("로그아웃되었어요.");
    }

    /**
     * Check password matches before change.
     *
     * @param password Current password
     * @return Message for valid
     */
    @GetMapping("/change/check/password")
    @SignInCheck(userType = {UserTypeEnum.USER, UserTypeEnum.ADMIN})
    public ResponseEntity<String> checkPasswordBeforeChange(@RequestParam final String password) {
        final String accountId = userSignFacade.getCurrentAccountId();

        userSignFacade.passwordMatches(accountId, password);

        return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 일치해요.");
    }

    /**
     * Change password.
     *
     * @param password Password
     * @return Message for change success
     */
    @PatchMapping("/change/password")
    @SignInCheck(userType = {UserTypeEnum.USER, UserTypeEnum.ADMIN})
    public ResponseEntity<Long> changePassword(@RequestParam final String password) {
        final String accountId = userSignFacade.getCurrentAccountId();

        validatePattern(
            password,
            ValidationConstants.PASSWORD_PATTERN,
            ValidationConstants.PASSWORD_PATTERN_INVALID_MESSAGE
        );

        final Long userId = userSignFacade.changePassword(accountId, password);

        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }

    /**
     * Check nickname validation and duplication before change.
     *
     * @param nickname Nickname to check
     * @return Message for valid
     */
    @GetMapping("/change/check/nickname")
    @SignInCheck(userType = {UserTypeEnum.USER, UserTypeEnum.ADMIN})
    public ResponseEntity<String> checkNicknameBeforeChange(@RequestParam final String nickname) {
        userSignFacade.getCurrentAccountId();     // Use for authorization

        validatePattern(
            nickname,
            ValidationConstants.NICKNAME_PATTERN,
            ValidationConstants.NICKNAME_PATTERN_INVALID_MESSAGE
        );

        userSignFacade.checkDuplicatedNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK).body("바꿀 수 있는 닉네임이에요.");
    }

    /**
     * Change account's nickname.
     *
     * @param nickname Nickname
     * @return Message for change success
     */
    @PatchMapping("/change/nickname")
    @SignInCheck(userType = {UserTypeEnum.USER, UserTypeEnum.ADMIN})
    public ResponseEntity<Long> changeAccountNickname(@RequestParam final String nickname) {
        final String accountId = userSignFacade.getCurrentAccountId();
        validatePattern(
            nickname,
            ValidationConstants.NICKNAME_PATTERN,
            ValidationConstants.NICKNAME_PATTERN_INVALID_MESSAGE
        );
        userSignFacade.checkDuplicatedNickname(nickname);

        final Long userId = userSignFacade.changeNickname(accountId, nickname);

        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }

    /**
     * Validate pattern.
     *
     * @param value value to check validation
     * @param pattern Value Pattern
     * @param invalidMessage Message for invalidation
     */
    private void validatePattern(final String value, final String pattern, final String invalidMessage) {
        if (null == value || !value.matches(pattern)) {
            throw new BusinessException(invalidMessage, ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
