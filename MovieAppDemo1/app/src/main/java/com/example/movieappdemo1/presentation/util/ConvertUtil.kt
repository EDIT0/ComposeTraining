package com.example.movieappdemo1.presentation.util

import java.net.URLDecoder
import java.net.URLEncoder

object ConvertUtil {

    fun urlDecode(value: String): String {
        return URLDecoder.decode(value, "UTF-8").replace("%20", " ")
    }

    fun urlEncode(value: String): String {
        return URLEncoder.encode(value, "UTF-8").replace("+", "%20")
    }

}