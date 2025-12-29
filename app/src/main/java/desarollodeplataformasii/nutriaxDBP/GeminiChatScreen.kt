// GeminiChatScreen.kt
package desarollodeplataformasii.nutriaxDBP

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GeminiChatScreen(speak: (String) -> Unit) {
    var userInput by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun sendMessage() {
        if (userInput.isBlank()) return
        coroutineScope.launch {
            isLoading = true
            try {
                val model = GenerativeModel(
                    modelName = "gemini-2.5-flash",
                    apiKey = Constants.GEMINI_API_KEY
                )
                val prompt = """
                    Eres un nutricionista virtual experto. Responde a la siguiente pregunta nutricional de forma clara, concisa y en español.
                    Evita usar markdown. No incluyas listas con asteriscos. Solo texto legible.
                    Pregunta: $userInput
                """.trimIndent()
                val response = withContext(Dispatchers.IO) {
                    model.generateContent(prompt)
                }
                val text = response.text?.trim() ?: "No pude generar una respuesta."
                responseText = text
                speak("Respuesta: $text")
            } catch (e: Exception) {
                responseText = "Error: ${e.message}"
                Log.e("GeminiChat", "Error", e)
                speak("Lo siento, hubo un error al procesar tu pregunta.")
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Asistente Nutricional (Gemini)", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Pregunta algo sobre nutrición") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )
        Button(
            onClick = { sendMessage() },
            enabled = !isLoading && userInput.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            } else {
                Text("Enviar")
            }
        }
        if (responseText.isNotEmpty()) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Respuesta:", fontWeight = FontWeight.Bold)
                    Text(responseText)
                }
            }
        }
    }
}
