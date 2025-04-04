package com.example.h_bank.data.network

import com.example.h_bank.data.dto.payment.OperationShortDto
import com.example.h_bank.data.dto.payment.OperationTypeDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.datetime.Instant
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class OperationWebSocketClient(
    private val client: OkHttpClient,
) : OperationWebSocketApi {
    private var webSocket: WebSocket? = null
    private val _operationsFlow = MutableSharedFlow<OperationShortDto>(replay = 0, extraBufferCapacity = 10)

    override fun connect(accountId: String): Flow<OperationShortDto> {
        val wsUrl = "ws://83.222.27.120:8080/ws/operations?accountid=$accountId"

        val request = Request.Builder().url(wsUrl).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                parseOperationDtoString(text)?.let { operation ->
                    val emitted = _operationsFlow.tryEmit(operation)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            }
        })

        return _operationsFlow.asSharedFlow()
    }

    override fun disconnect() {
        webSocket?.close(1000, "Disconnected by client")
        webSocket = null
    }

    private fun parseOperationDtoString(text: String): OperationShortDto? {
        return try {
            val regex = """OperationDTO\(id=([^,]+),\s*senderAccountId=[^,]+,\s*senderAccountNumber=[^,]+,\s*recipientAccountId=[^,]+,\s*recipientAccountNumber=[^,]+,\s*directionToMe=([^,]+),\s*amount=([^,]+),\s*transactionDateTime=([^,]+),\s*message=[^,]+,\s*operationType=([^)]+)\)""".toRegex()
            regex.matchEntire(text)?.let { match ->
                val (id, directionToMe, amount, transactionDateTime, operationType) = match.destructured
                OperationShortDto(
                    id = id.trim(),
                    amount = amount.trim().toFloat(),
                    directionToMe = directionToMe.trim().toBoolean(),
                    transactionDateTime = Instant.parse(transactionDateTime.trim()),
                    operationType = OperationTypeDto.valueOf(operationType.trim().uppercase())
                )
            } ?: run {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}