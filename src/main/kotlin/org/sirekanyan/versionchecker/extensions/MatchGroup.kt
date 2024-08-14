package org.sirekanyan.versionchecker.extensions

val MatchGroup?.intValue
    get() = this?.value?.toInt() ?: 0
