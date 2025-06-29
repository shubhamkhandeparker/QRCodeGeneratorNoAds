package com.example.qrgeneratornoads.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.qrgeneratornoads.datas.QRHistoryItem
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    historyItems: List<QRHistoryItem>,
    onItemSelected: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        //Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onBackPressed) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "QR History",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        //History List
        if (historyItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No QR Code Generated Yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historyItems) { item ->
                    HistoryItemCard(
                        item = item,
                        onItemClick = { onItemSelected(item.text) }

                    )

                }
            }
        }
    }
}

@Composable
private fun HistoryItemCard(
    item:QRHistoryItem,
    onItemClick:()-> Unit
){
    Card(
        modifier=Modifier.fillMaxWidth().padding(vertical = 4.dp),
        onClick=onItemClick
    )
    {
        Column(
            modifier=Modifier.padding(16.dp)
        ){
            Text(
                text=item.type,
                style = MaterialTheme.typography.labelMedium,
                color= MaterialTheme.colorScheme.primary
            )
            Text(
                text=item.text,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2
            )
            Text(
                text= SimpleDateFormat("MMM dd,yyy HH:mm",Locale.getDefault())
                    .format(Date(item.timeStamp)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant

            )
        }
    }
}
