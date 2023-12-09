package com.example.coursework.screens.passenger.passengerRoute

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coursework.R
import com.example.coursework.databinding.FragmentCreatingPassengerRouteBinding

class CreatingRouteFragment : Fragment() {

    private val args : CreatingRouteFragmentArgs by navArgs()

    private var _binding: FragmentCreatingPassengerRouteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CreatingRouteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatingPassengerRouteBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreatingRouteViewModel::class.java)

        binding.fromContainer.text = if(args.startAddress == "") "Откуда" else args.startAddress
        binding.toContainer.text = if(args.endAddress == "") "Куда" else args.endAddress

        binding.fromContainer.setOnClickListener {
            view.findNavController().navigate(R.id.action_routeCreationFragment_to_mapSelectRouteFragment)
        }
        binding.toContainer.setOnClickListener {
            view.findNavController().navigate(R.id.action_routeCreationFragment_to_mapSelectRouteFragment)
        }

        binding.searchRouteButton.setOnClickListener {
            val action =CreatingRouteFragmentDirections.actionRouteCreationFragmentToRouteListingFragment(
                args.startX,
                args.startY,
                args.endX,
                args.endY,
            )
            this.findNavController().navigate(action)
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}