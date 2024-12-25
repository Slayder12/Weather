package com.example.weather

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.weather.models.CurrentWeather
import com.example.weather.utils.RetrofitInstance
import com.example.weather.utils.WindDirection
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class WeatherActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var toolbar: Toolbar
    private lateinit var cityET: EditText
    private lateinit var getDataByCityBTN: Button
    private lateinit var getCurrentDataBTN: Button
    private lateinit var cityTV: TextView
    private lateinit var temperatureTV: TextView
    private lateinit var imageIV: ImageView
    private lateinit var windDirectionTV: TextView
    private lateinit var windSpeedTV: TextView
    private lateinit var atmosphericPressureTV: TextView
    private lateinit var humidityTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        init()

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        getLastKnownLocation()

        getDataByCityBTN.setOnClickListener {
            val cityName = cityET.text.toString()
            if (cityName.isNotEmpty()) {
                getCurrentWeatherByCity(cityName)
            } else {
                Toast.makeText(this, "Пожалуйста, введите название города", Toast.LENGTH_SHORT).show()
            }
        }

        getCurrentDataBTN.setOnClickListener{
            getLastKnownLocation()
        }

    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)

        cityET = findViewById(R.id.cityET)
        getDataByCityBTN = findViewById(R.id.getDataByCityBTN)
        getCurrentDataBTN = findViewById(R.id.getCurrentDataBTN)
        cityTV = findViewById(R.id.cityTV)
        temperatureTV = findViewById(R.id.temperatureTV)
        imageIV = findViewById(R.id.imageIV)
        windDirectionTV = findViewById(R.id.windDirectionTV)
        windSpeedTV = findViewById(R.id.windSpeedTV)
        atmosphericPressureTV = findViewById(R.id.atmosphericPressureTV)
        humidityTV  = findViewById(R.id.humidityTV)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                getCurrentWeather(latitude, longitude)
            } else {
                Toast.makeText(this, "Не удалось получить местоположение", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun getCurrentWeather(lat: Double, lon: Double) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getCurrentWeather(
                    lat,
                    lon,
                    "metric",
                    applicationContext.getString(R.string.api_Kay)
                )
            } catch (e: IOException) {
                appErrorMassage(e)
                return@launch
            } catch (e: HttpException){
                httpErrorMassage(e)
                return@launch
            }
            responseData(response)
        }
    }

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun getCurrentWeatherByCity(city: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getCurrentWeatherByCity(
                    city,
                    "metric",
                    applicationContext.getString(R.string.api_Kay)
                )
            } catch (e: IOException) {
                appErrorMassage(e)
                return@launch
            } catch (e: HttpException) {
                httpErrorMassage(e)
                return@launch
            }
            responseData(response)
        }
    }

    private fun httpErrorMassage(e: HttpException) {
        Toast.makeText(applicationContext, "http error${e.message}", Toast.LENGTH_SHORT).show()
    }

    private fun appErrorMassage(e: IOException) {
        Toast.makeText(applicationContext, "app error${e.message}", Toast.LENGTH_SHORT).show()
    }

    private suspend fun responseData(response: Response<CurrentWeather>) {
        if (response.isSuccessful && response.body() != null) {
            withContext(Dispatchers.Main) {
                val data = response.body()
                updateUI(data!!)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(data: CurrentWeather) {
        cityTV.text = data.name
        temperatureTV.text = "${data.main.temp} °C \n" +
                "(Ощущается как: ${data.main.feels_like} °C)\n" +
                "Часовой пояс: ${convertTimezoneOffset(data.timezone)}"
        windDirectionTV.text = "Направление ветра\n" +
                WindDirection().getWindDirection(data.wind.deg)
        windSpeedTV.text = "Скорость ветра\n${data.wind.speed} м/сек"
        val iconId = data.weather[0].icon
        val imageUrl = "https://openweathermap.org/img/wn/$iconId@4x.png"
        Picasso.get().load(imageUrl).into(imageIV)
        val convertPressure = (data.main.pressure / 1.33).toInt()
        atmosphericPressureTV.text = "Давление\n$convertPressure мм рт. ст."
        humidityTV.text = "Влажность\n${data.main.humidity}%"
    }

    @SuppressLint("DefaultLocale")
    fun convertTimezoneOffset(offsetInSeconds: Int): String {
        val hours = offsetInSeconds / 3600
        val minutes = (offsetInSeconds % 3600) / 60
        return String.format("%+03d:%02d", hours, minutes)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenu -> {
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}