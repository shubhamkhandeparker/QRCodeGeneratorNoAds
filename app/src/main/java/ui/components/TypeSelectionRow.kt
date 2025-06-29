package com.example.qrgeneratornoads.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


@Composable
private fun SelectableButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline
        )
    ) {
        Text(text)
    }
}

@Composable
fun TypeSelectionRow(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        //First Row : Text , URL, Email
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp))
        {
            SelectableButton(
                text = "Text",
                isSelected = selectedType == "Text",
                onClick = { onTypeSelected("Text") }
            )

           SelectableButton(
               text = "URL",
               isSelected = selectedType=="URL",
               onClick = {onTypeSelected("URL")}
           )

           SelectableButton(
               text = "Email",
               isSelected = selectedType=="Email",
               onClick = {onTypeSelected("Email")}
           )

        }
        //Second Row: Phone,Location
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {


            SelectableButton(
                text = "Phone",
                isSelected = selectedType=="Phone",
                onClick = {onTypeSelected("Phone")}
            )

            SelectableButton(
                text = "Location",
                isSelected = selectedType=="Location",
                onClick = {onTypeSelected("Location")}
            )


        }

    }
}


@Preview
@Composable

fun TypeSelectionRowPreview() {
    MaterialTheme {
        TypeSelectionRow(
            selectedType = "Text",
            onTypeSelected = { }  //Empty function for preview
        )
    }
}

