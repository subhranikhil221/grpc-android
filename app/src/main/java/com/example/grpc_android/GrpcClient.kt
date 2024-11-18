package com.example.grpc_android

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.AbstractStub
import apicontract.ApiRequest
import apicontract.ApiResponse
import apicontract.GatewayServiceGrpc
import apicontract.Action

class GrpcClient {

    private var channel: ManagedChannel? = null
    private var stub: GatewayServiceGrpc.GatewayServiceBlockingStub? = null

    init {
        // Initialize the channel to your mock server's address
        channel = ManagedChannelBuilder.forAddress("10.0.2.2", 50051)  // 10.0.2.2 is used for localhost in Android Emulator
            .usePlaintext()  // Disable SSL/TLS for local testing
            .build()
        stub = GatewayServiceGrpc.newBlockingStub(channel)
    }

    fun getData(query: String): String? {
        try {
            val request = ApiRequest.newBuilder()
                .setHeader(
                    ApiRequest.MsgHeader.newBuilder()
                        .setDeviceId("device123")
                        .setMessageId("msg001")
                        .setMsgTimestamp("2024-11-16T10:00:00Z")
                        .setAction(Action.SESSION_POST)
                        .build()
                )
                .setPayload(
                    ApiRequest.MsgPayload.newBuilder()
                        .setData(query)
                        .build()
                )
                .build()

            val response: ApiResponse = stub?.processRequest(request) ?: return null

            return response.payload?.success?.payload?.data ?: "No data received"
        } catch (e: Exception) {
            e.printStackTrace()
            return "Error occurred: ${e.message}"
        }
    }

    fun shutdown() {
        channel?.shutdown()
    }
}