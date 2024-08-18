package com.my.composebottomsheetdemo1

import android.content.Context
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import java.lang.Exception

enum class KakaoMapScreenName {
    ThirdMap,
}

object KakaoMapUtil {

    private var hashMap: HashMap<String, MapView> = HashMap<String, MapView>()
    private var kakaoMapHashMap: HashMap<String, KakaoMap> = HashMap()
    private var kakaoMapViewInfoHashMap: HashMap<String, MapViewInfo> = HashMap()

    fun setKakaoMap(
        context: Context,
        screenName: String,
        getKakaoMap: (KakaoMap) -> Unit,
        getMapViewInfo: (MapViewInfo) -> Unit
    ) : MapView? {
        var tmpScreenName: String? = null
        var tmpMapView: MapView? = null

        hashMap.forEach { (key, value) ->
            if(key == screenName) {
                tmpScreenName = key
                tmpMapView = value
            }
        }

        if(tmpScreenName == null && tmpMapView == null) {
            hashMap.put(screenName, MapView(context))
            hashMap[screenName]?.let {
                it.start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            LogUtil.d_dev("onMapDestroy")
                        }

                        override fun onMapError(p0: Exception?) {
                            LogUtil.e_dev("onMapError ${p0}")
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(p0: KakaoMap) {
                            LogUtil.d_dev("onMapReady ${p0}")
                            kakaoMapHashMap[screenName] = p0
                            getKakaoMap.invoke(kakaoMapHashMap[screenName]!!)
                            kakaoMapViewInfoHashMap[screenName] = p0.mapViewInfo!!
                            getMapViewInfo.invoke(kakaoMapViewInfoHashMap[screenName]!!)
                        }
                    }
                )
            }

            return hashMap[screenName]
        } else {
            kakaoMapHashMap[tmpScreenName]?.let {
                getKakaoMap.invoke(it)
            }
            return hashMap[tmpScreenName]
        }
    }

//    fun moveCameraCenter(
//
//    )

    fun onKakaoMapResume(screenName: String) {
        hashMap[screenName]?.resume()
        LogUtil.d_dev("${hashMap.size}")
    }

    fun onKakaoMapPause(screenName: String) {
        hashMap[screenName]?.pause()
    }

    fun onKakaoMapFinish(screenName: String) {
        hashMap[screenName]?.finish()
        kakaoMapHashMap.remove(screenName)
        kakaoMapViewInfoHashMap.remove(screenName)
        hashMap.remove(screenName)
    }

}