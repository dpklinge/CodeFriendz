package com.codefriendz.models.project

enum class ProjectAuthorization {
    COMMENT, EDIT, ADMIN, VIEW_ONLY;
    fun canMakeComment() = this != VIEW_ONLY

}