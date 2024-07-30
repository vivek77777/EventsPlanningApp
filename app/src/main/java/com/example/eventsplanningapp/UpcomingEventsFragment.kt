package com.example.eventsplanningapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UpcomingEventsFragment : Fragment() {

    private val viewModel: EventsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming_events, container, false)

        // Setup RecyclerView
        val eventsRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewUpcomingEvents)
        eventsRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = EventAdapter()
        eventsRecyclerView.adapter = adapter

        // Observe filtered events and submit to adapter
        viewModel.filteredEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        // Setup Spinner
        val spinnerOptions = view.findViewById<Spinner>(R.id.spinnerOptions)
        val eventTypes = arrayOf("All", "Festival", "Workshop", "Conference", "Meeting", "Party", "Seminar")

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            eventTypes
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOptions.adapter = spinnerAdapter

        spinnerOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = eventTypes[position]
                viewModel.filterEventsByType(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where no item is selected, if necessary
            }
        }

        return view
    }
}
