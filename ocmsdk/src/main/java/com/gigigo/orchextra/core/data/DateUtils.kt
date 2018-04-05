package com.gigigo.orchextra.core.data

import java.util.Calendar

fun getTodayPlusDays(daysAgo: Int): Long {
  val calendar = Calendar.getInstance()
  calendar.add(Calendar.DATE, daysAgo)
  return calendar.time.time
}

fun getToday(): Long {
  val calendar = java.util.Calendar.getInstance()
  return calendar.time.time
}