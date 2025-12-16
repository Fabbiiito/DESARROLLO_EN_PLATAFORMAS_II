package desarollodeplataformasii.nutriaxDBP

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import java.util.Calendar
import java.util.Locale

// =========================================================================
// CONSTANTES Y ENUMS
// =========================================================================

private const val GEMINI_API_KEY = "miclavedelapi"

// Las metas por defecto se mantienen aquí, pero ahora se pueden editar
data class UserGoals(
    val calories: Int = 2000,
    val protein: Double = 150.0,
    val carbs: Double = 250.0,
    val fat: Double = 70.0
)

enum class Screen {
    CALENDAR, VISION
}

// =========================================================================
// MODELOS DE DATOS (DEL CÓDIGO 1)
// =========================================================================

data class DateItem(
    val dayOfMonth: Int,
    val dayOfWeek: String,
    val month: Int,
    val year: Int
)

// =========================================================================
// FUNCIÓN DE AYUDA (DEL CÓDIGO 1)
// =========================================================================

private fun getDayName(day: Int): String {
    return when(day) {
        Calendar.MONDAY -> "L"
        Calendar.TUESDAY -> "M"
        Calendar.WEDNESDAY -> "M"
        Calendar.THURSDAY -> "J"
        Calendar.FRIDAY -> "V"
        Calendar.SATURDAY -> "S"
        Calendar.SUNDAY -> "D"
        else -> ""
    }
}

private fun getMonthName(month: Int): String {
    return when (month) {
        Calendar.JANUARY -> "Enero"
        Calendar.FEBRUARY -> "Feb"
        Calendar.MARCH -> "Mar"
        Calendar.APRIL -> "Abr"
        Calendar.MAY -> "May"
        Calendar.JUNE -> "Jun"
        Calendar.JULY -> "Jul"
        Calendar.AUGUST -> "Ago"
        Calendar.SEPTEMBER -> "Sep"
        Calendar.OCTOBER -> "Oct"
        Calendar.NOVEMBER -> "Nov"
        Calendar.DECEMBER -> "Dic"
        else -> ""
    }
}

private fun generateSampleDates(startCalendar: Calendar): List<DateItem> {
    val dates = mutableListOf<DateItem>()
    val calendar = startCalendar.clone() as Calendar

    // 1. Encuentra el Lunes de la semana de hoy
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    // 2. Retrocede 14 días (2 semanas completas)
    calendar.add(Calendar.DAY_OF_MONTH, -14)

    // 3. Generar 42 días (6 semanas)
    repeat(42) {
        dates.add(
            DateItem(
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                dayOfWeek = getDayName(calendar.get(Calendar.DAY_OF_WEEK)),
                month = calendar.get(Calendar.MONTH),
                year = calendar.get(Calendar.YEAR)
            )
        )
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return dates
}

private fun findTodayPosition(dates: List<DateItem>): Int {
    val today = Calendar.getInstance()
    val day = today.get(Calendar.DAY_OF_MONTH)
    val month = today.get(Calendar.MONTH)
    val year = today.get(Calendar.YEAR)

    return dates.indexOfFirst { it.dayOfMonth == day && it.month == month && it.year == year }
}

// =========================================================================
// COMPONENTES DE UI PERSONALIZADOS
// =========================================================================

@Composable
fun CircularProgressBar(
    percentage: Float,
    number: Number,
    label: String,
    color: Color,
    radius: Dp = 25.dp, // Reducido de 35.dp para que quepan 4 en fila
    strokeWidth: Dp = 5.dp, // Ligeramente más fino
    animDuration: Int = 1000
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = animDuration,
            delayMillis = 0
        ), label = "progress"
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp) // Padding reducido
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(radius * 2f)
        ) {
            Canvas(modifier = Modifier.size(radius * 2f)) {
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f * curPercentage.value,
                    useCenter = false,
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(
                text = "${(curPercentage.value * 100).toInt()}%",
                color = Color.Black,
                fontSize = 10.sp, // Fuente reducida
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) // Fuente reducida
        Text(text = "$number", fontSize = 9.sp, color = Color.Gray) // Fuente reducida
    }
}

// =========================================================================
// VISTA CALENDARIO (ADAPTADA A COMPOSE DEL CÓDIGO 1)
// =========================================================================

@Composable
fun DateItemView(dateItem: DateItem, isSelected: Boolean, onDateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .width(40.dp)
            .clickable(onClick = onDateClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dateItem.dayOfWeek,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dateItem.dayOfMonth.toString(),
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun MealCard(meal: MealEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = meal.mealType,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${meal.calories} kcal",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Borrar",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "P: ${meal.protein}g | C: ${meal.carbs}g | G: ${meal.fat}g",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AddMealDialog(
    onDismiss: () -> Unit,
    onSave: (String, Int, Double, Double, Double, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var mealType by remember { mutableStateOf("Almuerzo") } // Default

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Comida Manual") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del alimento") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Tipo de comida
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    listOf("Desayuno", "Almuerzo", "Cena").forEach { type ->
                        Button(
                            onClick = { mealType = type },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if(mealType == type) MaterialTheme.colorScheme.primary else Color.LightGray
                            ),
                            modifier = Modifier.weight(1f).padding(2.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text(type, fontSize = 10.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calorías (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("Prot (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    )
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("Carb (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    )
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { fat = it },
                        label = { Text("Gras (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val calVal = calories.toIntOrNull() ?: 0
                val protVal = protein.toDoubleOrNull() ?: 0.0
                val carbVal = carbs.toDoubleOrNull() ?: 0.0
                val fatVal = fat.toDoubleOrNull() ?: 0.0
                
                if (name.isNotEmpty()) {
                    onSave(name, calVal, protVal, carbVal, fatVal, mealType)
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// Diálogo para editar metas
@Composable
fun EditGoalsDialog(
    currentGoals: UserGoals,
    onDismiss: () -> Unit,
    onSave: (UserGoals) -> Unit
) {
    var calories by remember { mutableStateOf(currentGoals.calories.toString()) }
    var protein by remember { mutableStateOf(currentGoals.protein.toString()) }
    var carbs by remember { mutableStateOf(currentGoals.carbs.toString()) }
    var fat by remember { mutableStateOf(currentGoals.fat.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Metas Diarias") },
        text = {
            Column {
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Meta Calorías (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Meta Proteínas (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text("Meta Carbohidratos (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Meta Grasas (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val calVal = calories.toIntOrNull() ?: 2000
                val protVal = protein.toDoubleOrNull() ?: 150.0
                val carbVal = carbs.toDoubleOrNull() ?: 250.0
                val fatVal = fat.toDoubleOrNull() ?: 70.0
                
                onSave(UserGoals(calVal, protVal, carbVal, fatVal))
            }) {
                Text("Guardar Metas")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val todayCalendar = remember { Calendar.getInstance() }
    val allDates = remember { generateSampleDates(todayCalendar) }

    var selectedDateIndex by remember { mutableIntStateOf(findTodayPosition(allDates)) }
    val initialPosition = remember { findTodayPosition(allDates) }
    val listState = rememberLazyListState()
    
    // Estado para los diálogos
    var showAddDialog by remember { mutableStateOf(false) }
    var showGoalsDialog by remember { mutableStateOf(false) }

    // Obtener metas del ViewModel
    val userGoals by viewModel.userGoals.collectAsState()

    // Encabezado para mostrar la fecha seleccionada
    val selectedDateItem = if (selectedDateIndex != -1) allDates[selectedDateIndex] else allDates.first()
    val headerDateText = remember(selectedDateItem) {
        "${getMonthName(selectedDateItem.month)} ${selectedDateItem.dayOfMonth}, ${selectedDateItem.year}"
    }

    // Calcular el rango de tiempo del día seleccionado para la base de datos
    val selectedDayStart = remember(selectedDateItem) {
        val cal = Calendar.getInstance()
        cal.set(selectedDateItem.year, selectedDateItem.month, selectedDateItem.dayOfMonth, 0, 0, 0)
        cal.timeInMillis
    }
    val selectedDayEnd = remember(selectedDateItem) {
        val cal = Calendar.getInstance()
        cal.set(selectedDateItem.year, selectedDateItem.month, selectedDateItem.dayOfMonth, 23, 59, 59)
        cal.timeInMillis
    }

    // Obtener las comidas de la base de datos
    val meals by viewModel.getMealsForDay(selectedDayStart, selectedDayEnd).collectAsState(initial = emptyList())

    // Calcular totales del día
    val totalCalories = remember(meals) { meals.sumOf { it.calories } }
    val totalProtein = remember(meals) { meals.sumOf { it.protein } }
    val totalCarbs = remember(meals) { meals.sumOf { it.carbs } }
    val totalFat = remember(meals) { meals.sumOf { it.fat } }

    // Centrar la vista en la semana actual al inicio
    LaunchedEffect(Unit) {
        if (initialPosition != -1) {
            val weekStart = initialPosition - (initialPosition % 7)
            listState.scrollToItem(weekStart)
        }
    }

    // Función para abrir el DatePicker de Android
    fun openDatePicker(context: Context) {
        val anio = todayCalendar.get(Calendar.YEAR)
        val mes = todayCalendar.get(Calendar.MONTH)
        val dia = todayCalendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)

                // Actualizar la fecha seleccionada en el header
                val newDateItem = DateItem(day, getDayName(selectedCalendar.get(Calendar.DAY_OF_WEEK)), month, year)
                val newIndex = allDates.indexOfFirst { it == newDateItem }
                if (newIndex != -1) {
                    selectedDateIndex = newIndex
                    // Intentar centrar en la fecha seleccionada
                    val weekStart = newIndex - (newIndex % 7)
                    if (weekStart != -1) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(weekStart)
                        }
                    }
                }
            },
            anio, mes, dia
        )
        datePickerDialog.show()
    }

    Scaffold(
        floatingActionButton = {
            Column {
                // Botón para editar metas (encima del botón de agregar)
                FloatingActionButton(
                    onClick = { showGoalsDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Configurar Metas", modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Manualmente")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Encabezado (equivalente a tv_selected_date y el clic del header)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { openDatePicker(context) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = headerDateText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Text("🔍", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            // RecyclerView Horizontal (convertido a LazyRow)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = listState,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                itemsIndexed(allDates) { index, dateItem ->
                    DateItemView(
                        dateItem = dateItem,
                        isSelected = index == selectedDateIndex,
                        onDateClick = {
                            selectedDateIndex = index
                        }
                    )
                }
            }

            // RESUMEN DE MACROS DEL DÍA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CircularProgressBar(
                        percentage = (totalCalories.toFloat() / userGoals.calories.toFloat()).coerceAtMost(1f),
                        number = totalCalories,
                        label = "Calorías",
                        color = MaterialTheme.colorScheme.primary
                    )
                    CircularProgressBar(
                        percentage = (totalProtein.toFloat() / userGoals.protein.toFloat()).coerceAtMost(1f),
                        number = totalProtein.toInt(),
                        label = "Proteínas",
                        color = Color(0xFF4CAF50) // Verde
                    )
                    CircularProgressBar(
                        percentage = (totalCarbs.toFloat() / userGoals.carbs.toFloat()).coerceAtMost(1f),
                        number = totalCarbs.toInt(),
                        label = "Carbos",
                        color = Color(0xFFFFC107) // Ambar
                    )
                    CircularProgressBar(
                        percentage = (totalFat.toFloat() / userGoals.fat.toFloat()).coerceAtMost(1f),
                        number = totalFat.toInt(),
                        label = "Grasas",
                        color = Color(0xFFF44336) // Rojo
                    )
                }
            }

            // Lista de Comidas
            if (meals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay comidas registradas para este día.", fontSize = 16.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    items(meals) { meal ->
                        MealCard(meal = meal, onDelete = {
                            viewModel.deleteMeal(meal)
                            Toast.makeText(context, "Comida eliminada", Toast.LENGTH_SHORT).show()
                        })
                    }
                }
            }
        }

        if (showAddDialog) {
            AddMealDialog(
                onDismiss = { showAddDialog = false },
                onSave = { name, cal, prot, carb, fat, type ->
                    val meal = MealEntity(
                        name = name,
                        calories = cal,
                        protein = prot,
                        carbs = carb,
                        fat = fat,
                        grams = 0.0, // Manual no pide gramos obligatorio
                        mealType = type,
                        date = selectedDayStart + 1000 // Guardar en el día seleccionado (inicio + 1 seg)
                    )
                    viewModel.insertMeal(meal)
                    showAddDialog = false
                    Toast.makeText(context, "Comida agregada", Toast.LENGTH_SHORT).show()
                }
            )
        }
        
        if (showGoalsDialog) {
            EditGoalsDialog(
                currentGoals = userGoals,
                onDismiss = { showGoalsDialog = false },
                onSave = { newGoals ->
                    viewModel.updateGoals(newGoals)
                    showGoalsDialog = false
                    Toast.makeText(context, "Metas actualizadas", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

// =========================================================================
// FUNCIÓN DE AYUDA (DEL CÓDIGO 2)
// =========================================================================

/**
 * Función que crea una URI de archivo temporal para la cámara.
 */
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

/**
 * Función que convierte la URI a un Bitmap.
 */
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

// =========================================================================
// VIEW MODEL (Centralizado)
// =========================================================================

class MainViewModel(private val mealDao: MealDao) : ViewModel() {
    
    // Estado de las metas del usuario (en memoria, idealmente iría en DataStore)
    private val _userGoals = MutableStateFlow(UserGoals())
    val userGoals = _userGoals.asStateFlow()
    
    fun updateGoals(newGoals: UserGoals) {
        _userGoals.value = newGoals
    }
    
    fun insertMeal(meal: MealEntity) {
        viewModelScope.launch {
            mealDao.insertMeal(meal)
        }
    }
    
    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch {
            mealDao.deleteMeal(meal)
        }
    }

    fun getMealsForDay(start: Long, end: Long): Flow<List<MealEntity>> {
        return mealDao.getMealsForDay(start, end)
    }
    
    // Helper scope
    val viewModelScope = kotlinx.coroutines.CoroutineScope(Dispatchers.Main)
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

// =========================================================================
// VISTA ANÁLISIS NUTRICIONAL (DEL CÓDIGO 2)
// =========================================================================

@Composable
fun VisionScreen(speak: (String) -> Unit, viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Estados locales
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var resultText by remember { mutableStateOf("Analiza Nutricional con Gemini") }
    var isLoading by remember { mutableStateOf(false) }
    var currentAnalysisResult by remember { mutableStateOf<GeminiResponse?>(null) }

    // Estado para la URI temporal de la cámara
    val cameraUri = remember { mutableStateOf<Uri?>(null) }

    // Función central para actualizar estados después de seleccionar/tomar una imagen
    fun handleNewImageUri(uri: Uri?) {
        if (uri != null) {
            imageBitmap = uriToBitmap(context, uri)
            resultText = "Imagen seleccionada. Presiona Analizar Comida."
            currentAnalysisResult = null // Resetear resultado previo
        }
    }

    // 1. Lanzador de la Galería
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        handleNewImageUri(uri)
    }

    // 2. Lanzador de la Cámara
    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            handleNewImageUri(cameraUri.value)
        }
    }

    // 3. Lanzador de Permisos (para Android 13+)
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

    // Función de análisis de imagen
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
                apiKey = GEMINI_API_KEY
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

    // Función para guardar comida
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
            // Opcional: Limpiar después de guardar
            // currentAnalysisResult = null
            // imageBitmap = null
        }
    }

    // **********************************
    // * INTERFAZ DE USUARIO (COMPOSE) *
    // **********************************
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Análisis Nutricional con Gemini", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Previsualización de la Imagen
        imageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Imagen de comida seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(8.dp)
                .background(Color.LightGray)
        ) {
            Text("No hay imagen seleccionada", Modifier.align(Alignment.Center))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de Acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Botón de Galería
            Button(
                onClick = {
                    pickMedia.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                enabled = !isLoading
            ) {
                Text("Seleccionar Foto")
            }

            // Botón de Cámara
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

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Análisis
        Button(
            onClick = { analyzeImage() },
            enabled = !isLoading && imageBitmap != null
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Analizando...")
            } else {
                Text("Analizar Comida con Gemini")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resultado del Análisis
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de Registro (Solo visibles si hay un resultado válido)
        if (!isLoading && currentAnalysisResult != null) {
            Text(
                text = "Registrar en mi Agenda:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// =========================================================================
// ESTRUCTURA PRINCIPAL (ACTIVIDAD)
// =========================================================================

class MainActivity : ComponentActivity() {

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]

        // Inicialización del motor Text-to-Speech (DEL CÓDIGO 2)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale("es", "ES"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported")
                }
            } else {
                Log.e("TTS", "Initialization Failed!")
            }
        }

        setContent {
            MaterialTheme {
                MainAppScreen(::speak, viewModel)
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

    private fun speak(text: String) {
        if (::tts.isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID)
        }
    }
}

// =========================================================================
// BARRA DE NAVEGACIÓN Y PANTALLA PRINCIPAL (FUSIÓN)
// =========================================================================

@Composable
fun MainAppScreen(speak: (String) -> Unit, viewModel: MainViewModel) {
    var selectedScreen by remember { mutableStateOf(Screen.CALENDAR) }

    Scaffold(
        topBar = { AppTopBar(selectedScreen) },
        bottomBar = {
            BottomNavigationBar(
                selectedScreen = selectedScreen,
                onScreenSelected = { selectedScreen = it }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                Screen.CALENDAR -> CalendarScreen(viewModel) // Contenido del Código 1
                Screen.VISION -> VisionScreen(speak, viewModel)   // Contenido del Código 2
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(currentScreen: Screen) {
    TopAppBar(
        title = {
            Text(
                text = when (currentScreen) {
                    Screen.CALENDAR -> "📅 Mi Agenda Nutricional"
                    Screen.VISION -> "📷 Análisis de Comida"
                },
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

// AÑADE ESTA FUNCIÓN (Reemplaza NavItem)
@Composable
fun BottomNavTextButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer

    Button(
        onClick = onClick,
        modifier = Modifier.height(48.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else Color.Transparent,
            contentColor = color
        ),
        elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
        )
    }
}

// MODIFICA ESTA FUNCIÓN
@Composable
fun BottomNavigationBar(selectedScreen: Screen, onScreenSelected: (Screen) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavTextButton(
            label = "Calendario",
            isSelected = selectedScreen == Screen.CALENDAR,
            onClick = { onScreenSelected(Screen.CALENDAR) }
        )
        BottomNavTextButton(
            label = "Comida",
            isSelected = selectedScreen == Screen.VISION,
            onClick = { onScreenSelected(Screen.VISION) }
        )
    }
}
