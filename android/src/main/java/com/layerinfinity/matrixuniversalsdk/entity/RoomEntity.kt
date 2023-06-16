package com.layerinfinity.matrixuniversalsdk.entity

import com.squareup.moshi.JsonClass

// TODO: Use later
@JsonClass(generateAdapter = true)
data class RoomEntity(
  val id: String,
  val name: String
)
