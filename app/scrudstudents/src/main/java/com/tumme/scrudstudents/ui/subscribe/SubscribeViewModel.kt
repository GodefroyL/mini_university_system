package com.tumme.scrudstudents.ui.subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {
    val subscribes: StateFlow<List<SubscribeEntity>> =
        repo.getAllSubscribes().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun insertSubscribe(subscribe: SubscribeEntity) = viewModelScope.launch {
        repo.insertSubscribe(subscribe)
        _events.emit("Subscription saved")
    }

    fun deleteSubscribe(subscribe: SubscribeEntity) = viewModelScope.launch {
        repo.deleteSubscribe(subscribe)
        _events.emit("Subscription deleted")
    }

    suspend fun getSubscribeByStudent(studentId: Int) = repo.getSubscribesByStudent(studentId)
    suspend fun getSubscribeByCourse(courseId: Int) = repo.getSubscribesByCourse(courseId)
}
