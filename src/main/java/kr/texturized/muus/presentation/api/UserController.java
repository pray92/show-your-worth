package kr.texturized.muus.presentation.api;

import javax.validation.Valid;

import kr.texturized.muus.application.UserSignFacade;
import kr.texturized.muus.common.error.exception.BusinessException;
import kr.texturized.muus.common.error.exception.ErrorCode;
import kr.texturized.muus.common.util.SignInCheck;
import kr.texturized.muus.common.util.ValidationConstants;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.domain.vo.UserProfileResultVo;
import kr.texturized.muus.domain.vo.UserSignInResultVo;
import kr.texturized.muus.domain.vo.UserSignInVo;
import kr.texturized.muus.domain.vo.UserSignUpVo;
import kr.texturized.muus.presentation.api.request.UserSignInRequest;
import kr.texturized.muus.presentation.api.request.UserSignUpRequest;
import kr.texturized.muus.presentation.api.response.UserSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Rest Controller for User.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserSignFacade userSignFacade;

    /**
     * API for validation of account id.
     *
     * @param accountId account id to use
     * @return Available response.
     */
    @GetMapping("/validate-account")
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
    @GetMapping("/validate-password")
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
    @GetMapping("/validate-nickname")
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
    public ResponseEntity<Long> signUp(@RequestBody @Valid final UserSignUpRequest request) {
        final UserSignUpVo userSignUpVo = UserSignUpVo.of(request);
        final Long userId = userSignFacade.signUp(userSignUpVo);

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
    public ResponseEntity<UserSignInResponse> signIn(@RequestBody final UserSignInRequest request) {
        final UserSignInVo userSignInVo = UserSignInVo.of(request);
        final UserSignInResultVo resultVo = userSignFacade.signIn(userSignInVo);

        return ResponseEntity.status(HttpStatus.OK).body(UserSignInResponse.of(resultVo));
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
     * 유저의 프로필을 조회해요.
     *
     * @param userId 조회할 유저의 테이블 ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResultVo> profile(@PathVariable final Long userId) {
        final UserProfileResultVo resultVo = userSignFacade.profile(userId);

        return ResponseEntity.status(HttpStatus.OK).body(resultVo);
    }

    /**
     * Check password matches before change.
     *
     * @param password Current password
     * @return Message for valid
     */
    @GetMapping("/check-password")
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
    @PatchMapping("/password")
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
     * Change account's nickname.
     *
     * @param nickname Nickname
     * @return Message for change success
     */
    @PatchMapping("/nickname")
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

    /**
     * 프로필 이미지를 변경해요.
     *
     * @param imageFile 변경하려는 프로필 이미지
     * @return 유저 테이블 ID
     */
    @PatchMapping("/profile-image")
    @SignInCheck(userType = {UserTypeEnum.USER, UserTypeEnum.ADMIN})
    public ResponseEntity<Long> changeAccountProfileImage(MultipartFile imageFile) {
        final String accountId = userSignFacade.getCurrentAccountId();
        final Long userId = userSignFacade.changeProfileImage(accountId, imageFile);

        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }
}
