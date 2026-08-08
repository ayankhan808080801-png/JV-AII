package com.jvai.personal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class Message(val text: String, val fromUser: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { JVAIApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JVAIApp() {
    var input by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                Message("Hey Ayan 👋 I’m JV AI. Ask me anything.", false)
            )
        )
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("JV AI")
                            Text("Personal AI Assistant", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Message JV AI...") },
                        maxLines = 4
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val text = input.trim()
                            if (text.isNotEmpty()) {
                                messages = messages + Message(text, true)
                                input = ""
                                messages = messages + Message(
                                    "I received: "$text"\n\nAI backend abhi connect karna baaki hai. Ye starter app UI ready hai.",
                                    false
                                )
                                scope.launch {
                                    listState.animateScrollToItem(messages.lastIndex)
                                }
                            }
                        }
                    ) { Text("Send") }
                }
            }
        ) { padding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { message ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (message.fromUser)
                            Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.large,
                            tonalElevation = 2.dp
                        ) {
                            Text(
                                text = message.text,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
