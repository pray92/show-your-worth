package kr.texturized.muus.presentation.api.response;

import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.domain.vo.UserSignInResultVo;

public record UserSignInResponse(
    String result,                  // It could be account id, value or token
                                    // depending on login management system.
    UserTypeEnum userType
){

    public static UserSignInResponse of(final UserSignInResultVo vo) {
        return new UserSignInResponse(vo.accountId(), vo.userType());
    }
}
