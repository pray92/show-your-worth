package kr.texturized.muus.infrastructure.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import kr.texturized.muus.domain.entity.Busking;
import kr.texturized.muus.domain.entity.User;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.infrastructure.mapper.UserMapper;
import kr.texturized.muus.test.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class BuskingRepositoryTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BuskingRepository buskingRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(User.builder()
                .accountId("redgem92")
                .password("asdfqwerzxcv")
                .nickname("HoneyFist")
                .userType(UserTypeEnum.USER)
            .build());
    }

    @Test
    void createBusking() {
        Busking busking = buskingRepository.save(Busking.builder()
            .host(userMapper.findByAccountId("redgem92").orElseThrow())
            .title("Test")
            .description("Hello fells")
            .latitude(27.0)
            .longitude(120.0)
            .managedStartTime(LocalDateTime.now().plusHours(1L))
            .managedEndTime(LocalDateTime.now().plusHours(2L))
        .build());

        log.info("Busking: {}", busking);

        assertThat(buskingRepository.count()).isEqualTo(1);

        Optional<Busking> findBusking = buskingRepository.findById(busking.getId());

        log.info("Find Busking: {}", findBusking);

        assertThat(findBusking).isNotEmpty();
        assertThat(findBusking.get().getId()).isEqualTo(busking.getId());
        assertThat(findBusking.get().getHost()).isSameAs(busking.getHost());
        assertThat(findBusking.get().getTitle()).isEqualTo(busking.getTitle());
        assertThat(findBusking.get().getDescription()).isEqualTo(busking.getDescription());
        assertThat(findBusking.get().getCreateTime()).isEqualTo(busking.getCreateTime());
        assertThat(findBusking.get().getLatitude())
            .isCloseTo(busking.getLatitude(), Percentage.withPercentage(0.000001));
        assertThat(findBusking.get().getLongitude())
            .isCloseTo(busking.getLongitude(), Percentage.withPercentage(0.000001));
        assertThat(findBusking.get().getEndTime()).isEqualTo(busking.getEndTime());
        assertThat(findBusking.get().getManagedStartTime()).isEqualTo(busking.getManagedStartTime());
        assertThat(findBusking.get().getManagedEndTime()).isEqualTo(busking.getManagedEndTime());
    }
}