package com.base.utill

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


inline fun  Fragment.requestPermissions(
    permissionList: List<String>,
    crossinline onSuccessAll: (List<String>) -> Unit,
    crossinline onPermissionDenied: (permissionType: String, index: Int) -> Unit,
) {
    var counter = 0
    permissionList.forEach { permissionType ->
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permissionType
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            ++counter
        }
    }

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Check if all permissions are granted
        if (permissions.all { it.value }) {
            // All permissions granted, do something
            // For example, launch the camera to take a picture
            onSuccessAll(permissionList)
        } else {
            permissionList.forEachIndexed { index, permissionType ->
                if (!shouldShowRequestPermissionRationale(permissionType)) {
                    onPermissionDenied(permissionType, index)
                    return@registerForActivityResult
                }
            }
        }
    }


    if (counter == permissionList.size) {
        onSuccessAll(permissionList)
        return
    } else {
        Toast.makeText(requireContext(), "Permission", Toast.LENGTH_SHORT).show()
        requestPermissionLauncher.launch(permissionList.toTypedArray())
    }


}