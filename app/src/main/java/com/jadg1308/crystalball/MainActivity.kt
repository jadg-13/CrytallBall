package com.jadg1308.crystalball

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.jadg1308.crystalball.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private var sensorInitialized = false
    private val threshold = 250.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        iniciarCrystalBall()

    }

    private fun likeAnimation(
        imageView: LottieAnimationView, animation: Int
    ) {
        imageView.setAnimation(animation)
        imageView.playAnimation()
    }

    private fun iniciarCrystalBall() {
        likeAnimation(binding.ivMano, R.raw.shakecell)

        binding.ivCrystallBall.setOnClickListener{
            likeAnimation(binding.ivMano, R.raw.shakecell)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val accelerationX = event.values[0]
            val accelerationY = event.values[1]
            val accelerationZ = event.values[2]

            // Detectar si se ha agitado el celular
            val acceleration = accelerationX?.times(accelerationX)!! + accelerationY?.times(accelerationY)!! + accelerationZ?.times(accelerationZ)!!

            // Si la aceleración total es mayor a un umbral dado, se considera agitado
            if (acceleration > threshold) {
                // El dispositivo se está agitando
                giveAnswer()
            }

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onResume() {
        super.onResume()
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            sensorInitialized = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (sensorInitialized) {
            sensorManager.unregisterListener(this)
            sensorInitialized = false
        }
    }
    fun generateRandomNumber(): Int {
        return Random.nextInt(1, 4) // Genera un número aleatorio entre 1 y 3 (exclusivo)
    }

    fun giveAnswer() {
        val num = generateRandomNumber()
        when (num) {
            1 -> binding.tvRespuesta.text = "SI"
            2 -> binding.tvRespuesta.text = "NO"
            3 -> binding.tvRespuesta.text = "PUEDE SER"
        }
    }


}