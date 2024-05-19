package com.example.eventos

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventos.api.EventsItem
import com.example.eventos.api.asyncGetHttpRequest
import com.example.eventos.ui.theme.EventosTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
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
    Column {
        Button(onClick = {
            asyncGetHttpRequest(
                endpoint = "https://events.wiremockapi.cloud/api/events",
                onSuccess = {
                    eventList.value = it.response.eventos
                },
                onError = {
                    Log.d("ERROR", it.message.toString())
                }
            )
        }) {
            Text(
                text = "Buscar eventos",
                modifier = modifier
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(eventList.value) { item ->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    Column {
                        Text(
                            text = "Titulo : ${item.title}",
                            modifier = modifier
                        )
                        Text(
                            text = "Descrição: ${item.description}",
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}