package com.codefriendz.models.user

import org.springframework.data.annotation.Id
import java.util.UUID

data class CodeFriendzAppUser(
    val displayName: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    @Id
    var id: UUID? = null
) {
    fun toReturnUser()= AppUserNoPassword(displayName, password, email, phoneNumber, id!!)
}
data class AppUserNoPassword(val displayName: String,
                             val password: String,
                             val email: String,
                             val phoneNumber: String,
                             var id: UUID)
