package com.example.map_picker

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)