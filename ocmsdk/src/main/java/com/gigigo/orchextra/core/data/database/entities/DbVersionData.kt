package com.gigigo.orchextra.core.data.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "version")
class DbVersionData(@PrimaryKey var id: String, var version: String) {
  constructor() : this(VERSION_KEY, "")

  companion object {
    @JvmField
    val VERSION_KEY = "VERSION_KEY"
  }
}