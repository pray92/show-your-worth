package kr.texturized.muus.presentation.api.response;

import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.domain.vo.SignInResultVo;

public record SignInResponse (
    String result,                  // It could be account id, value or token
                                    // depending on login management system.
    UserTypeEnum userType
){

    public static SignInResponse of(final SignInResultVo vo) {
        return new SignInResponse(vo.accountId(), vo.userType());
    }
}
