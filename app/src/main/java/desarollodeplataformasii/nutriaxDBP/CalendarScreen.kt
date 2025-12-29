// CalendarScreen.kt
package desarollodeplataformasii.nutriaxDBP

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import desarollodeplataformasii.nutriaxDBP.database.MealEntity
import kotlinx.coroutines.launch
import java.util.Calendar

data class DateItem(
    val dayOfMonth: Int,
    val dayOfWeek: String,
    val month: Int,
    val year: Int
)

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
    var mealType by remember { mutableStateOf("Almuerzo") }
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
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    listOf("Desayuno", "Almuerzo", "Cena").forEach { type ->
                        Button(
                            onClick = { mealType = type },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (mealType == type) MaterialTheme.colorScheme.primary else Color.LightGray
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

private fun getDayName(day: Int): String {
    return when (day) {
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
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.add(Calendar.DAY_OF_MONTH, -14)
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

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val todayCalendar = remember { Calendar.getInstance() }
    val allDates = remember { generateSampleDates(todayCalendar) }
    var selectedDateIndex by remember { mutableIntStateOf(findTodayPosition(allDates)) }
    val initialPosition = remember { findTodayPosition(allDates) }
    val listState = rememberLazyListState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showGoalsDialog by remember { mutableStateOf(false) }
    val userGoals by viewModel.userGoals.collectAsState()
    val selectedDateItem = if (selectedDateIndex != -1) allDates[selectedDateIndex] else allDates.first()
    val headerDateText = remember(selectedDateItem) {
        "${getMonthName(selectedDateItem.month)} ${selectedDateItem.dayOfMonth}, ${selectedDateItem.year}"
    }
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
    val meals by viewModel.getMealsForDay(selectedDayStart, selectedDayEnd).collectAsState(initial = emptyList())
    val totalCalories = remember(meals) { meals.sumOf { it.calories } }
    val totalProtein = remember(meals) { meals.sumOf { it.protein } }
    val totalCarbs = remember(meals) { meals.sumOf { it.carbs } }
    val totalFat = remember(meals) { meals.sumOf { it.fat } }
    LaunchedEffect(Unit) {
        if (initialPosition != -1) {
            val weekStart = initialPosition - (initialPosition % 7)
            listState.scrollToItem(weekStart)
        }
    }
    fun openDatePicker(context: Context) {
        val anio = todayCalendar.get(Calendar.YEAR)
        val mes = todayCalendar.get(Calendar.MONTH)
        val dia = todayCalendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)
                val newDateItem = DateItem(day, getDayName(selectedCalendar.get(Calendar.DAY_OF_WEEK)), month, year)
                val newIndex = allDates.indexOfFirst { it == newDateItem }
                if (newIndex != -1) {
                    selectedDateIndex = newIndex
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
                        onDateClick = { selectedDateIndex = index }
                    )
                }
            }
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
                        color = Color(0xFF4CAF50)
                    )
                    CircularProgressBar(
                        percentage = (totalCarbs.toFloat() / userGoals.carbs.toFloat()).coerceAtMost(1f),
                        number = totalCarbs.toInt(),
                        label = "Carbos",
                        color = Color(0xFFFFC107)
                    )
                    CircularProgressBar(
                        percentage = (totalFat.toFloat() / userGoals.fat.toFloat()).coerceAtMost(1f),
                        number = totalFat.toInt(),
                        label = "Grasas",
                        color = Color(0xFFF44336)
                    )
                }
            }
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
                        grams = 0.0,
                        mealType = type,
                        date = selectedDayStart + 1000
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