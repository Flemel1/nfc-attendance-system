package com.example.nfcattendancesystem.data

data class PresenceRequestData(
    val class_id: Int,
    val student_id: Int,
    val longitude: String,
    val latitude: String,
    val tag_id: String
)