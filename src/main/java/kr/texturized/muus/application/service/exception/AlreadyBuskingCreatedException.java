package kr.texturized.muus.application.service.exception;

import kr.texturized.muus.common.error.exception.BusinessException;
import kr.texturized.muus.common.error.exception.ErrorCode;

public class AlreadyBuskingCreatedException extends BusinessException {
    public AlreadyBuskingCreatedException() {
        super(ErrorCode.ALREADY_BUSKING_CREATION);
    }
}
