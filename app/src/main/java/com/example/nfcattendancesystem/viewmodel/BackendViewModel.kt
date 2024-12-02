package com.example.nfcattendancesystem.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfcattendancesystem.data.CollegeClass
import com.example.nfcattendancesystem.data.Presence
import com.example.nfcattendancesystem.data.PresenceRequestData
import com.example.nfcattendancesystem.data.UserLogin
import com.example.nfcattendancesystem.data.UserLoginResponse
import com.example.nfcattendancesystem.domain.BackendUseCase
import com.example.nfcattendancesystem.repository.BackendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackendViewModel @Inject constructor(
    private val backendUseCase: BackendUseCase
) : ViewModel() {
//    private val repository = BackendRepository()

    private val _classCollege: MutableStateFlow<List<CollegeClass>> = MutableStateFlow(emptyList())
    private val _selectedClass: MutableStateFlow<CollegeClass?> = MutableStateFlow(null)
    private val _presences: MutableStateFlow<List<Presence>> = MutableStateFlow(emptyList())
    private val _userLoginResponse: MutableStateFlow<UserLoginResponse?> = MutableStateFlow(null)

    val classCollege = _classCollege.asStateFlow()
    val selectedClass = _selectedClass.asStateFlow()
    val presences = _presences.asStateFlow()
    val userLoginResponse = _userLoginResponse.asStateFlow()

    fun fetchAllClass() {
        viewModelScope.launch {
            try {
                val classes = backendUseCase.getAllClasses()
                _classCollege.value = classes
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun fetchPresenceByClass() {
        viewModelScope.launch {
            try {
                if (selectedClass.value == null) {
                    _presences.value = emptyList<Presence>()
                } else {
                    val presences = backendUseCase.getPresenceByClass(selectedClass.value!!.id)
                    _presences.value = presences
                }

            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun createPresence(data: PresenceRequestData) {
        viewModelScope.launch {
            try {
                val status = backendUseCase.createPresence(data)
//                _presences.value = presences

            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
                Log.e("Error", e.cause.toString())
            }
        }
    }

    fun login(data: UserLogin) {
        viewModelScope.launch {
            try {
                val result = backendUseCase.login(data)
                _userLoginResponse.value = result

            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun selectClass(selectedClass: CollegeClass) {
        viewModelScope.launch {
            _selectedClass.value = selectedClass
        }
    }
}