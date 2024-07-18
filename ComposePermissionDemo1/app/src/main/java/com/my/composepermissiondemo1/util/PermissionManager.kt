package com.my.composepermissiondemo1.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat

object PermissionManager {

    private val permissionDeniedList = ArrayList<String>()
    private val permissionRationaleList = ArrayList<String>()

    /**
     * 권한
     * */
    val permissionOfCamera = Manifest.permission.CAMERA
    val permissionOfNotifications = Manifest.permission.POST_NOTIFICATIONS
    val permissionOfFindLocation = Manifest.permission.ACCESS_FINE_LOCATION
    val permissionOfCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
    val permissionOfMediaImages = Manifest.permission.READ_MEDIA_IMAGES
    val permissionOfMediaVideo = Manifest.permission.READ_MEDIA_VIDEO
//    private val permissionOfMediaAudio = Manifest.permission.READ_MEDIA_AUDIO
    val permissionOfExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE
//    private val permissionOfBackgroundLocation = Manifest.permission.ACCESS_BACKGROUND_LOCATION
    val permissionOfPhoneState = Manifest.permission.READ_PHONE_STATE
    val permissionOfPhoneNumbers = Manifest.permission.READ_PHONE_NUMBERS
    val permissionOfCallPhone = Manifest.permission.CALL_PHONE
    val permissionOfWriteExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE

    val permissionMap: Map<String, String> = mapOf(
        permissionOfCamera to "카메라",
        permissionOfNotifications to "알림",
        permissionOfFindLocation to "위치",
        permissionOfCoarseLocation to "위치",
        permissionOfMediaImages to "사진",
        permissionOfMediaVideo to "동영상",
//        permissionOfMediaAudio to "오디오",
        permissionOfExternalStorage to "파일",
//        permissionOfBackgroundLocation to "위치(항상허용)",
        permissionOfPhoneState to "전화",
        permissionOfPhoneNumbers to "전화",
        permissionOfCallPhone to "전화",
        permissionOfWriteExternalStorage to "파일쓰기"
    )

    /**
     * 권한 체크 (하나라도 누락되어있으면 false 반환)
     * */
    fun checkAllPermissions(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT

        if(version >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(context, permissionOfCamera) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfNotifications) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfFindLocation) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfCoarseLocation) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfMediaImages) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfMediaVideo) != PackageManager.PERMISSION_GRANTED) { return false }
//            if(ContextCompat.checkSelfPermission(activity, permissionOfMediaAudio) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfPhoneState) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfPhoneNumbers) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfCallPhone) != PackageManager.PERMISSION_GRANTED) { return false }

            // Q 이상
//            if(ContextCompat.checkSelfPermission(this.context.applicationContext, permissionOfBackgroundLocation) != PackageManager.PERMISSION_GRANTED) { return false }
        } else if(version >= Build.VERSION_CODES.Q) {
            if(ContextCompat.checkSelfPermission(context, permissionOfCamera) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfFindLocation) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfCoarseLocation) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfExternalStorage) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfPhoneState) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfPhoneNumbers) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfCallPhone) != PackageManager.PERMISSION_GRANTED) { return false }

            // Q 이상
//            if(ContextCompat.checkSelfPermission(this.context.applicationContext, permissionOfBackgroundLocation) != PackageManager.PERMISSION_GRANTED) { return false }
            // Tiramisu 이하
            if(ContextCompat.checkSelfPermission(context, permissionOfWriteExternalStorage) != PackageManager.PERMISSION_GRANTED) { return false }
        } else if(version >= Build.VERSION_CODES.O) {
            if(ContextCompat.checkSelfPermission(context, permissionOfCamera) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfFindLocation) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfCoarseLocation) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfExternalStorage) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfPhoneState) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfPhoneNumbers) != PackageManager.PERMISSION_GRANTED) { return false }
            if(ContextCompat.checkSelfPermission(context, permissionOfCallPhone) != PackageManager.PERMISSION_GRANTED) { return false }

            // Tiramisu 이하
            if(ContextCompat.checkSelfPermission(context, permissionOfWriteExternalStorage) != PackageManager.PERMISSION_GRANTED) { return false }
        } else {

        }

        return true
    }

    fun checkPermission(context: Context, permissionName: String) : Boolean {
        return if(ContextCompat.checkSelfPermission(context, permissionName) != PackageManager.PERMISSION_GRANTED) {
            false
        } else {
            true
        }
    }

    /**
     * 권한 가져오기
     * */
    fun getAllPermissions() : Array<String> {
        val version = Build.VERSION.SDK_INT

        if(version >= Build.VERSION_CODES.TIRAMISU) {
            return arrayOf(
                permissionOfCamera,
                permissionOfNotifications,
                permissionOfFindLocation,
                permissionOfCoarseLocation,
                permissionOfMediaImages,
                permissionOfMediaVideo,
//                permissionOfMediaAudio,
                permissionOfPhoneState,
                permissionOfPhoneNumbers,
                permissionOfCallPhone
            )
        } else if(version >= Build.VERSION_CODES.Q) {
            return arrayOf(
                permissionOfCamera,
                permissionOfFindLocation,
                permissionOfCoarseLocation,
                permissionOfExternalStorage,
                permissionOfPhoneState,
                permissionOfPhoneNumbers,
                permissionOfCallPhone,
                permissionOfWriteExternalStorage
            )
        } else if(version >= Build.VERSION_CODES.O) {
            return arrayOf(
                permissionOfCamera,
                permissionOfFindLocation,
                permissionOfCoarseLocation,
                permissionOfExternalStorage,
                permissionOfPhoneState,
                permissionOfPhoneNumbers,
                permissionOfCallPhone,
                permissionOfWriteExternalStorage
            )
        } else {
            return emptyArray()
        }
    }

    /**
     * 설정된 모든 권한 요청
     * */
    fun requestPermission(
        requestAllPermissionsLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
        permissionArray: Array<String>
    ) {
        requestAllPermissionsLauncher.launch(permissionArray)
    }

    /**
     * 요청에 대한 응답 값 로직
     * */
    fun requestAllPermissionsLauncherLogic(context: Context, permissions: Map<String, Boolean>) {
        permissionDeniedList.clear()
        permissionRationaleList.clear()

        for(p in permissions.entries) {
            if(!p.value) {
                permissionDeniedList.add(p.key) // 거부된 권한 추가
                // 권한 재요청이 가능 여부
//                    if(ActivityCompat.shouldShowRequestPermissionRationale(context, p.key)) {
//                        permissionRationaleList.add(p.key)
//                    }
            }
        }

        if(permissionDeniedList.size > 0) {
            if(permissionRationaleList.size > 0) {
                // 권한 재요청 가능
                showDeniedPermissionAlert(permissionRationaleList)
            } else {
                // 권한 재요청 불가능하므로 설정으로 이동
                showMoveToPermissionSettingAlert(permissionDeniedList)
            }
        } else {
            // 요청한 모든 권한 허용되었음.
            // 선행 권한 허용이 필요한 권한이 있을 경우, 여기에서 요청
            // 위치 권한 허용되었으면 진행 (Q버전 이상)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // ACEESS_FINE_LOCATION, ACCESS_COARSE_LOCATION 권한이 허용되었는지 확인
                if(ContextCompat.checkSelfPermission(context, permissionOfFindLocation) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, permissionOfCoarseLocation) == PackageManager.PERMISSION_GRANTED) {
                    // ACCESS_BACKGROUND_LOCATION 권한이 허용되었는지 확인
//                    if(ContextCompat.checkSelfPermission(this.context.applicationContext, permissionOfBackgroundLocation) == PackageManager.PERMISSION_GRANTED) {
                    resultCallback?.result(true)
//                    } else {
//                        showRequestAdditionalPermissionAlert(arrayOf(permissionOfBackgroundLocation))
//                    }
                }
            }
        }
    }

    /**
     * 거부된 권한 알림창
     * */
    private fun showDeniedPermissionAlert(deniedList: ArrayList<String>) {
        LogUtil.d_dev("거부된 권한 알림창 showDeniedPermissionAlert()")
    }

    /**
     * 권한 설정으로 이동 알림창
     * */
    private fun showMoveToPermissionSettingAlert(deniedList: ArrayList<String>) {
        LogUtil.d_dev("권한 설정으로 이동 알림창 showMoveToPermissionSettingAlert() ${deniedList}")
        var str = StringBuilder()
        str.append("다음 권한을 허용해주세요.\n\n")
        for (i in 0 until deniedList.size) {
            try {
                val permissionNickname = permissionMap.getValue(deniedList.get(i))
                if (!str.contains("${permissionNickname}", ignoreCase = true)) {
                    str.append("[${permissionNickname}]\n")
                }
            } catch (e: Exception) {
                continue
            }
        }
        str.append("\n권한 허용 후 사용해주세요.")

        resultCallback?.dialog(str.toString(), "설정으로 이동")
    }

    /**
     * 권한 설정화면으로 이동
     * */
    fun moveToPermissionSettingPage(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.packageName))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * UI 업데이트를 위한 콜백
     * */
    interface ResultCallback {
        fun result(enable: Boolean)
        fun dialog(message: String, buttonText: String)
    }

    private var resultCallback: ResultCallback? = null

    fun setPermissionResultCallback(resultCallback: ResultCallback) {
        PermissionManager.resultCallback = resultCallback
    }
}