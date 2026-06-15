package com.dgsw.butja_server.domain.user.domain

import com.dgsw.butja_server.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "tb_user")
class User(
    @Column(nullable = false, unique = true)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var nickname: String
): BaseTimeEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set
}