package org.devshred.todo

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import todo.model.TodoItem
import java.util.*

@Entity
@Table(name = "todos")
class TodoEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: UUID?,
    val owner: String,
    val text: String,
    var done: Boolean
) {
    fun toTodoItem() = TodoItem(id, text, done)
}
