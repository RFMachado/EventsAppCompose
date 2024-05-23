package com.example.eventos

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eventos.api.EventsItem
import com.example.eventos.api.asyncGetHttpRequest
import com.example.eventos.ui.theme.EventosTheme
import com.example.eventos.ui.theme.Green50


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Green50
                ) {
                    GetEventsItems()
                }
            }
        }
    }
}

@Composable
fun GetEventsItems(modifier: Modifier = Modifier) {
    val eventList = remember { mutableStateOf<List<EventsItem>>(listOf()) }
    val isLoading = remember { mutableStateOf(true) }

    asyncGetHttpRequest(
        endpoint = "https://events.wiremockapi.cloud/api/events",
        onSuccess = {
            isLoading.value = false
            eventList.value = it.response.eventos
        },
        onError = {
            isLoading.value = false
            Log.d("ERROR", it.message.toString())
        }
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(eventList.value) { item ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column {
                    if (item.image.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.image)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                            /*.clip(RoundedCornerShape(1.dp, 1.dp, 0.dp, 0.dp))*/
                        )
                    }

                    Text(
                        text = item.title,
                        modifier = modifier.padding(8.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        Text(
                            text = "Data: ${item.date.getDateTime()}",
                            modifier = modifier.padding(8.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Valor: ${item.price}",
                            modifier = modifier.padding(8.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = item.description,
                        modifier = modifier.padding(8.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

    if (isLoading.value) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            CircularProgressIndicator()
        }
    }
}