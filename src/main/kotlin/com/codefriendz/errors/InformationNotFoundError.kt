package com.codefriendz.errors

class InformationNotFoundError(message: String = "Requested information not found") : AppServerError(message, ErrorCode.INFORMATION_NOT_FOUND)
