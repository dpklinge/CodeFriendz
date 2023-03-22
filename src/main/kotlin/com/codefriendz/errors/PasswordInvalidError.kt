package com.codefriendz.errors

class PasswordInvalidError(message: String = "Username or password was invalid") : AppServerError(message, ErrorCode.PASSWORD_INVALID)
