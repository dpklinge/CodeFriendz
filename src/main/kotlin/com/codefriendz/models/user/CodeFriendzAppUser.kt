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
    fun stripPassword(): CodeFriendzAppUser = CodeFriendzAppUser(displayName, "", email, phoneNumber, id)
}
