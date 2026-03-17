package RequestContentData

data class Weather(
    val status: String,
    val temperature: List<Double>,
    val skycon: List<String>,
    val humidity: List<Double>,
    val windSpeed: List<Double>,
    val windDirection: List<Int>,
    val precipitation: List<Double>,
    val time: List<String>,
    val longitude: Double,
    val latitude: Double
)
