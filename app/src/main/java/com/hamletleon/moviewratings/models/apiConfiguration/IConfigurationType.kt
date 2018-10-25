package com.hamletleon.moviewratings.models.apiConfiguration

import androidx.annotation.IntDef

@IntDef(0, 1, 2, 3, 4, 5)
annotation class IConfigurationType

enum class ConfigurationType(val id: Int) {
    NONE(0),
    IMAGE_SIZE_BACKDROP(1),
    IMAGE_SIZE_LOGO(2),
    IMAGE_SIZE_POSTER(3),
    IMAGE_SIZE_PROFILE(4),
    IMAGE_SIZE_STILL(5)
}