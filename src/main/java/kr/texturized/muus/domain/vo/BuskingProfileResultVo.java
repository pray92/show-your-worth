package kr.texturized.muus.domain.vo;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 버스킹 프로필 조회 결과 DTO.
 */
@Getter
public class BuskingProfileResultVo {
    private Long userId;
    private String nickname;
    private String profileImagePath;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private LocalDateTime managedStartTime;
    private LocalDateTime managedEndTime;
    private LocalDateTime endTime;
    private List<String> keywords = new ArrayList<>();
    private List<String> imagePaths = new ArrayList<>();
}
