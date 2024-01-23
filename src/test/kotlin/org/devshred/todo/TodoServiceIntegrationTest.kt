package org.devshred.todo

import io.mockk.every
import io.mockk.mockk
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import todo.model.CreateTodoItem

@SpringBootTest
@ActiveProfiles("test")
class TodoServiceIntegrationTest {
    @Autowired
    private lateinit var service: TodoService

    @BeforeEach
    fun mockAuthentication() {
        val randomOwner = randomAlphabetic(24)
        val authentication = mockk<Authentication>()
        val securityContext: SecurityContext = mockk<SecurityContext>()
        every { securityContext.authentication } returns authentication
        every { authentication.isAuthenticated } returns true
        every { authentication.name } returns randomOwner
        SecurityContextHolder.setContext(securityContext)
    }

    @Test
    fun `insert items with correct priority`() {
        val items: MutableMap<Int, TodoEntity> = mutableMapOf()
        for (i in 1..10) {
            items[i] = service.save(CreateTodoItem("Task $i"))
            assertThat(items[i]?.priority).isEqualTo(i)
        }
    }

    @Test
    fun `decrease priority of an item`() {
        val items: MutableMap<Int, TodoEntity> = mutableMapOf()
        for (i in 1..5) {
            items[i] = service.save(CreateTodoItem("Task $i"))
            assertThat(items[i]?.priority).isEqualTo(i)
        }

        service.changePriority(items[4]!!, 2)

        val changedItems = service.allTodoItems()

        assertThat(changedItems.map { it.text })
            .containsExactly("Task 1", "Task 4", "Task 2", "Task 3", "Task 5")
    }

    @Test
    fun `increase priority of an item`() {
        val items: MutableMap<Int, TodoEntity> = mutableMapOf()
        for (i in 1..5) {
            items[i] = service.save(CreateTodoItem("Task $i"))
            assertThat(items[i]?.priority).isEqualTo(i)
        }

        service.changePriority(items[2]!!, 4)

        val changedItems = service.allTodoItems()

        assertThat(changedItems.map { it.text })
            .containsExactly("Task 1", "Task 3", "Task 4", "Task 2", "Task 5")
    }

    @Test
    fun `increase priority to a currently not existing value`() {
        val items: MutableMap<Int, TodoEntity> = mutableMapOf()
        for (i in 1..5) {
            items[i] = service.save(CreateTodoItem("Task $i"))
            assertThat(items[i]?.priority).isEqualTo(i)
        }

        service.changePriority(items[2]!!, 10)

        val changedItems = service.allTodoItems()

        assertThat(changedItems.map { it.text })
            .containsExactly("Task 1", "Task 3", "Task 4", "Task 5", "Task 2")
    }

    @Test
    fun `if decrease priority to a value lower than 1 - then order stays the same`() {
        val items: MutableMap<Int, TodoEntity> = mutableMapOf()
        for (i in 1..5) {
            items[i] = service.save(CreateTodoItem("Task $i"))
            assertThat(items[i]?.priority).isEqualTo(i)
        }

        service.changePriority(items[3]!!, -2)

        val changedItems = service.allTodoItems()

        assertThat(changedItems.map { it.text })
            .containsExactly("Task 1", "Task 2", "Task 3", "Task 4", "Task 5")
    }
}
