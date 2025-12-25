package desarollodeplataformasii.nutriaxDBP.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    @SerialName("alimento_identificado") val foodName: String,
    @SerialName("estimacion_gramos") val grams: Double = 0.0,
    @SerialName("macros_estimados") val macros: Macros,
    @SerialName("consejo_nutricional") val advice: String = ""
)

@Serializable
data class Macros(
    @SerialName("calorias_kcal") val calories: Int = 0,
    @SerialName("proteinas_g") val protein: Double = 0.0,
    @SerialName("carbohidratos_g") val carbs: Double = 0.0,
    @SerialName("grasas_g") val fat: Double = 0.0
)
