package kr.texturized.muus.infrastructure.mapper;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
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
import org.junit.jupiter.api.AfterEach;
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

    private User saveUser = null;
    private Long buskingId1 = 0L;
    private Long buskingId2 = 0L;

    @BeforeEach
    void beforeEach() {
        saveUser = userRepository.save(User.builder()
                .accountId("redgem92")
                .password("asdfqwerzxcv")
                .nickname("HoneyFist")
                //.email("redgem92@gmail.com")
                .userType(UserTypeEnum.USER)
            .build());

        buskingId1 = saveBusking(
                saveUser,
                27.0, 120.0,
                LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().plusDays(1L).plusHours(1L),
                Arrays.asList("a1", "a2", "a3"),
                new ArrayList<>()
        );
        buskingId2 = saveBusking(
                saveUser,
                27.5, 120.5,
                LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().plusDays(1L).plusHours(1L),
                Arrays.asList("b1", "b2", "b3"),
                new ArrayList<>()
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
            1.0,
            1.0
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
    }
}