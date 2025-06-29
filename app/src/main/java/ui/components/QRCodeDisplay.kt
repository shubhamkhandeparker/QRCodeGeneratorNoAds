package com.example.qrgeneratornoads.ui.components

import androidx.compose.animation.core.animateDecay
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrgeneratornoads.ui.theme.QRBackground
import com.example.qrgeneratornoads.ui.theme.QRCodeBlack
import com.example.qrgeneratornoads.ui.theme.QRCodeWhite
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import com.google.zxing.client.android.AmbientLightManager


@Composable
fun QRCodeDisplay(
    qrCodeBitmap: ImageBitmap?=null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(280.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(QRBackground),
        contentAlignment = Alignment.Center
    ) {

if (qrCodeBitmap!=null ){
    //Show real QR code
    Image(bitmap = qrCodeBitmap,
        contentDescription = "Generated QR Code",
        modifier=Modifier.size(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White))
}else{
    //show placeholder
    Box(
        modifier= Modifier.size(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        Text(
            text="Enter text to generate\nQR Code",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

    }
}

//This Creates the QR pattern I see in the image

private fun drawQRPattern(drawScope: DrawScope) {
    with(drawScope) {
        val cellSize = size.width / 25f  //25x25 grid

        //Draw horizontal lines

        for (i in 0..24) {
            val y = i * cellSize
            val isBlackLine = i % 2 == 0
            if (isBlackLine) {
                drawRect(
                    color = QRCodeBlack,
                    topLeft = androidx.compose.ui.geometry.Offset(0f, y),
                    size = androidx.compose.ui.geometry.Size(size.width, cellSize)
                )
            }
        }

        //Draw the corner Square (finder Pattern )

        drawFinderPattern(0f, 0f, cellSize * 7)
        drawFinderPattern(size.width - cellSize * 7, 0f, cellSize * 7)
        drawFinderPattern(0f, size.height - cellSize * 7, cellSize * 7)

    }
}

private fun DrawScope.drawFinderPattern(x: Float, y: Float, size: Float) {
    //Outer box Square

    drawRect(
        color = QRCodeBlack,
        topLeft = androidx.compose.ui.geometry.Offset(x, y),
        size = androidx.compose.ui.geometry.Size(size, size)
    )

    //Inner White Square

    val innerPadding = size * 0.14f
    drawRect(
        color = QRCodeWhite,
        topLeft = androidx.compose.ui.geometry.Offset(x + innerPadding, y + innerPadding),
        size = androidx.compose.ui.geometry.Size(size - 2 * innerPadding, size - 2 * innerPadding)
    )

    //Center black square

    val centerPadding = size * 0.28f
    drawRect(
        color = QRCodeBlack,
        topLeft = androidx.compose.ui.geometry.Offset(x + centerPadding, y + centerPadding),
        size = androidx.compose.ui.geometry.Size(size - 2 * centerPadding, size - 2 * centerPadding)
    )
}

@Preview
@Composable
fun QRCodeDisplayPreview(){
    MaterialTheme{
        QRCodeDisplay()
    }
}
