package com.xut.utils

import androidx.annotation.GuardedBy

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
        get() = synchronized(lock) { _userId }
        set(value) {
            synchronized(lock) {
                _userId = value
            }
            notifyLoginChange()
        }

    var passToken: String
        get() = synchronized(lock) { _passToken }
        set(value) {
            synchronized(lock) {
                _passToken = value
            }
            notifyLoginChange()
        }

    var deviceId: String
        get() = synchronized(lock) { _deviceId }
        set(value) {
            synchronized(lock) {
                _deviceId = value
            }
            notifyLoginChange()
        }

    val isLoggedIn: Boolean
        get() = synchronized(lock) {
            _userId.isNotEmpty() && _passToken.isNotEmpty() && _deviceId.isNotEmpty()
        }

    fun logOut() {
        synchronized(lock) {
            _userId = ""
            _passToken = ""
            _deviceId = ""
        }
        notifyLoginChange()
    }

    private fun notifyLoginChange() {
        synchronized(lock) { listeners.toList() }
            .forEach { it.onLoginChange() }
    }

    fun registerListener(listener: Listener) = synchronized(lock) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) = synchronized(lock) {
        listeners.remove(listener)
    }

    interface Listener {
        fun onLoginChange()
    }

    companion object {
        @Volatile
        private var instance: AuthManager? = null

        fun getInstance(): AuthManager {
            return instance ?: synchronized(this) {
                instance ?: AuthManager().also { instance = it }
            }
        }
    }
}