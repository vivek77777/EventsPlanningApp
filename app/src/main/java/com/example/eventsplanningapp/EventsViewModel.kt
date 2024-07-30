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
        _events.value?.add(event)
        _events.value = _events.value
        // Update filtered events
        _filteredEvents.value = _events.value
    }

    fun filterEventsByType(type: String) {
        _filteredEvents.value = if (type == "All") {
            _events.value
        } else {
            _events.value?.filter { it.type == type }
        }
    }
}