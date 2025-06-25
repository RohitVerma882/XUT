package com.xut.domain.model

import androidx.annotation.StringRes

data class Region(
    @StringRes val labelId: Int,
    val host: String
)

