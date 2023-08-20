package com.codefriendz.errors

class UnauthorizedActionError(message: String) : AppServerError(message, ErrorCode.DUPLICATE_PROJECT_NAME)