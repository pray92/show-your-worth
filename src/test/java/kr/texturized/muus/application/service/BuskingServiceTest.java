package kr.texturized.muus.application.service;

import kr.texturized.muus.application.service.exception.DisabledToEndBuskingNowException;
import kr.texturized.muus.application.service.exception.DisabledToStartBuskingNowException;
import kr.texturized.muus.domain.entity.Busking;
import kr.texturized.muus.domain.entity.User;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.application.service.exception.BuskingProfileNotFoundException;
import kr.texturized.muus.domain.vo.BuskingCreateVo;
import kr.texturized.muus.domain.vo.BuskingProfileResultVo;
import kr.texturized.muus.infrastructure.repository.BuskingRepository;
import kr.texturized.muus.infrastructure.repository.UserRepository;
import kr.texturized.muus.test.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;


public class BuskingServiceTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuskingService buskingService;

    @Autowired
    private BuskingRepository buskingRepository;

    @Autowired
    private EntityManager em;

    private Long buskingId;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(User.builder()
                    .accountId("redgem92")
                    .password("asdfqwerzxcv")
                    .nickname("HoneyFist")
                    .userType(UserTypeEnum.USER)
                .build());
        buskingId = buskingService.create(new BuskingCreateVo(
                user.getAccountId(),
                "Test",
                new ArrayList<>(),
                27.51234,
                120.51234,
                new ArrayList<>(),
                "test",
                LocalDateTime.now().plusDays(2L),
                LocalDateTime.now().plusDays(2L).plusHours(1L)
        ));
    }

    @Test
    void createBuskingThenGetValidProfile() {
        BuskingProfileResultVo vo = buskingService.profile(buskingId);
        assertThat(vo).isNotNull();
    }

    @Test
    void createBuskingThenGetInvalidProfile() {
        Assertions.assertThrows(BuskingProfileNotFoundException.class, () -> {
            BuskingProfileResultVo vo = buskingService.profile(-1L);
        });
    }

    @Test
    void alreadyStartedBuskingThenMayNotStartNow() throws InterruptedException {
        Busking busking = buskingRepository.getById(buskingId);
        busking.startNow();

        em.flush();
        Thread.sleep(1100);

        Assertions.assertThrows(DisabledToStartBuskingNowException.class, () -> buskingService.validateBuskingMayStartNow(buskingId));
    }

    @Test
    void readyToStartBuskingThenMayStartNow() {
        buskingService.validateBuskingMayStartNow(buskingId);
    }

    @Test
    void notStartedYetBuskingThenMayNotEndNow() {
        Assertions.assertThrows(DisabledToEndBuskingNowException.class, () -> buskingService.validateBuskingMayEndNow(buskingId));
    }

    @Test
    void alreadyEndedBuskingThenMayNotEndNow() throws InterruptedException {
        Busking busking = buskingRepository.getById(buskingId);
        busking.endNow();

        em.flush();
        Thread.sleep(1100);

        Assertions.assertThrows(DisabledToEndBuskingNowException.class, () -> buskingService.validateBuskingMayEndNow(buskingId));
    }

    @Test
    void startedBuskingThenMayEndNow() throws InterruptedException {
        Busking busking = buskingRepository.getById(buskingId);
        busking.startNow();

        em.flush();
        Thread.sleep(1100);

        buskingService.validateBuskingMayEndNow(buskingId);
    }
}
