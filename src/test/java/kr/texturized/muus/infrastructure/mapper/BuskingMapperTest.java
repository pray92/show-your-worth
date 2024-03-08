package kr.texturized.muus.infrastructure.mapper;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import kr.texturized.muus.dao.BuskingDao;
import kr.texturized.muus.domain.entity.Busking;
import kr.texturized.muus.domain.entity.User;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.domain.vo.BuskingCreateModelVo;
import kr.texturized.muus.domain.vo.BuskingProfileResultVo;
import kr.texturized.muus.domain.vo.BuskingSearchResultVo;
import kr.texturized.muus.infrastructure.repository.UserRepository;
import kr.texturized.muus.test.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class BuskingMapperTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuskingDao buskingDao;

    @Autowired
    private BuskingMapper buskingMapper;

    private User saveUser1 = null;
    private User saveUser2 = null;

    private Long buskingId1 = 0L;
    private final Double latitude1 = 27.00001;
    private final Double longitude1 = 120.00001;
    private final LocalDateTime managedStartTime1 = LocalDateTime.now().plusDays(1L);
    private final LocalDateTime managedEndTime1 = LocalDateTime.now().plusDays(1L).plusHours(1L);
    private final List<String> keyword1 = Arrays.asList("a1", "a2", "a3");
    private final List<String> imagePaths1 = Arrays.asList("image1", "image2", "image3");

    private Long buskingId2 = 0L;
    private final Double latitude2 = 27.51234;
    private final Double longitude2 = 120.51234;
    private final LocalDateTime managedStartTime2 = LocalDateTime.now().plusDays(2L);
    private final LocalDateTime managedEndTime2 = LocalDateTime.now().plusDays(2L).plusHours(1L);
    private final List<String> keyword2 = Arrays.asList("b1", "b2", "b3");
    private final List<String> imagePaths2 = Arrays.asList("image3", "image4", "image5");

    @BeforeEach
    void beforeEach() {
        saveUser1 = userRepository.save(User.builder()
                .accountId("redgem92")
                .password("asdfqwerzxcv")
                .nickname("HoneyFist")
                .userType(UserTypeEnum.USER)
                .build());

        saveUser2 = userRepository.save(User.builder()
                .accountId("redgem920")
                .password("asdfqwerzxcv")
                .nickname("HoneyFist2")
                .userType(UserTypeEnum.USER)
                .build());

        buskingId1 = saveBusking(
                saveUser1,
                latitude1, longitude1,
                managedStartTime1,
                managedEndTime1,
                keyword1,
                imagePaths1
        );
        buskingId2 = saveBusking(
                saveUser1,
                latitude2, longitude2,
                managedStartTime2,
                managedEndTime2,
                keyword2,
                imagePaths2
        );
    }

    private Long saveBusking(
        User user,
        double latitude, double longitude,
        LocalDateTime start, LocalDateTime end,
        List<String> keywords, List<String> imagePaths
    ) {
        return buskingDao.create(new BuskingCreateModelVo(
                Busking.builder()
                        .host(user)
                        .title("Test")
                        .description("Test")
                        .latitude(latitude)
                        .longitude(longitude)
                        .managedStartTime(start)
                        .managedEndTime(end)
                    .build(),
                keywords,
                imagePaths));
    }

    @Test
    void getActiveBuskingInMap() {
        List<BuskingSearchResultVo> buskings = buskingMapper.search(
            27.0,
            120.0,
            1.5,
            1.5
        );
        assertThat(buskings.size()).isEqualTo(2);

        buskings = buskingMapper.search(
            27.0,
            120.0,
            0.5,
            0.5
        );
        assertThat(buskings.size()).isEqualTo(1);
    }

    @Test
    void getBuskingProfile() {
        final BuskingProfileResultVo vo1 = buskingMapper.profile(buskingId1).get();
        assertThat(vo1.getUserId()).isEqualTo(saveUser1.getId());
        assertThat(vo1.getNickname()).isEqualTo(saveUser1.getNickname());
        assertThat(vo1.getProfileImagePath()).isNullOrEmpty();
        assertThat(vo1.getLatitude()).isEqualTo(latitude1);
        assertThat(vo1.getLongitude()).isEqualTo(longitude1);
        assertThat(vo1.getTitle()).isEqualTo("Test");
        assertThat(vo1.getDescription()).isEqualTo("Test");
        assertThat(vo1.getManagedStartTime()).isEqualTo(managedStartTime1);
        assertThat(vo1.getManagedEndTime()).isEqualTo(managedEndTime1);
        assertThat(vo1.getEndTime()).isNull();
        vo1.getKeywords().forEach(keyword -> assertThat(keyword).isIn(keyword1));
        assertThat(vo1.getImagePaths()).isEqualTo(imagePaths1);

        final BuskingProfileResultVo vo2 = buskingMapper.profile(buskingId2).get();
        assertThat(vo2.getUserId()).isEqualTo(saveUser1.getId());
        assertThat(vo2.getNickname()).isEqualTo(saveUser1.getNickname());
        assertThat(vo2.getProfileImagePath()).isNullOrEmpty();
        assertThat(vo2.getLatitude()).isEqualTo(latitude2);
        assertThat(vo2.getLongitude()).isEqualTo(longitude2);
        assertThat(vo2.getTitle()).isEqualTo("Test");
        assertThat(vo2.getDescription()).isEqualTo("Test");
        assertThat(vo2.getManagedStartTime()).isEqualTo(managedStartTime2);
        assertThat(vo2.getManagedEndTime()).isEqualTo(managedEndTime2);
        assertThat(vo2.getEndTime()).isNull();
        vo2.getKeywords().forEach(keyword -> assertThat(keyword).isIn(keyword2));
        assertThat(vo2.getImagePaths()).isEqualTo(imagePaths2);
    }

    @Test
    void validateBuskingAndUser() {
        assertThat(buskingMapper.isBuskingMadeByUser(buskingId1, saveUser1.getId())).isTrue();
        assertThat(buskingMapper.isBuskingMadeByUser(buskingId2, saveUser1.getId())).isTrue();
    }

    @Test
    void validateWithUnknownUserThenReturnFalse() {
        assertThat(buskingMapper.isBuskingMadeByUser(buskingId1, -1L)).isFalse();
        assertThat(buskingMapper.isBuskingMadeByUser(buskingId2, 1_000_000_000L)).isFalse();
    }

    @Test
    void validateWithBuskingNotOwnedThenReturnFalse() {
        assertThat(buskingMapper.isBuskingMadeByUser(buskingId1 + 10L, saveUser1.getId())).isFalse();
        assertThat(buskingMapper.isBuskingMadeByUser(buskingId2 + 4_000L, saveUser1.getId())).isFalse();
    }

    @Test
    void validateUserNotMadeBuskingReturnFalse() {
        assertThat(buskingMapper.isBuskingMadeByUser(buskingId1, saveUser2.getId())).isFalse();
        assertThat(buskingMapper.isBuskingMadeByUser(buskingId2, saveUser2.getId())).isFalse();
    }

}
