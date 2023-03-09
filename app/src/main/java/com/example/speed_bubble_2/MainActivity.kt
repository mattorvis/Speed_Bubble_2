package com.example.speed_bubble_2

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    //private lateinit var accelerometer: Sensor
    //private lateinit var gyroscope: Sensor
    //private var gravitySensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null
    //private lateinit var gravityTextView: TextView
    private lateinit var velocityTextView: TextView
    //private lateinit var accelerationTextView: TextView
    //private lateinit var gravitySensorTextView: TextView
    private lateinit var accelerometerSensorTextView: TextView
    //private var gravity: FloatArray = FloatArray(3)
    private var velocity: FloatArray = FloatArray(3)
    //private var alpha = 0.8f
    private var lastUpdateTime: Long = 0
    private var dt: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get reference to gravity TextView
        //gravityTextView = findViewById(R.id.gravityTextView)
        velocityTextView = findViewById(R.id.velocityTextView)
        //accelerationTextView = findViewById(R.id.accelerationTextView)
        //gravitySensorTextView = findViewById(R.id.gravitySensorTextView)
        accelerometerSensorTextView = findViewById(R.id.accelerometerSensorTextView)


        // Get reference to the sensor manager and sensors
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        //accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        //gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        lastUpdateTime = System.currentTimeMillis()

        // Initialize gravity vector to zero
        //gravity = floatArrayOf(0f, 0f, 0f)
        velocity = floatArrayOf(0f, 0f, 0f)

        // Check if the sensors are available
        //if (gravitySensor == null) {
        //    gravitySensorTextView.text = "Gravity Sensor Not Available"
        //}
        if (accelerometerSensor == null) {
            accelerometerSensorTextView.text = "Accelerometer Sensor Not Available"
        }
    }

    override fun onResume() {
        super.onResume()
        // Register sensor listeners
        //sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        //sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        // Register the sensor listeners
        //sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        lastUpdateTime = System.currentTimeMillis() // Initialize last update time
    }

    override fun onPause() {
        super.onPause()
        // Unregister sensor listeners
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used, but required to implement SensorEventListener
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Check if the event is null
        if (event == null) {
            return
        }

        // Check which sensor has triggered the event
        when (event?.sensor?.type) {
            Sensor.TYPE_LINEAR_ACCELERATION -> {
                // Compute the gravity vector using a complementary filter
                val currentTime = System.currentTimeMillis()
                //dt = (currentTime - lastUpdateTime) / 1000f
                dt = (currentTime - lastUpdateTime) /1f

                //val alpha = 0.8f
                //val gravityMagnitude = Math.sqrt(
                //    (event.values[0] * event.values[0] +
                //            event.values[1] * event.values[1] +
                //            event.values[2] * event.values[2]).toDouble()
                //).toFloat()

                if (dt >= 100) {
                    //gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
                    //gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
                    //gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

                    // Remove the gravity component from the accelerometer readings
                    val linearAccelerationValues = floatArrayOf(
                        event.values[0],
                        event.values[1],
                        event.values[2]
                    )
                    // Display the x, y, and z components of the gravity vector in the TextView
                    accelerometerSensorTextView.text =
                        "Acceleration:\nX: ${linearAccelerationValues[0]}\nY: ${linearAccelerationValues[1]}\nZ: ${linearAccelerationValues[2]}"

                    //gravityTextView.text =
                    //    "Gravity:\nX: ${gravity[0]}\nY: ${gravity[1]}\nZ: ${gravity[2]}"


                    // Calculate the velocity using the formula: v = v0 + a*t
                    velocity[0] += linearAccelerationValues[0] * dt / 1000
                    velocity[1] += linearAccelerationValues[1] * dt / 1000
                    velocity[2] += linearAccelerationValues[2] * dt / 1000
                    velocityTextView.text = "Velocity:\nX: ${velocity[0]}\nY: ${velocity[1]}\nZ: ${velocity[2]}"

                    lastUpdateTime = currentTime
                }
            }
        }

        // Get the sensor type
        val sensorType = event.sensor.type

        // Update the UI based on the sensor type
        if (sensorType == Sensor.TYPE_GRAVITY) {
            val gravityX = event.values[0]
            val gravityY = event.values[1]
            val gravityZ = event.values[2]
            //gravitySensorTextView.text = "Gravity Sensor:\nX: $gravityX\nY: $gravityY\nZ: $gravityZ"
        } else if (sensorType == Sensor.TYPE_LINEAR_ACCELERATION) {
            val accelerometerX = event.values[0]
            val accelerometerY = event.values[1]
            val accelerometerZ = event.values[2]
            //accelerometerSensorTextView.text = "Accelerometer Sensor:\nX: $accelerometerX\nY: $accelerometerY\nZ: $accelerometerZ"
        }


    }

}
