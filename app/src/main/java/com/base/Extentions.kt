package com.base

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.base.sampleapp.R
import com.google.android.material.snackbar.Snackbar


fun Fragment.showSnackBarWithAction(
    message: String,
    actionMessage: String,
    @ColorRes textColor: Int,
    @DrawableRes drawableRes: Int,
    onAction: () -> Unit
) {
    try {
        view?.let {
            val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            snackbar.setTextColor(ContextCompat.getColor(requireContext(), textColor))
            snackbar.setAction(actionMessage) {
                onAction()
            }.setActionTextColor(ContextCompat.getColor(requireContext(), textColor))
            snackbar.view.background =
                ContextCompat.getDrawable(requireContext(), drawableRes)
            snackbar.show()
        }
    } catch (e: Exception) {
    }


}


fun Fragment.openAppSettings() {
    try {
        val intent = Intent()
        val packageName = requireActivity().packageName
        val uri: Uri = Uri.fromParts("package", packageName, this.toString())
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).data = uri
        startActivity(intent)
    } catch (e: Exception) {
        Log.e("APP::Exception", "$e : ${e.message}")
    }

}