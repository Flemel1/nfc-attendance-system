package com.example.nfcattendancesystem.domain

import com.example.nfcattendancesystem.data.CollegeClass
import com.example.nfcattendancesystem.data.IBackendService
import com.example.nfcattendancesystem.data.Presence
import com.example.nfcattendancesystem.data.PresenceRequestData
import com.example.nfcattendancesystem.data.UserLogin
import com.example.nfcattendancesystem.data.UserLoginResponse
import javax.inject.Inject

class BackendUseCase @Inject constructor(
    private val service: IBackendService
) {

    suspend fun getAllClasses(): List<CollegeClass> {
        return service.getAllClasses()
    }

    suspend fun getPresenceByClass(classId: Int): List<Presence> {
        return service.getPresenceByClass(classId)
    }

    suspend fun createPresence(data: PresenceRequestData): List<Presence> {
        return service.createPresence(data)
    }

    suspend fun login(data: UserLogin): UserLoginResponse {
        return service.login(data)
    }
}