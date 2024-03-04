package kr.texturized.muus.presentation.api.response;

import kr.texturized.muus.domain.vo.BuskingProfileResultVo;

public record BuskingProfileResponse (BuskingProfileResultVo result){

    public static BuskingProfileResponse of(final BuskingProfileResultVo result) {
        return new BuskingProfileResponse(result);
    }
}
