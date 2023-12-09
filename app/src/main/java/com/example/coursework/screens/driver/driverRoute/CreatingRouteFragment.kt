package com.example.coursework.screens.driver.driverRoute

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coursework.R
import com.example.coursework.databinding.FragmentCreatingDriverRouteBinding
import com.example.coursework.retrofit.CreateTrackRequest
import com.example.coursework.retrofit.Location
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates.notNull


class CreatingRouteFragment : Fragment() {

    private val args : CreatingRouteFragmentArgs by navArgs()

    private var _binding: FragmentCreatingDriverRouteBinding? = null
    private val binding get() = _binding!!
    private var maxSeats by notNull<Int>()

    private lateinit var viewModel: CreatingRouteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreatingDriverRouteBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CreatingRouteViewModel::class.java)

        binding.fromContainer.text = if(args.startAddress == "") "Откуда" else args.startAddress
        binding.toContainer.text = if(args.endAddress == "") "Куда" else args.endAddress

        binding.fromContainer.setOnClickListener {
            view.findNavController().navigate(R.id.action_creatingRouteFragment_to_mapSelectRouteFragment)
        }
        binding.toContainer.setOnClickListener {
            view.findNavController().navigate(R.id.action_creatingRouteFragment_to_mapSelectRouteFragment)
        }



        binding.done.setOnClickListener {
            viewModel.createRoute(
                CreateTrackRequest(
                    startLocation = Location(arrayOf(args.startX.toDouble(), args.startY.toDouble()), args.startAddress),
                    endLocation = Location(arrayOf(args.endX.toDouble(), args.endY.toDouble()), args.endAddress),
                    maxSeats = maxSeats,
                    departureTime = formatDateForApi(viewModel.selectedTime.value!!),
                    driverComment = binding.edComments.text.toString().trim()
                )
            )
        }

        binding.timePickerButton.setOnClickListener {
            showDateTimePickerDialog()
        }
        viewModel.selectedTime.observe(this, Observer {
            binding.timePickerButton.text = formatDate(it)
        })


        viewModel.responseContainer.observe(this, Observer {
            if (it != null) {

                //navigate to the creating route screen for Driver
                view.findNavController().navigate(R.id.action_creatingRouteFragment_to_routeFragment)

            } else {
                Toast.makeText(activity, "Ошибка!", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.isShowProgress.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })


        val options = arrayOf(1,2,3,4)
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.num_of_seats, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                maxSeats = options.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }


    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd.MM HH:mm")
        return sdf.format(date)
    }
    fun formatDateForApi(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return sdf.format(date)
    }


    private fun showDateTimePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Set the selected date in the calendar
                calendar.set(selectedYear, selectedMonth, selectedDay)

                // Show the time picker after selecting the date
                showTimePickerDialog(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set minimum date as the current date
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        // Optional: Set maximum date as +24 hours from now
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() + (24 * 60 * 60 * 1000)

        datePickerDialog.show()
    }

    private fun showTimePickerDialog(calendar: Calendar) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // Set the selected time in the calendar
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                // Handle the selected date and time
                val selectedDateTime = calendar.time
                viewModel.selectedTime.value = calendar.time

                // You can use the selectedDateTime as needed
                // For example, display it in a TextView
                // textView.text = selectedDateTime.toString()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // Set to true if you want 24-hour format, false for 12-hour format
        )

        timePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}