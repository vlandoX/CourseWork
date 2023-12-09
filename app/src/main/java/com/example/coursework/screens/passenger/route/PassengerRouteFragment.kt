package com.example.coursework.screens.passenger.route

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coursework.R
import com.example.coursework.databinding.FragmentPassengerRouteBinding
import com.example.coursework.databinding.FragmentRouteListingBinding

class PassengerRouteFragment : Fragment() {

    private val args: PassengerRouteFragmentArgs by navArgs()

    private var _binding: FragmentPassengerRouteBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = PassengerRouteFragment()
    }

    private lateinit var viewModel: PassengerRouteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPassengerRouteBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PassengerRouteViewModel::class.java)


        binding.run {

            if(args.track == null){
                viewModel.infoRoute()
            }else{
                viewModel.trackId.value = args.track!!.id
                binding.departureTimeTextView.text = args.track!!.departureTime
                binding.fromTextView.text = args.track!!.startLocation.address
                binding.toTextView.text = args.track!!.endLocation.address
                binding.commentTextView.text = args.track!!.driverComment
                binding.infoTextView.text = when (args.track!!.state) {
                    "WAITING_PASSENGER"->"Ожидает пассажира"
                    "WAITING_DEPARTURE"->"Ожидает отправки"
                    "ACTIVE"->"В пути"
                    "FINISHED"->"Завершена"
                    else -> "Ожидает пассажира"
                }
            }
        }

        binding.confirmButton.setOnClickListener {
            viewModel.addToRoute()
        }
        binding.cancelButton.setOnClickListener {
            viewModel.leaveRoute()
        }

        viewModel.responseContainer.observe(this) { track->
            if (track != null ) {
                binding.departureTimeTextView.text = track.departureTime
                binding.fromTextView.text = track.startLocation.address
                binding.toTextView.text = track.endLocation.address
                binding.commentTextView.text = track.driverComment
                binding.infoTextView.text = when (track.state) {
                    "WAITING_PASSENGER"->"Ожидает пассажира"
                    "WAITING_DEPARTURE"->"Ожидает отправки"
                    "ACTIVE"->"В пути"
                    "FINISHED"->"Завершена"
                    else -> "Ожидает пассажира"
                }

                if (track.passenger != null) {
                    binding.confirmButton.visibility = View.GONE
                    binding.cancelButton.visibility = View.VISIBLE
                }
            }

        }

        viewModel.isReturn.observe(this){
            if(it) view.findNavController().navigateUp()
        }

    }

}