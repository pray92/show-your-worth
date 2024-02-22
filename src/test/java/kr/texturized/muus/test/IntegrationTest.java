package kr.texturized.muus.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import kr.texturized.muus.Main;
import kr.texturized.muus.test.config.TestProfile;
import java.io.IOException;
import java.io.InputStream;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 통합 테스트의 Base 클래스에요. 이를 통해 테스트 전략을 통일성 있게 가져갈 수 있어요.
 *
 * 통합 테스트는 주로 컨트롤러 테스트를 하며, 요청부터 응답까지 전체 플로우를 테스트해요.
 * {@code @ActiveProfiles(TestProfile.TEST)} 설정으로 테스트에 프로파일을 지정해요.
 *
 * 환경별로 yml 파일을 관리하듯이 test도 반드시 별도의 yml로 관리하는 것이 바람직해요.
 *
 * 인터페이스나 enum 클래스를 통해서 프로파일을 관리해요. 오타 실수를 줄일 수 있으며, 전체적인 프로필이 몇개 있는지 한번에 확인이 가능해요.
 *
 * {@code @Transactional} 트랜잭션 어노테이션을 추가하면 자연스레 DB 상태 의존적인 테스트를 자연스럽게 하지 않을 수 있게 되요.
 *
 * 통합 테스트 시 필요한 기능들을 protected로 제공할 수 있어요. API 테스트를 주로 하게 되니 ObjectMapper 등을 제공해줄 수 있어요.
 * 유틸성 메서드들도 protected로 제공해주면 중복 코드 및 테스트 코드의 편의성이 높아져요.
 *
 * 실제로 동작할 필요가 없으니 {@code @Ignore} 어노테이션을 추가해요.
 */
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles(TestProfile.TEST)
@Transactional
public class IntegrationTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ResourceLoader resourceLoader;
    protected final String identifier = "{class-name}/{method-name}";

    protected <T> T readValue(final String path, Class<T> clazz) throws IOException {
        final InputStream json = resourceLoader.getResource(path).getInputStream();
        return objectMapper.readValue(json, clazz);
    }

    protected String readJson(final String path) throws IOException {
        final InputStream inputStream = resourceLoader.getResource(path).getInputStream();
        final ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() {
                return inputStream;
            }
        };
        return byteSource.asCharSource(Charsets.UTF_8).read();
    }
}
