package kr.texturized.muus.common.config;

import java.net.InetSocketAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Redis Configuration in production mode.
 */
@Profile("prod")
@Configuration
public class RedisConfig {

    @Value("${redis.session.master.node}")
    private String masterSessionNode;

    @Value("${redis.session.password}")
    private String password;

    /**
     * Session Storage with standalone-mode redis with replicas.
     *
     * @return Redis Connection Factory for Session
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        final InetSocketAddress master = parseIpPort(masterSessionNode);
        final RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(
            master.getHostName(),
            master.getPort()
        );
        standaloneConfig.setPassword(password);

        return new LettuceConnectionFactory(standaloneConfig);
    }

    private InetSocketAddress parseIpPort(String input) {
        String[] parts = input.split(":");
        if (2 != parts.length) {
            throw new IllegalArgumentException(String.format("Invalid ip:port format: %s", input));
        }
        return new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));
    }
}