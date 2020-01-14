package com.tdrury.example.cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
@Testcontainers
public class RedisCacheIT {

    private RedisCache underTest;

    // container
    @Container
    public GenericContainer redis = new GenericContainer<>("redis:5.0.3-alpine")
            .withExposedPorts(6379);


    @BeforeEach
    public void setUp() {
        String address = redis.getContainerIpAddress();
        Integer port = redis.getMappedPort(6379);
        Jedis jedis = new Jedis(address, port);
        log.debug("setUp: redis container at address={} port={}", address, port);

        // Now we have an address and port for Redis, no matter where it is running
        underTest = new RedisCache(jedis, "test");
    }

    @Test
    public void whenPutString_thenGetStringBack() {
        // given
        underTest.put("test", "example");

        // when
        Optional<String> retrieved = underTest.get("test", String.class);
        log.debug("whenPutString_thenGetStringBack: retrieved={}", retrieved);

        // then
        assertThat(retrieved.isPresent(), is(true));
        assertThat(retrieved.get(), is("example"));
    }

    @Test
    public void whenPutNothing_thenGetNothingBack() {
        // given

        // when
        Optional<String> retrieved = underTest.get("does_not_exist", String.class);
        log.debug("whenPutNothing_thenGetNothingBack: retrieved={}", retrieved);

        // then
        assertThat(retrieved.isPresent(), is(false));
    }

}