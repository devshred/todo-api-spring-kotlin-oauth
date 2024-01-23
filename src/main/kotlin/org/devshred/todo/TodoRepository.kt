package org.devshred.todo

import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface TodoRepository : CrudRepository<TodoEntity, UUID>, RefreshableRepository<TodoEntity, UUID> {
    fun findByOwnerOrderByPriority(owner: String): Iterable<TodoEntity>

    fun findByIdAndOwner(
        id: UUID,
        owner: String,
    ): Optional<TodoEntity>

    fun findByOwnerAndPriorityGreaterThanEqualAndPriorityLessThan(
        owner: String,
        priorityLowerBoundary: Int,
        priorityUpperBoundary: Int,
    ): Iterable<TodoEntity>

    fun findByOwnerAndPriorityGreaterThanAndPriorityLessThanEqual(
        owner: String,
        priorityLowerBoundary: Int,
        priorityUpperBoundary: Int,
    ): Iterable<TodoEntity>

    fun deleteByOwner(owner: String)
}
