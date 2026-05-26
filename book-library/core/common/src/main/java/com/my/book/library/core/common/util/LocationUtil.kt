package com.my.book.library.core.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager

class LocationUtil(
    private val context: Context,
    private val permissionUtil: PermissionUtil
) {

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(): Location? {
        if (!permissionUtil.isLocationPermissionGranted()) return null
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

}
