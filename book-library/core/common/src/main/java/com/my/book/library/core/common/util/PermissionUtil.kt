package com.my.book.library.core.common.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionUtil(private val context: Context) {

    fun isGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun isLocationPermissionGranted(): Boolean =
        isGranted(Manifest.permission.ACCESS_FINE_LOCATION) ||
        isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)

}
