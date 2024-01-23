package org.devshred.todo

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@TestPropertySource(locations = ["/application.yaml"])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TodoRepositoryTest {
    @Autowired
    private lateinit var db: TodoRepository

    @Test
    fun saveEntity() {
        assertThat(db.findAll()).isEmpty()
        db.save(TodoEntity(owner = "Peter", text = "Something to do"))
        assertThat(db.findAll()).hasSize(1)
    }

    @Test
    fun findEntity() {
        val text = randomAlphabetic(32)
        val saved = db.save(TodoEntity(owner = "Peter", text = text))

        val found = db.findById(saved.id!!)

        assertThat(found).isPresent().map { it.text }.hasValue(text)
    }

    @Test
    fun checkOrder() {
        val owner = randomAlphabetic(32)

        val saved1 = db.saveAndRefresh(TodoEntity(owner = owner, text = randomAlphabetic(32)))
        val saved2 = db.saveAndRefresh(TodoEntity(owner = owner, text = randomAlphabetic(32)))

        assertThat(saved1.priority).isNotNull().isEqualTo(1)
        assertThat(saved2.priority).isNotNull().isEqualTo(2)
        assertThat(saved1.priority).isLessThan(saved2.priority)
    }
}
