package com.example.coursework.screens.starter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.coursework.R
import com.example.coursework.misc.UserPrefs
import com.example.coursework.static.Static
import java.util.Stack

class StarterFragment : Fragment() {

    companion object {
        fun newInstance() = StarterFragment()
    }

    private lateinit var viewModel: StarterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_starter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(StarterViewModel::class.java)
        val prefs = UserPrefs(requireContext())



        viewModel.responseContainer.observe(this, Observer { user->
            if( user != null ) {
                Static.user = user
                prefs.saveUser(user)
                when(user.role) {
                    "ROLE_DRIVER"->{
                        when(user.state) {
                            "FREE"->{
                                view.findNavController().navigate(R.id.action_starterFragment_to_creatingRouteFragment)
                            }
                            else ->{
                                view.findNavController().navigate(R.id.action_starterFragment_to_routeFragment)
                            }
                        }

                    }
                    "ROLE_PASSENGER" ->{
                        when(user.state) {
                            "FREE"->{
                                view.findNavController().navigate(R.id.action_starterFragment_to_routeCreationFragment)
                            }
                            else ->{
                                view.findNavController().navigate(R.id.action_starterFragment_to_passengerRouteFragment)
                            }
                        }
                    }
                }

            }
        })

        viewModel.errorMessage.observe(this) {
            if (it == "Error : Not Found") {
                prefs.clearToken()
                prefs.clearUser()
                Static.token = null
                Static.user = null
                view.findNavController().navigate(R.id.action_starterFragment_to_signInFragment)
            }
        }

        val token = prefs.getToken()
        val user = prefs.getUser()
        if(token == null) return view.findNavController().navigate(R.id.action_starterFragment_to_signInFragment)
        Static.token = token

        if (user != null) {
            viewModel.getUser(user.id)
        } else {
            return view.findNavController().navigate(R.id.action_starterFragment_to_signInFragment)
        }

    }

}