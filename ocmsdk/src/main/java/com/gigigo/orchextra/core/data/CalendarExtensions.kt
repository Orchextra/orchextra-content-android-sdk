package com.gigigo.orchextra.core.data

import java.util.Calendar

fun Calendar.getTodayPlusDays(daysAgo: Int) : Long {
  add(Calendar.DATE, daysAgo)
  return time.time
}