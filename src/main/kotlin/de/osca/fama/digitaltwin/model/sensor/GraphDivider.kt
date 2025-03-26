package de.osca.fama.digitaltwin.model.sensor

import kotlinx.serialization.Serializable

@Serializable
data class GraphDivider(
    val name: String,
    val color: String,
    val value: Double,
)
