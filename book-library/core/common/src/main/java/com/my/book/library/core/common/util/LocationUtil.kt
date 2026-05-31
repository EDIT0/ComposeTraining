package com.my.book.library.core.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationUtil(
    private val context: Context,
    private val permissionUtil: PermissionUtil
) {

    /**
     * 위치 권한(정밀 또는 대략적) 허용 여부를 반환합니다.
     */
    fun isLocationPermissionGranted(): Boolean = permissionUtil.isLocationPermissionGranted()

    /**
     * 마지막으로 캐시된 위치를 즉시 반환합니다.
     * GPS → Network 순으로 시도하며, 권한이 없거나 캐시가 없으면 null을 반환합니다.
     * 앱 시작 시 초기 위치를 빠르게 가져올 때 사용합니다.
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(): Location? {
        if (!permissionUtil.isLocationPermissionGranted()) return null
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * 위치가 변경될 때마다 [Location]을 emit하는 Flow를 반환합니다.
     * GPS와 Network 프로바이더를 모두 등록하며, 최소 1초 간격으로 업데이트됩니다.
     * 권한이 없으면 즉시 종료되고, Flow 수집이 취소되면 위치 업데이트 콜백이 자동으로 해제됩니다.
     */
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Flow<Location> = callbackFlow {
        if (!permissionUtil.isLocationPermissionGranted()) {
            close()
            return@callbackFlow
        }
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val listener = LocationListener { location -> trySend(location) }
        listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER).forEach { provider ->
            try {
                if (locationManager.isProviderEnabled(provider)) {
                    locationManager.requestLocationUpdates(provider, 1000L, 0f, listener)
                }
            } catch (_: Exception) {}
        }
        awaitClose { locationManager.removeUpdates(listener) }
    }

}
