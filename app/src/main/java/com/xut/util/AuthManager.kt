package com.xut.util

import android.content.Context
import android.content.SharedPreferences

import androidx.annotation.GuardedBy
import androidx.core.content.edit

class AuthManager private constructor(private val sharedPref: SharedPreferences) {
    private val lock = Any()

    @GuardedBy("lock")
    private val listeners: MutableSet<Listener> = hashSetOf()

    val userId: String?
        get() {
            synchronized(lock) {
                return sharedPref.getString(USER_ID, null)
            }
        }

    val passToken: String?
        get() {
            synchronized(lock) {
                return sharedPref.getString(PASS_TOKEN, null)
            }
        }

    val deviceId: String?
        get() {
            synchronized(lock) {
                return sharedPref.getString(DEVICE_ID, null)
            }
        }

    val isLoggedIn: Boolean
        get() {
            return userId != null && passToken != null && deviceId != null
        }

    fun setUserId(value: String) {
        synchronized(lock) {
            sharedPref.edit {
                putString(USER_ID, value)
            }
        }
        notifyLoginStateChanged(isLoggedIn)
    }

    fun setPassToken(value: String) {
        synchronized(lock) {
            sharedPref.edit {
                putString(PASS_TOKEN, value)
            }
        }
        notifyLoginStateChanged(isLoggedIn)
    }

    fun setDeviceId(value: String) {
        synchronized(lock) {
            sharedPref.edit {
                putString(DEVICE_ID, value)
            }
        }
        notifyLoginStateChanged(isLoggedIn)
    }

    fun logOut() {
        synchronized(lock) {
            sharedPref.edit {
                remove(USER_ID)
                remove(PASS_TOKEN)
                remove(DEVICE_ID)
            }
        }
        notifyLoginStateChanged(false)
    }

    private fun notifyLoginStateChanged(isLoggedIn: Boolean) {
        synchronized(lock) {
            listeners.forEach {
                it.onLoginStateChanged(isLoggedIn)
            }
        }
    }

    fun registerListener(listener: Listener) {
        synchronized(lock) { listeners.add(listener) }
    }

    fun unregisterListener(listener: Listener) {
        synchronized(lock) { listeners.remove(listener) }
    }

    interface Listener {
        fun onLoginStateChanged(isLoggedIn: Boolean)
    }

    companion object {
        private const val PREFS_NAME = ".AUTH"
        const val USER_ID = "userId"
        const val PASS_TOKEN = "passToken"
        const val DEVICE_ID = "deviceId"

        @Volatile
        private var instance: AuthManager? = null

        @Synchronized
        fun getInstance(context: Context): AuthManager {
            return instance ?: synchronized(this) {
                instance ?: run {
                    val sharedPref = context.applicationContext.getSharedPreferences(
                        context.packageName + PREFS_NAME,
                        Context.MODE_PRIVATE
                    )
                    AuthManager(sharedPref).also { instance = it }
                }
            }
        }
    }
}