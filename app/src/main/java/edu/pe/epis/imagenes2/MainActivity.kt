package edu.pe.epis.imagenes2

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import edu.pe.epis.imagenes2.ui.theme.Imagenes2Theme
import kotlinx.coroutines.launch
import android.Manifest
import android.content.Context
import androidx.core.content.FileProvider
import java.io.File

// ⚠️ ADVERTENCIA: Reemplaza con tu clave API REAL de Gemini.
// Para producción, usa un método de almacenamiento seguro.
private const val GEMINI_API_KEY = "AIzaSyDN8H-ko-tn11PeqlYmnOUAaLAmiTWoHZE"

class MainActivity : ComponentActivity() {

    // URI temporal para almacenar la imagen tomada por la cámara
    private lateinit var currentPhotoUri: Uri

    // State para almacenar la URI de la imagen seleccionada (galería o cámara)
    private var selectedImageUri by mutableStateOf<Uri?>(null)

    // Contrato para seleccionar una imagen de la galería
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImageUri = uri // Establecer URI de la galería
        }
    }

    // Contrato para tomar una foto con la cámara
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Si la foto se tomó con éxito, establece la URI de la foto como seleccionada
            selectedImageUri = currentPhotoUri
        }
    }

    // Contrato para solicitar el permiso de la cámara
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, lanzar la cámara
            launchCamera()
        } else {
            // Permiso denegado, podrías mostrar un mensaje al usuario
            selectedImageUri = null
        }
    }

    // Función para crear un archivo temporal y obtener una URI para la cámara
    private fun createImageUri(context: Context): Uri {
        val imagesDir = File(context.cacheDir, "images")
        imagesDir.mkdirs()
        val file = File(imagesDir, "temp_photo.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Debe coincidir con el <provider> en AndroidManifest.xml
            file
        )
    }

    // Lanza la aplicación de la cámara
    private fun launchCamera() {
        currentPhotoUri = createImageUri(this)
        takePicture.launch(currentPhotoUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Imagenes2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VisionScreen(
                        modifier = Modifier.padding(innerPadding),
                        // Acción para lanzar el selector de galería
                        onPickImage = {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        // Acción para tomar una foto
                        onTakePicture = {
                            // Primero, solicitar permiso. Si ya está concedido, lanza la cámara
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                        // Pasa la URI del estado
                        imageUri = selectedImageUri
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------

@Composable
fun VisionScreen(
    modifier: Modifier = Modifier,
    onPickImage: () -> Unit,
    onTakePicture: () -> Unit, // Nueva acción para tomar foto
    imageUri: Uri?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Estados para la respuesta, el estado de carga y el error
    var resultText by remember { mutableStateOf("Selecciona una imagen y presiona 'Analizar'") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    // Inicialización del modelo de Gemini
    val model = remember {
        try {
            // El modelo gemini-2.5-flash soporta tanto texto como imágenes (multimodal)
            GenerativeModel(
                modelName = "gemini-2.5-flash",
                apiKey = GEMINI_API_KEY
            )
        } catch (e: Exception) {
            errorMessage = "Error de inicialización: Asegúrate que la API Key es válida."
            null
        }
    }

    // Función auxiliar para convertir Uri a Bitmap
    fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            errorMessage = "No se pudo cargar la imagen: ${e.message}"
            null
        }
    }

    // Función principal para la llamada a la API
    fun analyzeImage() {
        errorMessage = null // Limpiar errores anteriores

        if (model == null) {
            errorMessage = "Error: El modelo de Gemini no está disponible."
            return
        }

        val bitmap = imageUri?.let { uriToBitmap(it) }

        if (bitmap == null) {
            resultText = "Por favor, selecciona una imagen primero."
            return
        }

        isLoading = true
        resultText = "Analizando imagen, por favor espera..."

        coroutineScope.launch {
            try {
                // 1. Construir el contenido multimodal: imagen + prompt
                val imagePart = content { image(bitmap) }
                val prompt = "Describe en detalle qué ves en esta imagen. Identifica objetos, personas, y el contexto de la escena. Sé conciso y utiliza el español."

                // 2. Generar el contenido
                val response = model.generateContent(imagePart, content { text(prompt) })

                // 3. Mostrar el resultado
                resultText = response.text ?: "No se pudo obtener una descripción del modelo."
            } catch (e: Exception) {
                errorMessage = "Error en la llamada a la API: ${e.message}. Verifica tu conexión y clave API."
                resultText = "Análisis fallido."
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Título y Descripción ---
        Text(
            text = "Análisis Visual de Imagenes",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Selecciona una foto de tu galería o toma una nueva para que la IA la describa.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- Botón para Tomar Foto (NUEVO) ---
        Button(
            onClick = onTakePicture,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Tomar Foto con Cámara")
        }

        // --- Botón para Seleccionar Imagen (Existente) ---
        Button(
            onClick = onPickImage,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Elegir Imagen de Galería")
        }


        Spacer(modifier = Modifier.height(16.dp))

        // --- Vista de la Imagen Seleccionada ---
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Imagen seleccionada para análisis",
                modifier = Modifier
                    .size(250.dp)
                    .padding(8.dp)
                    .fillMaxWidth(),
            )

            // --- Botón para Analizar Imagen ---
            Button(
                onClick = ::analyzeImage,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                if (isLoading) {
                    // Indicador de carga
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analizando...")
                } else {
                    Text("Analizar Imagen")
                }
            }
        } else {
            Text("Esperando selección de imagen...", style = MaterialTheme.typography.titleSmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Sección de Resultados ---
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Resultado del Análisis:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar errores si existen
        if (errorMessage != null) {
            Text(
                text = "ERROR: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Mostrar el resultado de la IA
        Text(
            text = resultText,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}