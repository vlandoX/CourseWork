package com.example.coursework.screens.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.coursework.LoadingAlert
import com.example.coursework.MainActivity
import com.example.coursework.R
import com.example.coursework.databinding.FragmentSignUpBinding
import com.example.coursework.retrofit.SignUpRequest
import com.example.coursework.screens.signIn.SignInViewModel
import com.example.coursework.screens.signIn.SignInViewModelFactory


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        nameFocusListener()
        phoneFocusListener()
        passwordFocusListener()
        repeatPasswordFocusListener()
        chooseRoleListener()
        validAgeCheck()

        viewModel.responseContainer.observe(this, Observer {
            if (it != null) {

                //navigate to the creating route screen for Passenger
                when (it.user.role) {
                    "ROLE_PASSENGER" -> view.findNavController()
                        .navigate(R.id.action_signUpFragment_to_routeCreationFragment)

                    //navigate to the registration car screen for Driver
                    "ROLE_DRIVER" -> view.findNavController()
                        .navigate(R.id.action_signUpFragment_to_carRegistrationFragment)
                }

            } else {
                Toast.makeText(activity, "Ошибка! Данный номер телефона или пароль уже используется!", Toast.LENGTH_LONG).show()
            }
        })

        binding.submitButton.setOnClickListener {
            submitForm()
        }


        binding.signInRedirectText.setOnClickListener {
            view.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            Toast.makeText(activity, "Авторизация", Toast.LENGTH_SHORT).show()
        }

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
    }

    fun initViewModel() {
        val viewModelFactory = SignUpViewModelFactory(LoadingAlert(activity as MainActivity))
        viewModel = ViewModelProvider(this, viewModelFactory).get(SignUpViewModel::class.java)
    }

    private fun submitForm() {
        with(binding) {

            nameContainer.helperText = viewModel.validName(nameEditText.text.toString())
            phoneContainer.helperText = viewModel.validPhone(phoneEditText.text.toString())
            passwordContainer.helperText = viewModel.validPassword(passwordEditText.text.toString())
            repeatPasswordContainer.helperText = viewModel.validRepeatPassword(
                repeatPasswordEditText.text.toString(),
                passwordEditText.text.toString()
            )

            if (nameContainer.helperText == null && phoneContainer.helperText == null
                && passwordContainer.helperText == null && repeatPasswordContainer.helperText == null
            ) {

                viewModel.createUser(
                    SignUpRequest(
                        username = binding.nameEditText.text.toString().trim(),
                        phoneNumber = binding.phoneEditText.text.toString().trim(),
                        password = binding.passwordEditText.text.toString().trim(),
                        role = roleForRequestFormat()
                    )
                )
            }
        }
    }


    private fun validAgeCheck() {
        binding.checkBox.setOnCheckedChangeListener { _, isChecked -> checkConditions() }
    }

    private fun nameFocusListener() {
        binding.nameEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.nameContainer.helperText =
                    viewModel.validName(binding.nameEditText.text.toString())
                checkConditions()
            }
        }
    }

    private fun phoneFocusListener() {
        binding.phoneEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.phoneContainer.helperText =
                    viewModel.validPhone(binding.phoneEditText.text.toString())
                checkConditions()
            }
        }
    }

    private fun passwordFocusListener() {
        binding.passwordEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordContainer.helperText =
                    viewModel.validPassword(binding.passwordEditText.text.toString())
                checkConditions()
            }
        }
    }

    private fun repeatPasswordFocusListener() {
        with(binding) {
            repeatPasswordEditText.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    repeatPasswordContainer.helperText = viewModel.validRepeatPassword(
                        repeatPasswordEditText.text.toString(),
                        passwordEditText.text.toString()
                    )
                    checkConditions()
                }
            }
        }
    }

    private fun initChooseRoleMenu() {
        val items = listOf("Пассажир", "Водитель")
        val adapter = activity?.let { ArrayAdapter(it, R.layout.roles_list_item, items) }
        binding.dropdownField.setAdapter(adapter)
    }

    private fun chooseRoleListener() {
        initChooseRoleMenu()
        binding.dropdownField.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val itemSelected = adapterView.getItemAtPosition(position) as String
                if (itemSelected == null) {
                    binding.chooseContainer.helperText = "Необходимо выбрать роль"
                } else {
                    binding.chooseContainer.helperText = null
                }
                checkConditions()
            }

    }

    fun roleForRequestFormat(): String {
        val role = binding.dropdownField.text.toString()
        return if (role == "Пассажир") "ROLE_PASSENGER"
        else "ROLE_DRIVER"

    }

    private fun checkConditions() {
        with(binding) {

            val nameError = viewModel.validName(nameEditText.text.toString())
            val phoneError = viewModel.validPhone(phoneEditText.text.toString())
            val passwordError = viewModel.validPassword(passwordEditText.text.toString())
            val repeatPasswordError = viewModel.validRepeatPassword(
                repeatPasswordEditText.text.toString(),
                passwordEditText.text.toString()
            )
            val chooseRoleError = chooseContainer.helperText

            submitButton.isEnabled =
                nameError == null && phoneError == null && passwordError == null
                        && repeatPasswordError == null && chooseRoleError == null && binding.checkBox.isChecked
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
