package org.sirekanyan.versionchecker.extensions

import kotlin.reflect.full.memberProperties

fun Any.callMemberProperty(key: String): Any? =
    this::class.memberProperties.find { it.name == key }?.call(this)
