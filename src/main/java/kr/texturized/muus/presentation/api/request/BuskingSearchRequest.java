package kr.texturized.muus.presentation.api.request;

public record BuskingSearchRequest(
    double latitude,
    double longitude,
    double widthMeter,
    double heightMeter
) {

}
