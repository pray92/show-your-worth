package kr.texturized.muus.domain.vo;

import kr.texturized.muus.presentation.api.request.BuskingSearchRequest;

public record BuskingSearchVo(double latitude, double longitude, double widthMeter, double heightMeter) {

    public static BuskingSearchVo of(final BuskingSearchRequest request) {
        return new BuskingSearchVo(request.latitude(), request.longitude(), request.widthMeter(), request.heightMeter());
    }
}
