package kr.texturized.muus.application.service.exception;

import kr.texturized.muus.common.error.exception.BusinessException;
import kr.texturized.muus.common.error.exception.ErrorCode;

public class BuskingProfileNotFoundException extends BusinessException {
    public BuskingProfileNotFoundException() { super(ErrorCode.BUSKING_PROFILE_NOT_FOUND); }
}
