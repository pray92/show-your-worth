package kr.texturized.muus.test;

import kr.texturized.muus.test.config.TestProfile;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

/**
 * 서비스 테스트<br>
 * <br>
 * 진행하고자 하는 테스트에만 집중할 수 있어요.
 *
 * 테스트 진행 시 중요 관점이 아닌 것들은 Mocking 처리해서 외부 의존성들을 줄일 수 있어요.
 * 예를 들어, 주문 할인 로직이 제대로 동작하는지에 대한 테스트만 진행하지 이게 실제로 DB에 insert되는지는 해당 테스트의 관심사가 아니에요.
 *
 * 테스트 속도가 빨라요.
 *
 * 대신 의존성 있는 객체를 Mocking하기 때문에 문제가 완결된 테스트는 아니에요.
 *
 * Mocking하기가 귀찮은 단점도 존재해요.
 *
 * Mocking 라이브러리에 대한 학습 비용이 발생해요.<br>
 * <br>
 * ref: <a href=https://github.com/cheese10yun/spring-guide/blob/master/docs/test-guide.md#%EC%84%9C%EB%B9%84%EC%8A%A4-%ED%85%8C%EC%8A%A4%ED%8A%B8>spring-guide 서비스 테스트</a>
 */
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(TestProfile.TEST)
@Ignore
public class MockTest {
}
