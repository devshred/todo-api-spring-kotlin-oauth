package org.devshred.todo

import org.springframework.data.repository.CrudRepository
import java.util.*

interface TodoRepository : CrudRepository<TodoEntity, UUID> {
    fun findByOwner(owner: String): Iterable<TodoEntity>

    fun findByIdAndOwner(id: UUID, owner: String): Optional<TodoEntity>

    fun deleteByOwner(owner: String)
}