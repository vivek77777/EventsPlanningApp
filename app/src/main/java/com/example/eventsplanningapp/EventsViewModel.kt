package com.example.eventsplanningapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventsViewModel : ViewModel() {

    private val _events = MutableLiveData<MutableList<Event>>(mutableListOf())
    val events: LiveData<MutableList<Event>> get() = _events

    private val _filteredEvents = MutableLiveData<List<Event>>()
    val filteredEvents: LiveData<List<Event>> get() = _filteredEvents

    fun addEvent(event: Event) {
        _events.value?.apply {
            add(event)
            _filteredEvents.value = this // Update filtered events
        }
    }

    fun filterEventsByType(type: String) {
        _filteredEvents.value = if (type == "All") {
            _events.value
        } else {
            _events.value?.filter { it.type == type }
        }
    }
}
