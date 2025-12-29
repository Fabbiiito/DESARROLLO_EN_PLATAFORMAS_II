package desarollodeplataformasii.nutriaxDBP

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var currentUser by mutableStateOf(auth.currentUser)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private fun translateError(exception: Exception?): String {
        if (exception !is FirebaseAuthException) {
            return exception?.message ?: "Error de conexión o configuración"
        }
        return when (exception.errorCode) {
            "ERROR_INVALID_EMAIL" -> "El correo electrónico no es válido."
            "ERROR_WRONG_PASSWORD" -> "La contraseña es incorrecta."
            "ERROR_USER_NOT_FOUND" -> "No existe ningún usuario con este correo."
            "ERROR_USER_DISABLED" -> "Esta cuenta de usuario ha sido inhabilitada."
            "ERROR_TOO_MANY_REQUESTS" -> "Demasiados intentos. Inténtalo más tarde."
            "ERROR_OPERATION_NOT_ALLOWED" -> "El inicio de sesión no está habilitado en la consola."
            "ERROR_WEAK_PASSWORD" -> "La contraseña es muy débil (mínimo 6 caracteres)."
            "ERROR_EMAIL_ALREADY_IN_USE" -> "Este correo ya está registrado."
            "ERROR_INVALID_CREDENTIAL" -> "Credenciales incorrectas o expiradas."
            else -> "Error: ${exception.message}"
        }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            errorMessage = "Completa todos los campos"
            return
        }
        isLoading = true
        errorMessage = null
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    onSuccess()
                } else {
                    errorMessage = translateError(task.exception as? Exception)
                }
            }
    }

    fun register(email: String, pass: String, name: String, onSuccess: () -> Unit) {
        if (email.isBlank() || pass.isBlank() || name.isBlank()) {
            errorMessage = "Completa todos los campos"
            return
        }
        isLoading = true
        errorMessage = null
        
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userData = hashMapOf(
                        "uid" to user?.uid,
                        "name" to name,
                        "email" to email,
                        "createdAt" to System.currentTimeMillis()
                    )
                    
                    val uid = user?.uid
                    if (uid != null) {
                        db.collection("users").document(uid).set(userData)
                            .addOnCompleteListener { dbTask ->
                                isLoading = false
                                if (dbTask.isSuccessful) {
                                    currentUser = user
                                    onSuccess()
                                } else {
                                    // Si falla Firestore, al menos logueamos al usuario pero avisamos
                                    currentUser = user
                                    errorMessage = "Usuario creado, pero no se pudieron guardar los datos adicionales."
                                    onSuccess() 
                                }
                            }
                    } else {
                        isLoading = false
                        currentUser = user
                        onSuccess()
                    }
                } else {
                    isLoading = false
                    errorMessage = translateError(task.exception as? Exception)
                }
            }
    }

    fun logout() {
        auth.signOut()
        currentUser = null
    }
}
