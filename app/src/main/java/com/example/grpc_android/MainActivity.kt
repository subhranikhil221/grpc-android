package com.example.grpc_android

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import apicontract.ApiRequest
import apicontract.ApiResponse
import apicontract.GatewayServiceGrpc
import apicontract.Action


class MainActivity : AppCompatActivity() {

    private val grpcClient = GrpcClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        button.setOnClickListener {
            // Run the gRPC call on a background thread
            Thread {
                // Make a gRPC call to get data
                val result = grpcClient.getData("Sample Query")

                // Update the UI with the result (runOnUiThread to update the main thread)
                runOnUiThread {
                    resultTextView.text = result ?: "Error occurred"
                }
            }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        grpcClient.shutdown()  // Ensure the client shuts down when the activity is destroyed
    }
}