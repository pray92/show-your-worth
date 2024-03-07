package kr.texturized.muus.infrastructure.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import kr.texturized.muus.dao.BuskingDao;
import kr.texturized.muus.domain.entity.*;
import kr.texturized.muus.domain.vo.BuskingCreateModelVo;
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

    @Autowired
    private BuskingDao buskingDao;

    @Autowired
    private KeywordRepository keywordRepository;

    private Busking busking = null;

    @BeforeEach
    void beforeEach() {
        userRepository.save(User.builder()
                .accountId("redgem92")
                .password("asdfqwerzxcv")
                .nickname("HoneyFist")
                .userType(UserTypeEnum.USER)
            .build());

        busking = buskingRepository.save(Busking.builder()
                .host(userMapper.findByAccountId("redgem92").orElseThrow())
                .title("Test")
                .description("Hello fells")
                .latitude(27.0)
                .longitude(120.0)
                .managedStartTime(LocalDateTime.now().plusHours(1L))
                .managedEndTime(LocalDateTime.now().plusHours(2L))
                .build());

        buskingDao.create(new BuskingCreateModelVo(busking, Arrays.asList("a1", "a2", "a3"), Arrays.asList()));

        log.info("Busking: {}", busking);
    }

    @Test
    void createBusking() {
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
            .isCloseTo(busking.getLatitude(), Percentage.withPercentage(0.000000000000001));
        assertThat(findBusking.get().getLongitude())
            .isCloseTo(busking.getLongitude(), Percentage.withPercentage(0.000000000000001));
        assertThat(findBusking.get().getEndTime()).isEqualTo(busking.getEndTime());
        assertThat(findBusking.get().getManagedStartTime()).isEqualTo(busking.getManagedStartTime());
        assertThat(findBusking.get().getManagedEndTime()).isEqualTo(busking.getManagedEndTime());
    }

    @Test
    void deleteBuskingKeywords() {
        List<Keyword> keywords = keywordRepository.findAllByPostIdAndPostType(busking.getId(), PostTypeEnum.BUSKING);
        final int prevCount = keywords.size();

        keywordRepository.deleteAllInBatchByPostIdAndPostType(busking.getId(), PostTypeEnum.BUSKING);
        keywords = keywordRepository.findAllByPostIdAndPostType(busking.getId(), PostTypeEnum.BUSKING);
        final int curCount = keywords.size();

        assertThat(curCount).isNotEqualTo(prevCount);
    }
}