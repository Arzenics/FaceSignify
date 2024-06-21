package com.dicoding.capstone.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.data.ClassDetails
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.data.service.ApiService
import kotlinx.coroutines.launch

class ClassViewModel(context: Context) : ViewModel() {
    private val _classes = MutableLiveData<List<ClassDetails>>()
    val classes: LiveData<List<ClassDetails>> get() = _classes

    private val userPreference = UserPreference(context)

    fun fetchClasses() {
        viewModelScope.launch {
            try {
                val allClasses = ApiService.instance.getClass()
                val currentUser = userPreference.getUserName()
                val filteredClasses = allClasses.filter { classItem ->
                    classItem.teacherUsername == currentUser || classItem.students.contains(currentUser)
                }
                _classes.value = filteredClasses
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

