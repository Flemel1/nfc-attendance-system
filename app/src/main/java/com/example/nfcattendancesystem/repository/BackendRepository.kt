package com.example.nfcattendancesystem.repository

import com.example.nfcattendancesystem.data.CollegeClass
import com.example.nfcattendancesystem.data.RetrofitInstance

class BackendRepository {
    private val backendService = RetrofitInstance.backendService

    suspend fun getAllClasses(): List<CollegeClass> {
        return backendService.getAllClasses()
    }
}