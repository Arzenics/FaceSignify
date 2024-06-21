package com.dicoding.capstone.ui.tabLayout.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.capstone.databinding.FragmentProfileBinding
import com.dicoding.capstone.ui.MainActivity
import com.dicoding.capstone.util.ViewModelFactory
import com.dicoding.capstone.viewModel.AuthViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.data.local.UserPreference

class ProfileFragment : Fragment() {
    private lateinit var userPreference: UserPreference

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory = ViewModelFactory(requireContext())
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        setupLogoutButton()
        userPreference = UserPreference(requireContext())
//        setActionBarTitleWithUsername()

        return root
    }

//    private fun setActionBarTitleWithUsername() {
//        val username = userPreference.getUserName()
//        if (username != null) {
//            (activity as? AppCompatActivity)?.supportActionBar?.title = "$username"
//        } else {
//            (activity as? AppCompatActivity)?.supportActionBar?.title = "Profile"
//        }
//    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

