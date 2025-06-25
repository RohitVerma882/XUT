package com.xut

import android.content.Context

import com.xut.data.AuthRepositoryImpl
import com.xut.domain.repository.AuthRepository
import com.xut.util.AuthManager

interface AppContainer {
    val authManager: AuthManager
    val authRepository: AuthRepository
}

class AppContainerImpl(private val context: Context) : AppContainer {
    override val authManager: AuthManager get() = AuthManager.getInstance(context)
    override val authRepository: AuthRepository get() = AuthRepositoryImpl()
}