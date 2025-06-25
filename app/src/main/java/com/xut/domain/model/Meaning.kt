package com.xut.domain.model

import androidx.annotation.StringRes

data class Meaning(
    val code: Int,
    @StringRes val messageId: Int
)

