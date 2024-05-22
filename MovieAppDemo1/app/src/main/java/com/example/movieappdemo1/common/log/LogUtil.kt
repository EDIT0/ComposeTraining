package com.example.movieappdemo1.common.log

import android.util.Log
import com.example.movieappdemo1.BuildConfig

object LogUtil {

    enum class VersionType {
        LOG_RELEASE, LOG_DEBUG
    }

    enum class LogType {
        Debug, Verbose, Info, Warn, Error
    }

    private val className = this.javaClass.simpleName

    fun d(message: String) {
        writeLog(versionType = VersionType.LOG_RELEASE, logType = LogType.Debug, message)
    }

    fun e(message: String) {
        writeLog(versionType = VersionType.LOG_RELEASE, logType = LogType.Error, message)
    }

    fun i(message: String) {
        writeLog(versionType = VersionType.LOG_RELEASE, logType = LogType.Info, message)
    }

    fun w(message: String) {
        writeLog(versionType = VersionType.LOG_RELEASE, logType = LogType.Warn, message)
    }

    fun v(message: String) {
        writeLog(versionType = VersionType.LOG_RELEASE, logType = LogType.Verbose, message)
    }

    fun d_dev(message: String) {
        writeLog(versionType = VersionType.LOG_DEBUG, logType = LogType.Debug, message)
    }

    fun e_dev(message: String) {
        writeLog(versionType = VersionType.LOG_DEBUG, logType = LogType.Error, message)
    }

    fun i_dev(message: String) {
        writeLog(versionType = VersionType.LOG_DEBUG, logType = LogType.Info, message)
    }

    fun w_dev(message: String) {
        writeLog(versionType = VersionType.LOG_DEBUG, logType = LogType.Warn, message)
    }

    fun v_dev(message: String) {
        writeLog(versionType = VersionType.LOG_DEBUG, logType = LogType.Verbose, message)
    }

    private fun writeLog(versionType: VersionType, logType: LogType, message: String) {
//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.getDefault())
//        val time = sdf.format(Date())

        val logMessage = "[${logType.name}] : $message"
        if(versionType == VersionType.LOG_RELEASE && !BuildConfig.DEBUG) {
            when(logType) {
                LogType.Debug -> {
                    Log.d(className, logMessage)
                }
                LogType.Error -> {
                    Log.e(className, logMessage)
                }
                LogType.Info -> {
                    Log.i(className, logMessage)
                }
                LogType.Warn -> {
                    Log.w(className, logMessage)
                }
                LogType.Verbose -> {
                    Log.v(className, logMessage)
                }
                else -> {

                }
            }
        } else if(versionType == VersionType.LOG_DEBUG && BuildConfig.DEBUG) {
            when(logType) {
                LogType.Debug -> {
                    Log.d(className, logMessage)
                }
                LogType.Error -> {
                    Log.e(className, logMessage)
                }
                LogType.Info -> {
                    Log.i(className, logMessage)
                }
                LogType.Warn -> {
                    Log.w(className, logMessage)
                }
                LogType.Verbose -> {
                    Log.v(className, logMessage)
                }
                else -> {

                }
            }
        }
    }

}