package com.codefriendz.errors

class DatabaseAccessError(message: String = "There was a problem retrieving your information. Please try again.") : AppServerError(message, ErrorCode.DATABASE_ACCESS_ERROR)
