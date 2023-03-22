package com.codefriendz.models.project

import org.springframework.data.annotation.Id
import java.util.UUID

data class Project(
    val owner: UUID,
    @Id
    val name: String,
    val description: String,
    val repoUrl: String,
    val projectImage: ByteArray,
    val messageThreadId: UUID? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Project

        if (owner != other.owner) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (repoUrl != other.repoUrl) return false
        if (!projectImage.contentEquals(other.projectImage)) return false
        if (messageThreadId != other.messageThreadId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = owner.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + repoUrl.hashCode()
        result = 31 * result + projectImage.contentHashCode()
        result = 31 * result + (messageThreadId?.hashCode() ?: 0)
        return result
    }

}