package com.codefriendz.errors.exceptions

import com.codefriendz.errors.InformationNotFoundError

class InformationNotFoundException(val error: InformationNotFoundError) : Exception(error.message)
