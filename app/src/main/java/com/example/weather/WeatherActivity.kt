package com.example.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.weather.utils.RetrofitInstance
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class WeatherActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var cityET: EditText
    private lateinit var getDataBTN: Button
    private lateinit var cityTV: TextView
    private lateinit var temperatureTV: TextView
    private lateinit var imageIV: ImageView
    private lateinit var windDirectionTV: TextView
    private lateinit var windSpeedTV: TextView
    private lateinit var atmosphericPressureTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        toolbar = findViewById(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)

        cityET = findViewById(R.id.cityET)
        getDataBTN = findViewById(R.id.getDataBTN)
        cityTV = findViewById(R.id.cityTV)
        temperatureTV = findViewById(R.id.temperatureTV)
        imageIV = findViewById(R.id.imageIV)
        windDirectionTV = findViewById(R.id.windDirectionTV)
        windSpeedTV = findViewById(R.id.windSpeedTV)
        atmosphericPressureTV = findViewById(R.id.atmosphericPressureTV)

        getCurrentWeather()
    }

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun getCurrentWeather() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getCurrentWeather(
                    "Moscow",
                    "metric",
                    applicationContext.getString(R.string.api_Kay)
                )

            } catch (e: IOException) {
                Toast.makeText(applicationContext, "app error${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException){
                Toast.makeText(applicationContext, "http error${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (response.isSuccessful && response.body() != null){
                withContext(Dispatchers.Main) {
                    val data = response.body()
                    cityTV.text = data!!.name
                    temperatureTV.text = "${data.main.temp} °C"
                    windDirectionTV.text = "${data.wind.deg}"
                    windSpeedTV.text = "${data.wind.speed} m/sec"
                    val iconId = data.weather[0].icon
                    val imageUrl = "https://openweathermap.org/img/wn/$iconId@4x.png"
                    Picasso.get().load(imageUrl).into(imageIV)
                    val convertPressure = (data.main.pressure / 1.33).toInt()
                    atmosphericPressureTV.text = "$convertPressure mm Hg"
                }
            }
        }
    }
}