package com.dgsw.butja_server.domain.user.domain

import jakarta.persistence.*

@Entity
@Table(name = "tb_user")
class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false, unique = true)
    var username: String = ""

    @Column(nullable = false)
    var password: String = ""

    @Column(nullable = false)
    var nickname: String = ""
}