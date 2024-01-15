package org.devshred.todo

import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import todo.model.CreateTodoItem
import todo.model.TodoItem
import todo.model.TodoStatus
import java.util.*

@Service
class TodoService(val db: TodoRepository) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun allTodoItems(): List<TodoItem> {
        val owner = currentUserOrFail
        return db.findByOwner(owner).map { entity -> entity.toTodoItem() }
    }

    fun save(dto: CreateTodoItem): TodoItem {
        val owner = currentUserOrFail
        return db.save(toEntity(dto, owner)).toTodoItem()
    }

    fun findById(id: UUID): TodoItem {
        val owner = currentUserOrFail
        return db.findByIdAndOwner(id, owner).map(TodoEntity::toTodoItem).orElseThrow { NotFoundException() }
    }

    fun updateStatus(id: UUID, status: TodoStatus) {
        val owner = currentUserOrFail
        val entity: TodoEntity = db.findByIdAndOwner(id, owner).orElseThrow { NotFoundException() }
        entity.done = status.done
        db.save(entity)
    }

    fun delete(id: UUID) {
        val owner = currentUserOrFail
        val entity: TodoEntity = db.findByIdAndOwner(id, owner).orElseThrow { NotFoundException() }
        db.delete(entity)
    }

    @Transactional
    fun deleteAll() {
        val owner = currentUserOrFail
        db.deleteByOwner(owner)
    }

    private val currentUserOrFail: String
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication == null || !authentication.isAuthenticated) {
                throw NotAuthenticatedException()
            }
            val username = authentication.name
            log.debug("found user {}", username)
            return username
        }

    companion object {
        private fun toEntity(todoItem: CreateTodoItem, owner: String): TodoEntity {
            return TodoEntity(owner = owner, text = todoItem.text, done = false, id = null)
        }
    }
}