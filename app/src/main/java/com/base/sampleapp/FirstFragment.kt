package com.base.sampleapp

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.base.openAppSettings
import com.base.sampleapp.databinding.FragmentFirstBinding
import com.base.showSnackBarWithAction
import com.base.utill.requestPermission
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!


    private val permissions = arrayListOf<String>()

    private val readStoragePermissionResult: ActivityResultLauncher<Array<String>> by requestPermission(
        permissions,
        grantedAll = {
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            // pickImage.launch("image/*")
        },
        denied = {
            if (!shouldShowRequestPermissionRationale(it)) {
                showSnackBarWithAction(
                    message = "Please Enable App Settings",
                    actionMessage = "Open",
                    textColor = R.color.white,
                    drawableRes = R.drawable.bg_snackbar
                ) {
                    openAppSettings()
                }
                return@requestPermission
            }

        },


        )


//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        // Check if all permissions are granted
//        if (permissions.all { it.value }) {
//            // All permissions granted, do something
//            // For example, launch the camera to take a picture
//            pickImage.launch("image/*")
//        } else {
//            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES))
//                Toast.makeText(requireContext(), "Permanent Denied", Toast.LENGTH_SHORT)
//                    .show()
//            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
//                Toast.makeText(requireContext(), "Below 13 Permanent Denied", Toast.LENGTH_SHORT)
//                    .show()
//        }
//    }


    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.imageView.setImageURI(uri)
        }

//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            // Permission granted, do something
//            // For example, launch the gallery to select an image
//            pickImage.launch("image/*")
//        } else {
//            // Permission denied, show a message or take some action
//            //Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            permissions.add(Manifest.permission.CAMERA)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.CAMERA)
            permissions.add(Manifest.permission.RECORD_AUDIO)
        }

        _binding = FragmentFirstBinding.inflate(inflater, container, false)


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonFirst.setOnClickListener {
            // Toast.makeText(requireContext(), "Running", Toast.LENGTH_SHORT).show()
            readStoragePermissionResult.launch(permissions.toTypedArray())
//            requestPermissions(permissions, { list ->
//                pickImage.launch("image/*")
//            }) { permission, index ->
//                Toast.makeText(
//                    requireContext(),
//                    "Permission Denied $permission",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSnackbar(message: String) {
        // Get the root view of the fragment
        view?.let {
            val snackbar = Snackbar.make(it, message, Snackbar.ANIMATION_MODE_SLIDE)
            snackbar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            snackbar.setAction("Open") {
                // Code to execute when the action is clicked
            }.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            snackbar.view.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_snackbar)
            snackbar.show()
        }

    }
}