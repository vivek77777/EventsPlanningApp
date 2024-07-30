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
import java.util.Calendar

class CreateEventFragment : Fragment() {

    private val viewModel: EventsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_event, container, false)

        val editTextEventName = view.findViewById<EditText>(R.id.editTextEventName)
        val buttonSelectDate = view.findViewById<Button>(R.id.buttonSelectDate)
        val spinnerEventType = view.findViewById<Spinner>(R.id.spinnerEventType)
        val buttonCreateEvent = view.findViewById<Button>(R.id.buttonCreateEvent)
        val buttonClear = view.findViewById<Button>(R.id.buttonClear)

        // Set up the spinner with event types
        val eventTypes = arrayOf("Festival", "Workshop", "Conference", "Meeting", "Party", "Seminar")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, eventTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEventType.adapter = adapter

        // Set up date picker dialog
        buttonSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "${dayOfMonth}/${month + 1}/$year"
                    buttonSelectDate.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Create event button click listener
        buttonCreateEvent.setOnClickListener {
            val eventName = editTextEventName.text.toString()
            val eventDate = buttonSelectDate.text.toString()
            val eventType = spinnerEventType.selectedItem.toString()

            if (eventName.isNotBlank() && eventDate != getString(R.string.select_date)) {
                val newEvent = Event(eventName, eventDate, eventType)
                viewModel.addEvent(newEvent)
                Toast.makeText(requireContext(), "Event created!", Toast.LENGTH_SHORT).show()
                clearInputs(editTextEventName, buttonSelectDate, spinnerEventType)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Clear button click listener
        buttonClear.setOnClickListener {
            clearInputs(editTextEventName, buttonSelectDate, spinnerEventType)
        }

        return view
    }

    private fun clearInputs(editText: EditText, button: Button, spinner: Spinner) {
        editText.text.clear()
        button.text = getString(R.string.select_date)
        spinner.setSelection(0)
    }
}