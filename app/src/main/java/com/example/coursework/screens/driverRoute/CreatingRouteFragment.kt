package com.example.coursework.screens.driverRoute

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
import com.example.coursework.R
import com.example.coursework.databinding.FragmentCreatingRouteBinding
import com.example.coursework.retrofit.TrackRequest
import kotlin.properties.Delegates.notNull


class CreatingRouteFragment : Fragment() {

    private var _binding: FragmentCreatingRouteBinding? = null
    private val binding get() = _binding!!
    private var maxSeats by notNull<Int>()

    private lateinit var viewModel: CreatingRouteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreatingRouteBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CreatingRouteViewModel::class.java)

        binding.done.setOnClickListener {
            viewModel.createRoute(
                TrackRequest(
                    maxSeats = maxSeats,
                    driverComment = binding.edComments.text.toString().trim()
                )
            )
        }


        viewModel.responseContainer.observe(this, Observer {
            if (it != null) {

                //navigate to the creating route screen for Passenger
                TODO("переход на карточку готового маршрута")

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




}