package com.example.h_bank.data.dto.payment

import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZoneId

@Serializable
data class OperationShortDto(
    val id: String,
    val amount: Float,
    val directionToMe: Boolean,
    @Serializable(with = KotlinxInstantSerializer::class)
    val transactionDateTime: Instant,
    val operationType: OperationTypeDto
)

internal fun OperationShortDto.toDomain(): OperationShortModel =
    OperationShortModel(
        id = id,
        amount = amount,
        directionToMe = directionToMe,
        transactionDateTime = transactionDateTime.toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime(),
        operationType = operationType
    )

object KotlinxInstantSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }
}