package kr.texturized.muus.application.service.exception;

import kr.texturized.muus.common.error.exception.BusinessException;
import kr.texturized.muus.common.error.exception.ErrorCode;

public class MismatchedPostAndUserException extends BusinessException {
    public MismatchedPostAndUserException() {
        super(ErrorCode.MISMATCHED_POST_AND_USER);
    }
}
