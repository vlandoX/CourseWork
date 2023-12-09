package com.example.coursework.screens.driver.carRegistration

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.coursework.LoadingAlert
import com.example.coursework.MainActivity
import com.example.coursework.R
import com.example.coursework.databinding.FragmentCarRegistrationBinding
import com.example.coursework.retrofit.Car
import com.example.coursework.screens.signIn.SignInViewModel
import com.example.coursework.screens.signIn.SignInViewModelFactory
import com.example.coursework.static.Static

class CarRegFragment : Fragment() {

    private var _binding: FragmentCarRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CarRegViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()



        viewModel.responseContainer.observe(this, Observer {
            if (it != null) {

                Static.user = it

                Toast.makeText(activity, "Автомобиль успешно зарегистрирован", Toast.LENGTH_SHORT).show()

                view.findNavController().navigate(R.id.action_carRegistrationFragment_to_creatingRouteFragment)


            } else {
                Toast.makeText(activity, "Ошибка! Пользователь не найден", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        binding.regCarButton.setOnClickListener {
            viewModel.signUpCar(
                Car(
                    model = binding.modelEditText.text.toString().trim(),
                    color = binding.colorEditText.text.toString().trim(),
                    regNumber = binding.regNumberEditText.text.toString().trim()
                )
            )
        }
    }
    private fun initViewModel() {
        val viewModelFactory = CarRegViewModelFactory(LoadingAlert(activity as MainActivity))
        viewModel = ViewModelProvider(this, viewModelFactory).get(CarRegViewModel::class.java)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}