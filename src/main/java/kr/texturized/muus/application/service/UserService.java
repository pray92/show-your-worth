package kr.texturized.muus.application.service;

import java.util.Optional;
import kr.texturized.muus.application.service.exception.DuplicatedAccountIdException;
import kr.texturized.muus.application.service.exception.DuplicatedNicknameException;
import kr.texturized.muus.application.service.exception.InvalidAccountException;
import kr.texturized.muus.common.storage.PostImageStorage;
import kr.texturized.muus.common.util.PasswordEncryptor;
import kr.texturized.muus.domain.entity.User;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.domain.exception.UserNotFoundException;
import kr.texturized.muus.domain.vo.UserProfileResultVo;
import kr.texturized.muus.domain.vo.UserSignInResultVo;
import kr.texturized.muus.domain.vo.UserSignInVo;
import kr.texturized.muus.domain.vo.UserSignUpVo;
import kr.texturized.muus.infrastructure.mapper.UserMapper;
import kr.texturized.muus.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for User Sign up.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PostImageStorage postImageStorage;

    /**
     * Sign up logic with total validation.
     *
     * @param vo Vo for sign-up
     * @return user with {@code Optional<T>} wrapper class,
     *         Optional is recommended to use for return result
     */
    @Transactional
    public Long signUp(final UserSignUpVo vo) {
        checkDuplicatedAccountId(vo.accountId());
        checkDuplicatedNickname(vo.nickname());

        final String encodedPassword = PasswordEncryptor.encrypt(vo.password());
        final User signUpUser = Optional.of(userRepository.save(User.builder()
                    .accountId(vo.accountId())
                    .password(encodedPassword)
                    .nickname(vo.nickname())
                    .userType(UserTypeEnum.USER)
                .build()))
            .map(user -> {
                log.info("Sign up: {}", user);

                return user;
            }).orElseThrow(InvalidAccountException::new);

        return signUpUser.getId();
    }

    /**
     * 유저 프로필을 반환해요.
     *
     * @param userId 대상 유저 테이블 ID
     * @return UI에서 유저 식별을 위한 기본적인 프로필 정보 Vo
     */
    public UserProfileResultVo profile(final Long userId) {
        return userMapper.findProfile(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    /**
     * Get account information matching with account id and password.<br>
     * <br>
     * NOTE: @Transactional(readOnly = true) is effective for selecting a lot of data.
     * Simple selection such as sign-in is not good for it.
     * It causes the overhead in transaction management.
     *
     * @param vo Vo for account validation
     * @return user with {@code Optional<T>} wrapper class,
     *      Optional is recommended to use for return result
     */
    public UserSignInResultVo getAccount(final UserSignInVo vo) {
        final User signInUser = getUser(vo.accountId())
            .filter(user -> PasswordEncryptor.matches(vo.password(), user.getPassword()))
            .orElseThrow(InvalidAccountException::new);

        return UserSignInResultVo.of(signInUser);
    }

    /**
     * Check Account ID and password matched.
     *
     * @param accountId Account ID to check
     * @param password Current password
     */
    public void passwordMatches(final String accountId, final String password) {
        getUser(accountId)
            .filter(user -> PasswordEncryptor.matches(password, user.getPassword()))
            .orElseThrow(InvalidAccountException::new);
    }

    /**
     * Change password.
     *
     * @param accountId Account ID to change
     * @param password Password to change
     */
    @Transactional
    public Long changePassword(
        final String accountId,
        final String password
    ) {
        final User user = getUser(accountId).orElseThrow(InvalidAccountException::new);
        final String encodedPassword = PasswordEncryptor.encrypt(password);
        user.update(encodedPassword, user.getNickname(), user.getProfileImagePath());

        return userRepository.save(user).getId();
    }

    /**
     * Change nickname.
     *
     * @param accountId Account ID to change
     * @param nickname Nickname to change
     */
    @Transactional
    public Long changeNickname(final String accountId, final String nickname) {
        final User user = getUser(accountId).orElseThrow(InvalidAccountException::new);
        user.update(user.getPassword(), nickname, user.getProfileImagePath());

        return userRepository.save(user).getId();
    }

    /**
     * 대상 계정의 프로필 이미지를 변경해요.
     *
     * @param accountId 대상 계정 ID
     * @param imageFile 프로필 이미지
     * @return 변경된 유저 ID
     */
    @Transactional
    public Long changeProfileImage(final String accountId, final MultipartFile imageFile) {
        final User user = getUser(accountId).orElseThrow(InvalidAccountException::new);
        final String uploadPath = postImageStorage.upload(user.getId(), imageFile);
        user.update(user.getPassword(), user.getNickname(), uploadPath);

        return userRepository.save(user).getId();
    }

    /**
     * Get User Entity from DB.
     *
     * @param accountId Account ID
     * @return User Entity
     */
    private Optional<User> getUser(final String accountId) {
        return userMapper.findByAccountId(accountId);
    }

    /**
     * Check whether account id is duplicated.
     *
     * @param accountId account to user
     */
    public void checkDuplicatedAccountId(final String accountId) {
        if (userMapper.existsByAccountId(accountId)) {
            throw new DuplicatedAccountIdException();
        }
    }

    /**
     * Check whether nickname is duplicated.
     *
     * @param nickname nickname to use
     */
    public void checkDuplicatedNickname(final String nickname) {
        if (userMapper.existsByNickname(nickname)) {
            throw new DuplicatedNicknameException();
        }
    }

    /**
     * Get user type for account id.
     *
     * @param accountId Account ID to find user type
     * @return User Type
     */
    public UserTypeEnum getAccountIdUserType(final String accountId) {
        return userMapper.findUserTypeByAccountId(accountId);
    }

}
