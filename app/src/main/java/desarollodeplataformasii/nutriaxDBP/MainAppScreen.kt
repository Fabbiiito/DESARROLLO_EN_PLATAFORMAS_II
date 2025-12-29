// MainAppScreen.kt
package desarollodeplataformasii.nutriaxDBP

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel

enum class Screen {
    CALENDAR, VISION, GEMINI_CHAT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(currentScreen: Screen, authViewModel: AuthViewModel, onLogout: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    val userEmail = authViewModel.currentUser?.email ?: "Usuario"

    TopAppBar(
        title = {
            Text(
                text = when (currentScreen) {
                    Screen.CALENDAR -> "📅 Mi Agenda"
                    Screen.VISION -> "📷 Comida"
                    Screen.GEMINI_CHAT -> "🧠 Asistente"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        actions = {
            Box {
                // El "redondo" para info del perfil
                Surface(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { showMenu = true },
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    Text(
                        text = userEmail,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Cerrar Sesión") },
                        onClick = {
                            showMenu = false
                            authViewModel.logout()
                            onLogout()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.ExitToApp, contentDescription = null)
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun MainAppScreen(speak: (String) -> Unit, mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = if (authViewModel.currentUser != null) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(authViewModel) {
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("main") {
            MainContent(speak, mainViewModel, authViewModel) {
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
    }
}

@Composable
fun MainContent(
    speak: (String) -> Unit,
    viewModel: MainViewModel,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    var selectedScreen by remember { mutableStateOf(Screen.CALENDAR) }
    
    Scaffold(
        topBar = { 
            AppTopBar(
                currentScreen = selectedScreen, 
                authViewModel = authViewModel,
                onLogout = onLogout
            ) 
        },
        bottomBar = {
            BottomNavigationBar(selectedScreen = selectedScreen) { selectedScreen = it }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                Screen.CALENDAR -> CalendarScreen(viewModel)
                Screen.VISION -> VisionScreen(speak, viewModel)
                Screen.GEMINI_CHAT -> GeminiChatScreen(speak)
            }
        }
    }
}

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
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else Color.Transparent,
            contentColor = color
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
        )
    }
}

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
        BottomNavTextButton(
            label = "Asistente",
            isSelected = selectedScreen == Screen.GEMINI_CHAT,
            onClick = { onScreenSelected(Screen.GEMINI_CHAT) }
        )
    }
}
