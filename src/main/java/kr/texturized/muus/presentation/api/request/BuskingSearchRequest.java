package kr.texturized.muus.presentation.api.request;

import kr.texturized.muus.domain.vo.BuskingSearchVo;

public record BuskingSearchRequest(
    double latitude,
    double longitude,
    double widthMeter,
    double heightMeter
) {

    public BuskingSearchVo toDto() {
        return new BuskingSearchVo(this.latitude(), this.longitude(), this.widthMeter(), this.heightMeter());
    }
}
