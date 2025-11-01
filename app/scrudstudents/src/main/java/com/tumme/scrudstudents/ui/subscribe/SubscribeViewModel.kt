package com.tumme.scrudstudents.ui.subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    val students: StateFlow<List<StudentEntity>> = repository.getAllStudents()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val courses: StateFlow<List<CourseEntity>> = repository.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val subscribes: StateFlow<List<SubscribeEntity>> = repository.getAllSubscribes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertSubscription(studentId: Int, courseId: Int) {
        viewModelScope.launch {
            val subscription = SubscribeEntity(studentId = studentId, courseId = courseId)
            repository.insertSubscribe(subscription)
        }
    }

    fun deleteSubscribe(subscribe: SubscribeEntity) {
        viewModelScope.launch {
            repository.deleteSubscribe(subscribe)
        }
    }
}
