package com.dicoding.capstone.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.data.ClassRequest
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.data.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateClassViewModel : ViewModel() {

    private lateinit var userPreference: UserPreference

    fun initialize(context: Context) {
        userPreference = UserPreference(context)
    }

    fun saveSchedule(
        context: Context,
        className: String,
        students: List<String> = emptyList(),
        subject: String,
        callback: (Boolean, String) -> Unit
    ) {
        if (className.isEmpty() || subject.isEmpty()) {
            callback(false, "Please fill all the fields")
            return
        }

        val token = userPreference.getToken()
        if (token == null) {
            callback(false, "User not authenticated")
            return
        }

        // Retrieve user info from UserPreference directly
        val teacherUsername = userPreference.getUserName()
        if (teacherUsername != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val classRequest = ClassRequest(
                        studentId = students,
                        teacherUsername = teacherUsername,
                        subject = subject,
                        className = className
                    )

                    val response = ApiService.instance.createClass(classRequest).execute()

                    if (response.isSuccessful && response.body() != null) {
                        val classResponse = response.body()!!
                        callback(true, "Class created successfully with code: ${classResponse.classCode}")
                        Log.d("CreateClassViewModel", "Class created successfully with code: ${classResponse.classCode}")
                    } else {
                        callback(false, "Failed to create class: ${response.message()}")
                    }
                } catch (e: Exception) {
                    callback(false, "Failed to create class: ${e.message}")
                }
            }
        } else {
            callback(false, "Failed to retrieve teacher username")
        }
    }
}

