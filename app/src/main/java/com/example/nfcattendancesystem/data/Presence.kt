package com.example.nfcattendancesystem.data

data class Presence (
    val id: Int,
    val presence_date: String,
    val presence_time: String,
    val class_id: Int,
    val student_id: Int
)