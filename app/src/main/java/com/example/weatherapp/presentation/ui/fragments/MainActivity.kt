package com.example.weatherapp.presentation.ui.fragments

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.weatherapp.R
import org.json.JSONObject
import org.w3c.dom.Text
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API: String = "73ad75781221d5b7d51d10808bc59d4e"
    val LAT: String = "-23.5392157"
    val LON: String = "-46.6235439"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()

    }

    inner class weatherTask():AsyncTask<String, Void, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.rlMainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.tvErrorMsg).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?lat=-23.5392157&lon=-46.6235439&units=metric&appid=73ad75781221d5b7d51d10808bc59d4e")
                    .readText(Charsets.UTF_8)
            }
            catch (e: Exception){
                response = null
            }

            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj =  JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt = jsonObj.getLong("dt")
                val updatedAtText = "Atualizado em: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"ºC"
                val tempMin = "Minima: " + main.getString("temp_min")+"ºC"
                val tempMax = "Máxima: " + main.getString("temp_max")+"ºC"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise = sys.getLong("sunrise")
                val sunset = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescript = weather.getString("description")
                val adress = jsonObj.getString("name") + ", " + sys.getString("country")


                findViewById<TextView>(R.id.tvLocal).text = adress
                findViewById<TextView>(R.id.tvUpdatedAt).text = updatedAtText
                findViewById<TextView>(R.id.tvStatus).text = weatherDescript.capitalize()
                findViewById<TextView>(R.id.tvTemp).text = temp
                findViewById<TextView>(R.id.tvTempMin).text = tempMin
                findViewById<TextView>(R.id.tvTempMax).text = tempMax
                findViewById<TextView>(R.id.tvSunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.tvSunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.tvWind).text = windSpeed
                findViewById<TextView>(R.id.tvPressure).text = pressure
                findViewById<TextView>(R.id.tvHumidity).text = humidity

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.rlMainContainer).visibility = View.VISIBLE
            }
            catch (e: Exception){
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.tvErrorMsg).visibility = View.VISIBLE
            }
        }
    }
}