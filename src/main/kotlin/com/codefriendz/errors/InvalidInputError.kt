package com.codefriendz.errors

open class InvalidInputError(vararg inputs: String, message: String = "Invalid inputs (${inputs.joinToString()}) were provided") : AppServerError(message, ErrorCode.INVALID_INPUT)
