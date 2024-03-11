package kr.texturized.muus.application.service.exception;

import kr.texturized.muus.common.error.exception.BusinessException;
import kr.texturized.muus.common.error.exception.ErrorCode;

public class DisabledToStartBuskingNowException extends BusinessException {
    public DisabledToStartBuskingNowException() {
        super(ErrorCode.DISABLE_TO_START_BUSKING_NOW);
    }
}
