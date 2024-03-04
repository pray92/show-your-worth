package kr.texturized.muus.application;

import kr.texturized.muus.application.service.SignInOutService;
import kr.texturized.muus.application.service.UserService;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.domain.vo.UserProfileResultVo;
import kr.texturized.muus.domain.vo.UserSignInResultVo;
import kr.texturized.muus.domain.vo.UserSignInVo;
import kr.texturized.muus.domain.vo.UserSignUpVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

/**
 * User 서비스와 로그인 세션 서비스를 관리하는 Facade 서비스.
 */
@Service
@RequiredArgsConstructor
public class UserSignFacade {

    private final UserService userService;
    private final SignInOutService signInOutService;

    /**
     * 회원가입
     *
     * @param vo 회원가입에 필요한 최소 정보 VO
     * @return DB에 등록된 User ID
     */
    @Transactional
    public Long signUp(final UserSignUpVo vo) {
        return userService.signUp(vo);
    }

    /**
     * 로그인
     *
     * @param vo 로그인에 필요한 정보 VO
     * @return 로그인한 유저 정보 VO
     */
    public UserSignInResultVo signIn(final UserSignInVo vo) {
        final UserSignInResultVo resultVo = userService.getAccount(vo);

        signInOutService.signIn(resultVo.accountId());

        return resultVo;
    }

    /**
     * 로그아웃
     */
    public void signOut() {
        signInOutService.signOut();
    }

    /**
     * 프로필 정보를 불러와요.
     *
     * @param userId 조회할 유저의 테이블 ID
     */
    public UserProfileResultVo profile(final Long userId) {
        return userService.profile(userId);
    }

    /**
     * 회원가입하려는 계정 ID 중복 여부를 확인해요.
     *
     * @param accountId 회원가입하려는 계정 ID
     */
    public void checkDuplicatedAccountId(final String accountId) {
        userService.checkDuplicatedAccountId(accountId);
    }

    /**
     * 닉네임 중복 여부를 확인해요.
     *
     * @param nickname 확인 닉네임
     */
    public void checkDuplicatedNickname(final String nickname) {
        userService.checkDuplicatedNickname(nickname);
    }

    /**
     * 닉네임을 변경해요.
     *
     * @param accountId 대상 계정 ID
     * @param nickname 변경할 닉네임
     * @return 성공 시 테이블 ID
     */
    @Transactional
    public Long changeNickname(final String accountId, final String nickname) {
        return userService.changeNickname(accountId, nickname);
    }

    /**
     * 해당 계정의 비밀번호가 일치하는지 확인해요.
     *
     * @param accountId 계정 ID
     * @param password 비밀번호
     */
    public void passwordMatches(final String accountId, final String password) {
        userService.passwordMatches(accountId, password);
    }

    /**
     * 대상 계정의 비밀번호를 변경해요.
     *
     * @param accountId 계정 ID
     * @param password 변경하려는 비밀번호 값
     */
    @Transactional
    public Long changePassword(String accountId, String password) {
        return userService.changePassword(accountId, password);
    }

    /**
     * 대상 계정의 프로필 이미지를 변경해요.
     *
     * @param accountId 계정 ID
     * @param imageFile 프로필 이미지
     * @return 변경된 유저 ID
     */
    @Transactional
    public Long changeProfileImage(final String accountId, final MultipartFile imageFile) {
        return userService.changeProfileImage(accountId, imageFile);
    }

    /**
     * 클라이언트의 계정 ID를 반환해요.
     *
     * @return 클라이언트의 계정 ID
     */
    public String getCurrentAccountId() {
        return signInOutService.getCurrentAccountId();
    }

    /**
     * 해당 계정의 유저 타입을 반환해요.
     *
     * @param accountId 계정 ID
     * @return 계정의 유저 타입
     */
    public UserTypeEnum getAccountIdUserType(final String accountId) {
        return userService.getAccountIdUserType(accountId);
    }
}
