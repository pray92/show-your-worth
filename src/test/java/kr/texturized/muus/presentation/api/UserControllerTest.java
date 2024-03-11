package kr.texturized.muus.presentation.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import kr.texturized.muus.common.error.exception.ErrorCode;
import kr.texturized.muus.test.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@Slf4j
public class UserControllerTest extends IntegrationTest {


    @Test
    void whenInvalidAccountIdThenReturnException() throws Exception {
        getValidateJsonAndExpectError("/api/users/validate-account", "accountId","", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-account", "accountId","asdfe", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-account", "accountId","Asd12", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-account", "accountId","asdfqwer12!", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-account", "accountId","12!DSsaFfqwer", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-account", "accountId","#####", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-account", "accountId","1q2w3e4r1!", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-account", "accountId","asdfqwerzxcvasdfqwer", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-account", "accountId","f-lab.kr", ErrorCode.INVALID_INPUT_VALUE);
    }

    @Test
    void whenValidAccountIdThenReturnOK() throws Exception {
        getValidateJsonAndExpectOK("/api/users/validate-account","accountId", "redgem92");
        getValidateJsonAndExpectOK("/api/users/validate-account","accountId", "12qewrsadf");
        getValidateJsonAndExpectOK("/api/users/validate-account","accountId", "123456789");
        getValidateJsonAndExpectOK("/api/users/validate-account","accountId", "zxcvasdfewr");
        getValidateJsonAndExpectOK("/api/users/validate-account","accountId", "REDGEM92");
        getValidateJsonAndExpectOK("/api/users/validate-account","accountId", "Redgem92");
        getValidateJsonAndExpectOK("/api/users/validate-account","accountId", "jisuJiUS12");
    }

    @Test
    void whenInvalidPasswordThenReturnException() throws Exception {
        getValidateJsonAndExpectError("/api/users/validate-password", "password","", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-password", "password","ASDFQ", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-password", "password","1!AS", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-password", "password","1q2w3e4r1!((", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-password", "password","한글도안됨ㅎ", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-password", "password","ASㅎㅁㄴㅇㄹㄴ", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-password", "password","asdfqwerasdf)-+_", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-password", "password","SDFAERGSGBSRTYRTHfdfweyro3fhefaidaosidf", ErrorCode.INVALID_INPUT_VALUE);
    }

    @Test
    void whenValidPasswordThenReturnOK() throws Exception {
        getValidateJsonAndExpectOK("/api/users/validate-password","password", "1q2w3e4r1!");
        getValidateJsonAndExpectOK("/api/users/validate-password","password", "1234567@@#$");
        getValidateJsonAndExpectOK("/api/users/validate-password","password", "123456789");
        getValidateJsonAndExpectOK("/api/users/validate-password","password", "zxcvasdfewr");
        getValidateJsonAndExpectOK("/api/users/validate-password","password", "JIsue%$#@@#%^&");
        getValidateJsonAndExpectOK("/api/users/validate-password","password", "!@#$%^&*");
        getValidateJsonAndExpectOK("/api/users/validate-password","password", "~!@#$%^&DSacvzxc");
    }

    @Test
    void whenInvalidNicknameThenReturnException() throws Exception {
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","a", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","ㅎㅎㅎㅎㅎ", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","@.@", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","오빠라고불러다오!", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","김미어콜베이베~베이베~", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","kamehameha^^", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","~崔志秀~", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","Songoku's げんきぎょく", ErrorCode.INVALID_INPUT_VALUE);
        getValidateJsonAndExpectError("/api/users/validate-nickname", "nickname","김미김미나 김미김미나 뚜두두두두", ErrorCode.INVALID_INPUT_VALUE);
    }

    @Test
    void whenValidNicknameThenReturnOK() throws Exception {
        getValidateJsonAndExpectOK("/api/users/validate-nickname","nickname", "MX");
        getValidateJsonAndExpectOK("/api/users/validate-nickname","nickname", "MX");
        getValidateJsonAndExpectOK("/api/users/validate-nickname","nickname", "F-lab");
        getValidateJsonAndExpectOK("/api/users/validate-nickname","nickname", "jisus.choi");
        getValidateJsonAndExpectOK("/api/users/validate-nickname","nickname", "崔志秀");
        getValidateJsonAndExpectOK("/api/users/validate-nickname","nickname", "Songoku-げんきぎょく");
        getValidateJsonAndExpectOK("/api/users/validate-nickname","nickname", "_--_");
        getValidateJsonAndExpectOK("/api/users/validate-nickname","nickname", "im-so-sexy");
        getValidateJsonAndExpectOK("/api/users/validate-nickname","nickname", "strong_minsu");
    }

    void getValidateJsonAndExpectError(
        final String uri,
        final String key,
        final String value,
        final ErrorCode errorCode
    )
        throws Exception {
        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .param(key, value))
            .andDo(print())
            .andExpect(jsonPath("message").value(errorCode.getMessage()))
            .andExpect(jsonPath("status").value(errorCode.getStatus()))
            .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    void getValidateJsonAndExpectOK(final String uri, final String key, final String value)
        throws Exception {
        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .param(key, value))
            .andDo(print())
            .andExpect(status().isOk());
    }


    @Test
    void withoutSignInThenChangeNicknameAndPassword() throws Exception {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("password", "qwer1234");
        getMethod("/api/users/check-password",  map,  ErrorCode.HANDLE_ACCESS_DENIED);
        patchMethod("/api/users/password", map, ErrorCode.HANDLE_ACCESS_DENIED);
        map.clear();
        map.add("nickname", "nickname");
        getMethod("/api/users/check-password", map, ErrorCode.HANDLE_ACCESS_DENIED);
        patchMethod("/api/users/password", map, ErrorCode.HANDLE_ACCESS_DENIED);
    }

    private void getMethod(
        final String url,
        final MultiValueMap<String, String> map,
        final ErrorCode errorCode
    ) throws Exception {
        final ResultActions resultActions = mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .params(map))
            .andDo(print());
        if(null != errorCode) {
            resultActions
                .andExpect(jsonPath("message").value(errorCode.getMessage()))
                .andExpect(jsonPath("status").value(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
        } else {
            resultActions.andExpect(status().isOk());
        }
    }

    private void patchMethod(
        final String url,
        final MultiValueMap<String, String> map,
        final ErrorCode errorCode
    ) throws Exception {
        final ResultActions resultActions = mvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .params(map))
            .andDo(print());
        if(null != errorCode) {
            resultActions
                .andExpect(jsonPath("message").value(errorCode.getMessage()))
                .andExpect(jsonPath("status").value(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
        } else {
            resultActions.andExpect(status().isOk());
        }
    }
}
