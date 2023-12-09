package com.example.coursework.screens.driver.route

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.coursework.R
import com.example.coursework.databinding.FragmentRouteBinding
import java.text.SimpleDateFormat
import java.util.*

class RouteFragment : Fragment() {

    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = RouteFragment()
    }

    private lateinit var viewModel: RouteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(RouteViewModel::class.java)

        viewModel.responseContainer.observe(this, Observer {
            if (it != null) {

                binding.departureTimeTextView.text = it.departureTime
                binding.fromTextView.text = it.startLocation.address
                binding.toTextView.text = it.endLocation.address
                binding.commentTextView.text = it.driverComment

            } else {
                Toast.makeText(activity, "Ошибка!", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.isShowProgress.observe(this, Observer {
            if (it) {
                binding.routeProgressBar.visibility = View.VISIBLE
            } else {
                binding.routeProgressBar.visibility = View.GONE
            }
        })

        viewModel.isReturn.observe(this) {
            if(it) view.findNavController().navigate(R.id.action_routeFragment_to_creatingRouteFragment)
        }

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.infoRoute()

        binding.cancelButton.setOnClickListener {
            viewModel.deleteRoute()
        }
    }

    fun apiDateToFormatDate(date: String): String {
        val sdf1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val localeDate = sdf1.parse(date)
        val sdf = SimpleDateFormat("dd.MM HH:mm")
        return sdf.format(localeDate)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}