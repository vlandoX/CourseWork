package com.example.coursework.screens.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.coursework.LoadingAlert
import com.example.coursework.MainActivity
import com.example.coursework.R
import com.example.coursework.databinding.FragmentSignInBinding
import com.example.coursework.misc.UserPrefs
import com.example.coursework.retrofit.AuthRequest
import com.example.coursework.static.Static


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignInViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        binding.loginButton.setOnClickListener {
            validation()
        }
        binding.signUpRedirectText.setOnClickListener {
            view.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            Toast.makeText(activity, "Регистрация", Toast.LENGTH_SHORT).show()
        }

        viewModel.responseContainer.observe(this, Observer {
            if (it != null) {

                Static.user = it.user
                Static.token = it.token

                val prefs = UserPrefs(requireContext())
                prefs.saveUser(it.user)
                prefs.saveToken(it.token)

                when (it.user.role) {
                    "ROLE_PASSENGER" -> view.findNavController()
                        .navigate(R.id.action_signInFragment_to_routeCreationFragment)

                    //navigate to the registration car screen for Driver
                    "ROLE_DRIVER" ->{
                        if(it.user.car!= null) view.findNavController().navigate(R.id.action_signInFragment_to_creatingRouteFragment)
                        else view.findNavController().navigate(R.id.action_signInFragment_to_carRegistrationFragment)
                    }
                }

            } else {
                Toast.makeText(activity, "Ошибка! Пользователь не найден", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })


    }

    private fun initViewModel() {
        val viewModelFactory = SignInViewModelFactory(LoadingAlert(activity as MainActivity))
        viewModel = ViewModelProvider(this, viewModelFactory).get(SignInViewModel::class.java)
    }


    private fun validation() {
        binding.phoneContainer.helperText = validateUserPhone()
        binding.passwordContainer.helperText = validatePassword()

        if (binding.phoneContainer.helperText == null
            && binding.passwordContainer.helperText == null
        ) {

            viewModel.authUser(
                AuthRequest(
                    phoneNumber = binding.phoneEditText.text.toString(),
                    password = binding.passwordEditText.text.toString()
                )
            )
        } else {
            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
        }
    }

    private fun validateUserPhone(): String? {
        val phoneNumber = binding.phoneEditText.text.toString()
        if (phoneNumber.isNullOrBlank()) {
            return "Номер не должен быть пустым"
            false
        } else if (!phoneNumber.matches(".*[0-9].*".toRegex())) {
            return "Номер телефона должен содержать толко цифры"
        } else {
            return null
        }
    }

    private fun validatePassword(): String? {
        val password = binding.passwordEditText.text.toString()
        return if (password.isNullOrBlank()) {
            "Пароль не должен быть пустым"
        } else {
            return null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}