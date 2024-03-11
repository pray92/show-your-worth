package kr.texturized.muus.application.service.exception;

import kr.texturized.muus.common.error.exception.BusinessException;
import kr.texturized.muus.common.error.exception.ErrorCode;

public class DisabledToEndBuskingNowException extends BusinessException {
    public DisabledToEndBuskingNowException() {
        super(ErrorCode.DISABLE_TO_END_BUSKING_NOW);
    }
}
