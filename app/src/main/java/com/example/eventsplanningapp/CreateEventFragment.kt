package com.example.eventsplanningapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.eventsplanningapp.R
import java.text.SimpleDateFormat
import java.util.*

class CreateEventFragment : Fragment() {

    private lateinit var editTextEventName: EditText
    private lateinit var buttonSelectDate: Button
    private lateinit var buttonClear: Button
    private lateinit var buttonCreateEvent: Button
    private lateinit var spinnerEventType: Spinner
    private lateinit var selectedDate: String
    private val calendar = Calendar.getInstance()

    private val viewModel: EventsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextEventName = view.findViewById(R.id.editTextEventName)
        buttonSelectDate = view.findViewById(R.id.buttonSelectDate)
        buttonClear = view.findViewById(R.id.buttonClear)
        buttonCreateEvent = view.findViewById(R.id.buttonCreateEvent)
        spinnerEventType = view.findViewById(R.id.spinnerEventType)

        // Populate Spinner
        val eventTypes = resources.getStringArray(R.array.event_types_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, eventTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEventType.adapter = adapter

        buttonSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        buttonClear.setOnClickListener {
            editTextEventName.text.clear()
            spinnerEventType.setSelection(0)
            buttonSelectDate.text = "Select Date" // Reset the button text
            selectedDate = "" // Clear the selected date
        }

        buttonCreateEvent.setOnClickListener {
            createEvent()
        }
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(year, month, day)
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)
            buttonSelectDate.text = selectedDate
        }
        DatePickerDialog(
            requireContext(), dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun createEvent() {
        val eventName = editTextEventName.text.toString()
        val eventType = spinnerEventType.selectedItem.toString()
        if (eventName.isNotEmpty() && selectedDate.isNotEmpty()) {
            val event = Event(eventName, selectedDate, eventType)
            viewModel.addEvent(event) // Add event to ViewModel
            Toast.makeText(requireContext(), "Event Created Successfully", Toast.LENGTH_SHORT).show()
            // Optionally navigate back or reset fields
            editTextEventName.text.clear()
            spinnerEventType.setSelection(0)
            buttonSelectDate.text = "Select Date"
            selectedDate = ""
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
