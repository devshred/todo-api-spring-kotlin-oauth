package org.devshred.todo

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.io.Serializable

class RefreshableRepositoryImpl<T : Any, ID : Serializable>(
    entityInformation: JpaEntityInformation<T, ID>,
    private val entityManager: EntityManager
) :
    SimpleJpaRepository<T, ID>(entityInformation, entityManager), RefreshableRepository<T, ID> {
    @Transactional
    override fun refresh(t: T) {
        entityManager.refresh(t)
    }

    @Transactional
    override fun saveAndRefresh(t: T): T {
        val saved: T = save(t)
        entityManager.flush()
        entityManager.refresh(saved)
        return saved
    }
}
