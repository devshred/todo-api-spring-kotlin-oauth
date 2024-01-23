package org.devshred.todo

import lombok.AllArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import todo.api.TodoApi
import todo.model.CreateTodoItem
import todo.model.TodoItem
import todo.model.TodoStatus
import java.net.URI
import java.util.*

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/v1")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class TodoController(val service: TodoService) : TodoApi {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun allTodoItems(): ResponseEntity<List<TodoItem>> {
        log.info("get all items")
        return ResponseEntity.ok(service.allTodoItems())
    }

    override fun changeTodoItem(id: UUID, todoStatus: TodoStatus): ResponseEntity<Unit> {
        log.info("change status of {} to {}", id, todoStatus)
        service.updateStatus(id, todoStatus)
        return ResponseEntity.noContent().build()
    }

    override fun createTodoItem(createTodoItem: CreateTodoItem): ResponseEntity<TodoItem> {
        log.info("create item: {}", createTodoItem)
        val todoItem: TodoItem = service.save(createTodoItem).toTodoItem()
        return ResponseEntity.created(URI.create("/" + todoItem.id)).body(todoItem)
    }

    override fun deleteTodoItem(id: UUID): ResponseEntity<Unit> {
        log.info("delete item {}", id)
        service.delete(id)
        return ResponseEntity.noContent().build()
    }

    override fun getTodoItem(id: UUID): ResponseEntity<TodoItem> {
        log.info("get todo item {}", id)
        return ResponseEntity.ok(service.findById(id))
    }

    override fun deleteAllTodoItems(): ResponseEntity<Unit> {
        log.info("delete all items")
        service.deleteAll()
        return ResponseEntity.noContent().build()
    }
}
