package org.devshred.todo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class TheApplicationTest {
    @Autowired
    private lateinit var service: TodoService

    @Test
    fun contextLoads() {
        assertThat(service).isNotNull
    }
}
