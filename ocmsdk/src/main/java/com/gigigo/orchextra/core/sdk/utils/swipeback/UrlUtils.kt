package com.gigigo.orchextra.core.sdk.utils.swipeback

val regexParam = Regex("#[0-9a-zA-Z-_]*#")

fun String.getUrlParameters(): List<String> = with(this) {
  return regexParam.findAll(this).toList().map { it.value }
}