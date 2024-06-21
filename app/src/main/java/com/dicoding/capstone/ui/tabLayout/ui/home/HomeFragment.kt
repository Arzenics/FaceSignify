package com.dicoding.capstone.ui.tabLayout.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstone.adapter.ClassAdapter
import com.dicoding.capstone.databinding.FragmentHomeBinding
import com.dicoding.capstone.ui.detail.CameraActivity
import com.dicoding.capstone.ui.detail.ResultActivity
import com.dicoding.capstone.util.ViewModelFactory
import com.dicoding.capstone.viewModel.ClassViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

    private lateinit var viewModel: ClassViewModel
    private lateinit var adapter: ClassAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Menggunakan requireContext() untuk mendapatkan aplikasi konteks
        val applicationContext = requireContext().applicationContext
        viewModel = ViewModelProvider(this, ViewModelFactory(applicationContext)).get(ClassViewModel::class.java)

//        binding.rvClass.layoutManager = LinearLayoutManager(requireContext())

        binding.cardView1.setOnClickListener {showAttendanceDialog() }
        binding.cardView2.setOnClickListener {showAttendanceDialog() }

//        viewModel.classes.observe(viewLifecycleOwner, { classes ->
//            adapter = ClassAdapter(classes)
//            binding.rvClass.adapter = adapter
//        })

        viewModel.fetchClasses()
//
        return root
    }
//        override fun onClassItemClick(item: UserClassResponse) {
//        // Tampilkan dialog presensi saat item diklik
//        showAttendanceDialog()
//    }

    private fun showAttendanceDialog() {
        val options = arrayOf("Gallery", "Camera")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Silahkan Lakukan Presensi")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> startGallery()
                    1 -> startCameraX()
                }
            }
            .show()
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(context, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data
            selectedImg?.let {
                val resultIntent = Intent(context, ResultActivity::class.java).apply {
                    putExtra("photoUri", it.toString())
                }
                startActivity(resultIntent)
            } ?: showToast("Failed to get image URI")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {
                currentImageUri = it
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}