package org.devshred.todo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable

@NoRepositoryBean
interface RefreshableRepository<T, ID : Serializable> : JpaRepository<T, ID> {
    fun refresh(t: T)

    fun saveAndRefresh(t: T): T
}
