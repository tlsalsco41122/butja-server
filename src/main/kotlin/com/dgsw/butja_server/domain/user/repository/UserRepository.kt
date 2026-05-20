package com.dgsw.butja_server.domain.user.repository

import com.dgsw.butja_server.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
}