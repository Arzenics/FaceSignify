package com.dicoding.capstone.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.viewModel.AuthViewModel
import android.content.Context
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.data.service.ApiConfig
import com.dicoding.capstone.viewModel.ClassViewModel
import com.dicoding.capstone.viewModel.CreateClassViewModel
//import com.dicoding.capstone.viewModel.ClassViewModel
import com.dicoding.capstone.viewModel.JoinClassViewModel

class ViewModelFactory(
    private val context: Context? = null,
    private val userPreference: UserPreference? = null,
    private val apiService: ApiConfig? = null,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(context!!) as T
            }
            modelClass.isAssignableFrom(ClassViewModel::class.java) -> {
                ClassViewModel(context!!) as T
            }
            modelClass.isAssignableFrom(JoinClassViewModel::class.java) -> {
                JoinClassViewModel(apiService!!, userPreference!!) as T
            }
//            modelClass.isAssignableFrom(CreateClassViewModel::class.java) -> {
//                CreateClassViewModel(userPreference!!, apiService!!) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


