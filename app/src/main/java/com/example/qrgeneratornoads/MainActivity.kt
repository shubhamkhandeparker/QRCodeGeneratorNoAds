package com.example.qrgeneratornoads

import com.example.qrgeneratornoads.ui.components.QRScannerScreen
import com.example.qrgeneratornoads.datas.QRHistoryItem
import com.example.qrgeneratornoads.ui.components.HistoryScreen

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.qrgeneratornoads.ui.theme.QRGeneratorNoAdsTheme
import com.example.qrgeneratornoads.ui.components.QRCodeDisplay
import com.example.qrgeneratornoads.ui.components.TypeSelectionRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.TextField
import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


private fun shareQRCode(context: Context, bitmap: ImageBitmap) {
    try {
        //convert ImageBitmap to Android Bitmap
        val androidBitmap = bitmap.asAndroidBitmap()

        //save to cache directory

        val file = File(context.cacheDir, "qr_Code_${System.currentTimeMillis()}.png")
        val outputStream = FileOutputStream(file)
        androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        //Create Share Intent

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, "check out this OR Code!")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
    } catch (e: Exception) {
        //Handle error gracefully
        Log.e("QRShare", "Error Sharing QR Code", e)
    }
}

private fun generateORCode(text: String): ImageBitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                )

            }
        }
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var selectedType by remember { mutableStateOf("Text") }
            var inputText by remember { mutableStateOf("") }
            var qrCodeBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
            var showScanner by remember { mutableStateOf(false) }
            var showHistory by remember { mutableStateOf(false) }
            var historyItems by remember { mutableStateOf<List<QRHistoryItem>>(emptyList()) }

            LaunchedEffect(inputText) {
                if (inputText.isNotBlank()) {
                    qrCodeBitmap = generateORCode(inputText)
                    //save to history
                    val newItem= QRHistoryItem(
                        text=inputText,
                        type = selectedType
                    )
                    historyItems=historyItems+newItem
                } else {
                    qrCodeBitmap = null //Clear Qr code when text is empty
                }
            }

            QRGeneratorNoAdsTheme {
                if (showScanner) {
                    QRScannerScreen(
                        onQRCodeDetected = { scannerText ->
                            inputText = scannerText
                            showScanner = false
                        },
                        onBackPressed = {
                            showScanner = false
                        }
                    )
                }
                else if(showHistory){
                    HistoryScreen(
                        historyItems=historyItems,
                        onItemSelected = {selectedType->
                            inputText=selectedType
                            showHistory=false
                        },
                        onBackPressed={
                            showHistory=false
                        }
                    )
                }

                else {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center

                        ) {
                            QRCodeDisplay(qrCodeBitmap = qrCodeBitmap) //Top
                            Spacer(modifier = Modifier.height(24.dp))
                            TypeSelectionRow(
                                selectedType = selectedType,
                                onTypeSelected = { newType ->
                                    selectedType = newType
                                }
                            )  //Bottom

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                label = {
                                    Text(
                                        when (selectedType) {
                                            "Text" -> "Enter Your Text"
                                            "URL" -> "Enter Your URL"
                                            "Email" -> "Enter Email Address"
                                            "Phone" -> "Enter Phone Number"
                                            "Location" -> "Enter Location"
                                            else -> "Enter Content"
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )

                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            //Bottom navigation Row

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = { showScanner = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Text("Scan")
                                }

//                                Button(
//                                    onClick = {/*TODO :Setting*/ },
//                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
//                                ) { Text("Setting") }


                                Button(
                                    onClick = {showHistory=true },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Text("History")
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            //Generate QR Code

                            Button(
                                onClick = {
                                    qrCodeBitmap?.let { bitmap ->
                                        shareQRCode(this@MainActivity, bitmap)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                enabled = qrCodeBitmap != null //only enables when QR code exists
                            ) { Text("Share QR Code") }

                        }
                    }
                }
            }
        }
    }
}

