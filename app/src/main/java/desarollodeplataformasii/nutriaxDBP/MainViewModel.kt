// MainViewModel.kt
package desarollodeplataformasii.nutriaxDBP

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import desarollodeplataformasii.nutriaxDBP.database.AppDatabase
import desarollodeplataformasii.nutriaxDBP.database.MealDao
import desarollodeplataformasii.nutriaxDBP.database.MealEntity
import desarollodeplataformasii.nutriaxDBP.model.GeminiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

class MainViewModel(private val mealDao: MealDao) : ViewModel() {
    private val _userGoals = MutableStateFlow(UserGoals())
    val userGoals = _userGoals.asStateFlow()

    fun updateGoals(newGoals: UserGoals) {
        _userGoals.value = newGoals
    }

    fun insertMeal(meal: desarollodeplataformasii.nutriaxDBP.database.MealEntity) {
        viewModelScope.launch {
            mealDao.insertMeal(meal)
        }
    }

    fun deleteMeal(meal: desarollodeplataformasii.nutriaxDBP.database.MealEntity) {
        viewModelScope.launch {
            mealDao.deleteMeal(meal)
        }
    }

    fun getMealsForDay(start: Long, end: Long): Flow<List<desarollodeplataformasii.nutriaxDBP.database.MealEntity>> {
        return mealDao.getMealsForDay(start, end)
    }
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val db = AppDatabase.getDatabase(context)
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db.mealDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun createImageUri(context: Context): Uri {
    val tempDir = File(context.cacheDir, "images")
    tempDir.mkdirs()
    val file = File(tempDir, "temp_image.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        }
    } catch (e: Exception) {
        Log.e("VisionScreen", "Error loading bitmap from URI: ${e.message}")
        null
    }
}

@Composable
fun VisionScreen(speak: (String) -> Unit, viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var resultText by remember { mutableStateOf("Analiza Nutricional con Gemini") }
    var isLoading by remember { mutableStateOf(false) }
    var currentAnalysisResult by remember { mutableStateOf<GeminiResponse?>(null) }
    val cameraUri = remember { mutableStateOf<Uri?>(null) }

    fun handleNewImageUri(uri: Uri?) {
        if (uri != null) {
            imageBitmap = uriToBitmap(context, uri)
            resultText = "Imagen seleccionada. Presiona Analizar Comida."
            currentAnalysisResult = null
        }
    }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        handleNewImageUri(uri)
    }

    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            handleNewImageUri(cameraUri.value)
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraUri.value = createImageUri(context)
            cameraUri.value?.let { takePicture.launch(it) }
        } else {
            resultText = "Permiso de cámara denegado."
        }
    }

    fun analyzeImage() = coroutineScope.launch {
        val bitmap = imageBitmap
        if (bitmap == null) {
            resultText = "Por favor, toma o selecciona una foto primero."
            return@launch
        }
        isLoading = true
        resultText = "Analizando... Esto puede tardar unos segundos."
        currentAnalysisResult = null
        try {
            val model = GenerativeModel(
                modelName = "gemini-2.5-flash",
                apiKey = Constants.GEMINI_API_KEY
            )
            val prompt = """
                Actúa como un nutricionista experto. Analiza esta imagen de comida.
                1. Identifica el nombre del plato o alimento principal.
                2. Estima el peso aproximado en gramos.
                3. Calcula los macronutrientes aproximados para esa porción (Calorías, Proteínas, Carbohidratos, Grasas).
                4. Proporciona un consejo breve y saludable sobre este alimento.
                Responde EXCLUSIVAMENTE con un objeto JSON válido, sin bloques de código markdown (sin ```json), ni texto adicional.
                Usa este formato exacto:
                {
                    "alimento_identificado": "Nombre del plato",
                    "estimacion_gramos": 250.0,
                    "macros_estimados": {
                        "calorias_kcal": 300,
                        "proteinas_g": 20.5,
                        "carbohidratos_g": 30.0,
                        "grasas_g": 10.0
                    },
                    "consejo_nutricional": "Un consejo breve aquí."
                }
                Si no es comida o no se puede identificar, responde con "alimento_identificado": "Desconocido".
            """.trimIndent()
            val inputContent = content {
                image(bitmap)
                text(prompt)
            }
            val response = withContext(Dispatchers.IO) {
                model.generateContent(inputContent)
            }
            val jsonText = response.text?.replace("```json", "")?.replace("```", "")?.trim() ?: ""
            if (jsonText.isNotEmpty()) {
                try {
                    val jsonParser = Json { ignoreUnknownKeys = true }
                    val analysis = jsonParser.decodeFromString<GeminiResponse>(jsonText)
                    currentAnalysisResult = analysis
                    resultText = "Plato: ${analysis.foodName}\nCalorías: ${analysis.macros.calories} kcal\nConsejo: ${analysis.advice}"
                    speak("Análisis completado. Es ${analysis.foodName} con aproximadamente ${analysis.macros.calories} calorías.")
                } catch (e: Exception) {
                    resultText = "Error al leer respuesta de IA: ${e.localizedMessage}\nRespuesta cruda: $jsonText"
                    speak("Error al procesar los datos de la comida.")
                }
            } else {
                resultText = "La IA no devolvió respuesta."
                speak("No pude analizar la imagen.")
            }
        } catch (e: Exception) {
            resultText = "Error de conexión o API: ${e.message}"
            Log.e("GeminiVision", "Error: ${e.message}", e)
            speak("Error de conexión.")
        } finally {
            isLoading = false
        }
    }

    fun saveMeal(mealType: String) {
        currentAnalysisResult?.let { result ->
            val meal = MealEntity(
                name = result.foodName,
                grams = result.grams,
                calories = result.macros.calories,
                protein = result.macros.protein,
                carbs = result.macros.carbs,
                fat = result.macros.fat,
                mealType = mealType,
                date = System.currentTimeMillis()
            )
            viewModel.insertMeal(meal)
            Toast.makeText(context, "Guardado como $mealType", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Text("Análisis Nutricional con Gemini", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.Companion.height(16.dp))
        imageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Imagen de comida seleccionada",
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Companion.Crop
            )
        } ?: Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(250.dp)
                .padding(8.dp)
                .background(Color.Companion.LightGray)
        ) {
            Text("No hay imagen seleccionada", Modifier.Companion.align(Alignment.Companion.Center))
        }
        Spacer(modifier = Modifier.Companion.height(16.dp))
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                enabled = !isLoading
            ) {
                Text("Seleccionar Foto")
            }
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    } else {
                        cameraUri.value = createImageUri(context)
                        cameraUri.value?.let { takePicture.launch(it) }
                    }
                },
                enabled = !isLoading
            ) {
                Text("Tomar Foto")
            }
        }
        Spacer(modifier = Modifier.Companion.height(16.dp))
        Button(
            onClick = { analyzeImage() },
            enabled = !isLoading && imageBitmap != null
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.Companion.size(24.dp),
                    color = Color.Companion.White
                )
                Spacer(modifier = Modifier.Companion.width(8.dp))
                Text("Analizando...")
            } else {
                Text("Analizar Comida con Gemini")
            }
        }
        Spacer(modifier = Modifier.Companion.height(16.dp))
        Card(
            modifier = Modifier.Companion.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.Companion.padding(16.dp)) {
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.Companion.height(16.dp))
        if (!isLoading && currentAnalysisResult != null) {
            Text(
                text = "Registrar en mi Agenda:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Companion.Bold,
                modifier = Modifier.Companion.align(Alignment.Companion.Start)
            )
            Row(
                modifier = Modifier.Companion.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { saveMeal("Desayuno") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Desayuno")
                }
                Button(
                    onClick = { saveMeal("Almuerzo") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Almuerzo")
                }
                Button(
                    onClick = { saveMeal("Cena") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Cena")
                }
            }
            Spacer(modifier = Modifier.Companion.height(16.dp))
        }
    }
}