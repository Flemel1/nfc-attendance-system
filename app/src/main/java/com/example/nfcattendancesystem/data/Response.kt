package com.example.nfcattendancesystem.data

data class UserLogin(
    val username: String,
    val password: String
)

data class UserLoginResponse(
    val id: Int?,
    val name: String?,
    val status: String,
    val status_code: Int
)