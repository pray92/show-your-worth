package kr.texturized.muus.dao;

import kr.texturized.muus.domain.entity.Busking;
import kr.texturized.muus.domain.entity.User;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.application.service.exception.BuskingProfileNotFoundException;
import kr.texturized.muus.domain.vo.BuskingCreateModelVo;
import kr.texturized.muus.domain.vo.BuskingProfileResultVo;
import kr.texturized.muus.domain.vo.BuskingUpdateModelVo;
import kr.texturized.muus.infrastructure.mapper.BuskingMapper;
import kr.texturized.muus.infrastructure.repository.UserRepository;
import kr.texturized.muus.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BuskingDaoTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuskingDao buskingDao;

    @Autowired
    private EntityManager em;

    @Autowired
    private BuskingMapper buskingMapper;

    User user = null;
    Busking busking = null;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .accountId("redgem92")
                .password("asdfqwerzxcv")
                .nickname("HoneyFist")
                .userType(UserTypeEnum.USER)
                .build());

        busking = Busking.builder()
                    .host(user)
                    .title("Test")
                    .description("Hello fells")
                    .latitude(27.0)
                    .longitude(120.0)
                    .managedStartTime(LocalDateTime.now().plusHours(1L))
                    .managedEndTime(LocalDateTime.now().plusHours(2L))
                .build();

        buskingDao.create(new BuskingCreateModelVo(
                busking,
                Arrays.asList("a1", "a2", "a3"),
                Arrays.asList())
        );
    }

    @Test
    void checkUpdateBusking() {
        final String changedTitle = "changedTitle";
        final String changedDescription = "changedDecription";
        final Double changedLatitude = 120.0;
        final Double changedLongitude = 27.0;
        final LocalDateTime changedStartTime = LocalDateTime.now().plusMinutes(1L);
        final LocalDateTime changedEndTime = LocalDateTime.now().plusHours(1L);
        final List<String> changedKeywords = Arrays.asList("b1", "b2");

        buskingDao.updateBusking(BuskingUpdateModelVo.of(
                busking.getId(),
                changedLatitude,
                changedLongitude,
                changedTitle,
                changedDescription,
                changedStartTime,
                changedEndTime,
                changedKeywords
            ));

        // JPA의 영속성 컨텍스트에 저장된 상태고 DB에 반영되지 않은 상태라 Mapper로는 정상 조회가 불가능해요.
        // 따라서 영속성 컨텍스트를 플러시 처리했어요.
        em.flush();

        final BuskingProfileResultVo vo = buskingMapper.profile(
                busking.getId()).orElseThrow(BuskingProfileNotFoundException::new);

        assertThat(vo.getTitle()).isEqualTo(changedTitle);
        assertThat(vo.getDescription()).isEqualTo(changedDescription);
        assertThat(vo.getLatitude()).isEqualTo(changedLatitude);
        assertThat(vo.getLongitude()).isEqualTo(changedLongitude);
        assertThat(vo.getManagedStartTime()).isEqualTo(changedStartTime);
        assertThat(vo.getManagedEndTime()).isEqualTo(changedEndTime);
        assertThat(vo.getKeywords().size()).isEqualTo(changedKeywords.size());
        vo.getKeywords().forEach(keyword -> assertThat(keyword).isIn(changedKeywords));
    }
}