package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.weather.R


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       fetchWeatherData("varanasi")
        search()


    }
    private fun search()
    {
        val searchView:androidx.appcompat.widget.SearchView=findViewById(R.id.searchBar)
        searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query.toString())
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }


        })

    }
    private fun fetchWeatherData(cityName:String) {
        val retrofit= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
            val location:TextView=findViewById(R.id.location)
            location.text=cityName
        val response=retrofit.getWeatherData(cityName,"1386e3f5600fe558d4a9e4b1100507c0", "metric")
        response.enqueue(object:Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody=response.body()
                if( responseBody!=null&& response.isSuccessful)
                {

                    val v=responseBody.main.temp.toInt()
                    val vv:TextView=findViewById(R.id.temp)
                    vv.text="${v.toString()}°C"

                    val h=responseBody.main.humidity.toInt()
                    val hh:TextView=findViewById(R.id.humidity)
                    hh.text=h.toString()+"%"

                    val w=responseBody.wind.speed
                    val ww:TextView=findViewById(R.id.windSpeed)
                    ww.text=w.toString()+"km/h"

                    val s=responseBody.sys.sunset
                    val ss:TextView=findViewById(R.id.sunSet)
                    ss.text=s.toString()

                    val ssr=responseBody.sys.sunrise
                    val sssr:TextView=findViewById(R.id.sunRise)
                    sssr.text=ssr.toString()

                    val r=responseBody.clouds.all
                    val rr:TextView=findViewById(R.id.rain)
                    rr.text=r.toString()+"%"

                    val f=responseBody.main.feels_like
                    val ff:TextView=findViewById(R.id.FeelsLike)
                    ff.text=f.toString()

                    val mt=responseBody.main.temp_max
                    val mmt:TextView=findViewById(R.id.maxTemp)
                    mmt.text="Max Temp: "+mt.toString()+"°C"

                    val m=responseBody.main.temp_min
                    val mm:TextView=findViewById(R.id.minTemp)
                    mm.text="Min Temp: "+m.toString()+"°C"
                   val c= responseBody.weather.firstOrNull()?.main?:"Sunny and clear sky"
                    if(c=="unknown"|| c=="Clear")
                    {

                    }
                    else if( c=="Rain"||c=="Drizzle"||c=="Thunderstorm")
                    {
                        val main:LinearLayout=findViewById(R.id.splashActivity)
                        main.setBackgroundResource(R.drawable.rain_background)

                        val anim:LottieAnimationView=findViewById(R.id.animation)
                        anim.setAnimation(R.raw.rain)

                        val cond:TextView=findViewById(R.id.description)
                        cond.text=responseBody.weather.firstOrNull()?.description?:"Sunny and clear sky"

                    }
                    else if(c=="Snow"|| v<16)
                    {
                        val main:LinearLayout=findViewById(R.id.splashActivity)
                        main.setBackgroundResource(R.drawable.snow_background)

                        val anim:LottieAnimationView=findViewById(R.id.animation)
                        anim.setAnimation(R.raw.snow)

                        val cond:TextView=findViewById(R.id.description)
                        cond.text=responseBody.weather.firstOrNull()?.description?:"Sunny and clear sky"
                    }
                    else if( c=="Clouds")
                    {
                        val main:LinearLayout=findViewById(R.id.splashActivity)
                        main.setBackgroundResource(R.drawable.colud_background)

                        val anim:LottieAnimationView=findViewById(R.id.animation)
                        anim.setAnimation(R.raw.cloud)

                        val cond:TextView=findViewById(R.id.description)
                        cond.text=responseBody.weather.firstOrNull()?.description?:"Sunny and clear sky"

                    }










                }
            }


            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {

            }


        })}


}