package kr.texturized.muus.test;

import kr.texturized.muus.test.config.TestProfile;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 리포지토리 테스트<br>
 * <br>
 * {@code @DataJapTest} 어노테이션을 통해서 Repository에 대한 Bean만 등록해요.
 *
 * {@code @DataJapTest}는 기본적으로 메모리 DB에 대한 테스트를 진행해요. {@code @AutoConfigureTestDatabase} 어노테이션을 통해 프로파일에 등록된 DB 정보로 대체할 수 있어요.
 *
 * JpaRepository에서 기본적으로 제공해주는 findById, findByAll, deleteById 등은 테스트하지 않아요.
 * 기본적으로 save() null 제약 조건 등의 테스트는 진행해도 좋아요.
 * 주로 커스텀하게 작성한 쿼리 메서드, {@code @Query}로 작성된 JPQL 등의 컴스텀에 추가된 메서드를 테스트해요.<br>
 * <br>
 * ref: <a href=https://github.com/cheese10yun/spring-guide/blob/master/docs/test-guide.md#repository-test>spring-guide Repostory Test</a>
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(TestProfile.TEST)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Ignore
public class RepositoryTest {
}
