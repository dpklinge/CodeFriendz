package com.codefriendz.repositories

import com.codefriendz.models.user.CodeFriendzAppUser
import org.springframework.data.r2dbc.repository.R2dbcRepository
import java.util.*

interface UserRepository : R2dbcRepository<CodeFriendzAppUser, UUID> {
    suspend fun findAppUserByEmail(email: String): CodeFriendzAppUser?
    suspend fun findAppUserByPhoneNumber(phoneNumber: String): CodeFriendzAppUser?
}
