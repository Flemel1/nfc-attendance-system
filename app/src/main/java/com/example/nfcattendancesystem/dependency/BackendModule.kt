package com.example.nfcattendancesystem.dependency

import com.example.nfcattendancesystem.data.IBackendService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackendModule {
    @Singleton
    @Provides
    fun provideBackendService(): IBackendService = Retrofit.Builder()
        .baseUrl("http://192.168.199.39/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IBackendService::class.java)
}