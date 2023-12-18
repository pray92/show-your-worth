package kr.texturized.muus.common.util;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.texturized.muus.application.service.UserSignFacade;
import kr.texturized.muus.application.service.exception.AuthorizationException;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignInInterceptor implements HandlerInterceptor {

    private final UserSignFacade userSignFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final SignInCheck signInCheck = checkAnnotation(handler);

        if (null != signInCheck) {
            final String currentAccountId = userSignFacade.getCurrentAccountId();
            final UserTypeEnum userType = userSignFacade.getAccountIdUserType(currentAccountId);

            if (Arrays.stream(signInCheck.userType()).noneMatch(type -> type == userType)) {
                throw new AuthorizationException();
            }
        }

        return true;
    }

    private SignInCheck checkAnnotation(Object handler) {
        SignInCheck annotation = null;
        if (handler instanceof HandlerMethod handlerMethod) {
            annotation = handlerMethod.getMethodAnnotation(SignInCheck.class);
        }
        return annotation;
    }
}
