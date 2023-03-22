package com.codefriendz.errors

class ProjectNameExistsError(message: String) : AppServerError(message, ErrorCode.DUPLICATE_PROJECT_NAME)
