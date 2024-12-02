package com.example.nfcattendancesystem.data

data class CollegeClass(
    val id: Int,
    val class_name: String,
    val class_room: String,
    val class_day: String,
    val class_start: String,
    val class_end: String,
    val teacher_id: Int,
)