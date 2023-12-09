package com.example.coursework.screens.passenger.routeListing

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coursework.R
import com.example.coursework.databinding.FragmentCreatingPassengerRouteBinding
import com.example.coursework.databinding.FragmentRouteListingBinding
import com.example.coursework.retrofit.TrackExpanded
import com.example.coursework.screens.passenger.passengerRoute.CreatingRouteFragmentArgs

class RouteListingFragment : Fragment() {

    private val args : RouteListingFragmentArgs by navArgs()

    companion object {
        fun newInstance() = RouteListingFragment()
    }
    private var _binding: FragmentRouteListingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RouteListingViewModel
    private lateinit var routeAdapter : RouteRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRouteListingBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RouteListingViewModel::class.java)

        setUpRecyclerView()

        routeAdapter.setOnClickListener(object :RouteRecyclerAdapter.OnClickListener{
            override fun onClick(track: TrackExpanded) {
                val action = RouteListingFragmentDirections.actionRouteListingFragmentToPassengerRouteFragment(
                    track = track
                )
                view.findNavController().navigate(action)
            }

        })


        viewModel.responseContainer.observe(this, Observer {
            if (it != null){

                routeAdapter.result = it.tracks


            }else{
                Toast.makeText(requireContext(), "There is some error!", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.isShowProgress.observe(this, Observer {
            if (it){
                binding.progressBar.visibility = View.VISIBLE

            }
            else{
                binding.progressBar.visibility = View.GONE

            }
        })



        viewModel.getRoutePageFromAPI(args.startX, args.startY, args.endX, args.endY)

    }

    private fun setUpRecyclerView() = binding.routeRecyclerView.apply {
        routeAdapter = RouteRecyclerAdapter()
        adapter = routeAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

}