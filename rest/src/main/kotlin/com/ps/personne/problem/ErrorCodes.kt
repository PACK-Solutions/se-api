package com.ps.personne.problem

object ErrorCodes {
    const val INTERNAL_SERVER_ERROR = "internal_server_error"
    const val BAD_REQUEST = "bad_request"
    const val NOT_FOUND = "not_found"

    val ALL: List<String> = listOf(
        INTERNAL_SERVER_ERROR,
        BAD_REQUEST,
        NOT_FOUND,
    )
}
