package com.xut.util

import androidx.annotation.GuardedBy

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthManager private constructor() {
    private val lock = Any()

    @GuardedBy("lock")
    private var _userId: String = ""

    @GuardedBy("lock")
    private var _passToken: String = ""

    @GuardedBy("lock")
    private var _deviceId: String = ""

    @GuardedBy("lock")
    private val listeners: MutableSet<Listener> = hashSetOf()

    var userId: String
        get() {
            synchronized(lock) {
                return _userId
            }
        }
        set(value) {
            synchronized(lock) {
                _userId = value
            }
            notifyLoginState()
        }

    var passToken: String
        get() {
            synchronized(lock) {
                return _passToken
            }
        }
        set(value) {
            synchronized(lock) {
                _passToken = value
            }
            notifyLoginState()
        }

    @OptIn(ExperimentalUuidApi::class)
    var deviceId: String
        get() {
            synchronized(lock) {
                if (_deviceId.isEmpty()) {
                    _deviceId = "wb_${Uuid.random()}"
                }
                return _deviceId
            }
        }
        set(value) {
            synchronized(lock) {
                _deviceId = value
            }
        }

    val isLoggedIn: Boolean
        get() {
            synchronized(lock) {
                return _userId.isNotEmpty() && _passToken.isNotEmpty()
            }
        }

    fun logOut() {
        synchronized(lock) {
            _userId = ""
            _passToken = ""
            _deviceId = ""
        }
        notifyLoginState()
    }

    private fun notifyLoginState() {
        synchronized(lock) {
            listeners.forEach {
                it.onLoginStateChanged()
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
        fun onLoginStateChanged()
    }

    companion object {
        const val USER_ID_KEY = "userId"
        const val PASS_TOKEN_KEY = "passToken"
        const val DEVICE_ID_KEY = "deviceId"

        @Volatile
        private var instance: AuthManager? = null

        @Synchronized
        fun getInstance(): AuthManager {
            return instance ?: synchronized(this) {
                instance ?: AuthManager().also { instance = it }
            }
        }
    }
}