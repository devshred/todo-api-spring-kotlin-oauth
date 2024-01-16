package org.devshred.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = RefreshableRepositoryImpl::class)
class TheApplication

fun main(args: Array<String>) {
    runApplication<TheApplication>(*args)
}
