package com.premium.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.premium.app.models.Message
import com.premium.app.viewmodels.AIViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIScreen(aiViewModel: AIViewModel = viewModel()) {
    val messages by aiViewModel.messages.collectAsState()
    val isTyping by aiViewModel.isTyping.collectAsState()
    var messageInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chat con IA", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            MessageInput(messageInput, onMessageChange = { messageInput = it }) {
                if (messageInput.isNotBlank()) {
                    aiViewModel.sendMessage(messageInput)
                    messageInput = ""
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                reverseLayout = true // Para que los mensajes nuevos aparezcan abajo
            ) {
                if (isTyping) {
                    item {
                        AIMessageBubble(message = Message(
                            id = "typing",
                            senderId = "AI",
                            text = "Escribiendo...",
                            isUser = false
                        ))
                    }
                }
                items(messages.reversed()) { message ->
                    MessageBubble(message = message)
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val bubbleColor = if (message.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (message.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (message.isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun AIMessageBubble(message: Message) {
    // Reutiliza MessageBubble para la IA
    MessageBubble(message = message)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(
    messageInput: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageInput,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Escribe un mensaje...") },
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        FloatingActionButton(
            onClick = onSendMessage,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Send, contentDescription = "Enviar mensaje")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAIScreen() {
    // AIScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewMessageBubbleUser() {
    MessageBubble(message = Message("1", "User", "Hola IA, ¿cómo estás?", isUser = true))
}

@Preview(showBackground = true)
@Composable
fun PreviewMessageBubbleAI() {
    MessageBubble(message = Message("2", "AI", "Estoy bien, gracias por preguntar. ¿En qué puedo ayudarte?", isUser = false))
}
