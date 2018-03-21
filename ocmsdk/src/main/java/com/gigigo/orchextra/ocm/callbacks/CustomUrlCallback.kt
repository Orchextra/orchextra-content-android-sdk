package com.gigigo.orchextra.ocm.callbacks

interface CustomUrlCallback {

  fun actionNeedsValues(parameters: List<String>): Map<String, String>
}