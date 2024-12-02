package com.example.nfcattendancesystem.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface IBackendService {
    @GET("class/all")
    suspend fun getAllClasses(): List<CollegeClass>

    @GET("presence/presence-by-class/{classId}")
    suspend fun getPresenceByClass(@Path("classId") classId: Int): List<Presence>

    @Headers("Content-Type: application/json")
    @POST("presence/create")
    suspend fun createPresence(@Body data: PresenceRequestData): List<Presence>

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body data: UserLogin): UserLoginResponse
}