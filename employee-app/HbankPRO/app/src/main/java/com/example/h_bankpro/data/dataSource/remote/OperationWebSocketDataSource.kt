package com.example.h_bankpro.data.dataSource.remote

import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.network.OperationWebSocketApi
import com.example.h_bankpro.data.utils.OperationParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class OperationWebSocketDataSource(
    private val client: OkHttpClient,
    private val baseWsUrl: String = "ws://83.222.27.120:8080/ws/operations"
) : OperationWebSocketApi {
    private var webSocket: WebSocket? = null
    private val _operationsFlow = MutableSharedFlow<OperationShortDto>(
        replay = 0,
        extraBufferCapacity = 10
    )

    override fun connect(accountId: String): Flow<OperationShortDto> {
        val wsUrl = "$baseWsUrl?accountid=$accountId"
        val request = Request.Builder().url(wsUrl).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                OperationParser.parseOperationDtoString(text)?.let { operation ->
                    _operationsFlow.tryEmit(operation)
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
}