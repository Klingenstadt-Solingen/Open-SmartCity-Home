package de.osca.fama.digitaltwin.model.sensor

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class SensorType(
    val objectId: String,
    val name: String,
    val definition: String,
    val type: SensorTypeCategory = SensorTypeCategory.NONE,
    val unit: String,
    val icon: String? = null,
    val order: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val graphDivider: List<GraphDivider> = emptyList(),
)
