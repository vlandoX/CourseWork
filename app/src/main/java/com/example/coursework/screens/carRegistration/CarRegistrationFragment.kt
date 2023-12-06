package com.example.coursework.screens.carRegistration

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coursework.databinding.FragmentCarRegistrationBinding

class CarRegistrationFragment : Fragment() {

    private var _binding: FragmentCarRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CarRegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(CarRegistrationViewModel::class.java)

        return view
    }



}