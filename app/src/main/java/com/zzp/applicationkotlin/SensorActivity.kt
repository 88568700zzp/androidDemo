package com.zzp.applicationkotlin

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.util.Log
import android.view.OrientationEventListener
import kotlinx.android.synthetic.main.activity_sensor.*


class SensorActivity : AppCompatActivity() {

    private val TAG = "SensorActivity_"
    private lateinit var  sensorEventListenerImpl:SensorEventListenerImpl
    private var orientationListenerDelayTime:Long = 0L

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorEventListenerImpl = SensorEventListenerImpl(this)

        sensorManager.registerListener(sensorEventListenerImpl, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(sensorEventListenerImpl)
        super.onDestroy()
    }

    fun onOrientationChanged(orientation:Int){
        val operationDelay = System.currentTimeMillis() - orientationListenerDelayTime > 500
        if(operationDelay){
            textView2.text = "$orientation"
        }
        if ((orientation >= 300 || orientation <= 30) && operationDelay) {
            //屏幕顶部朝上
            textView1.text = "屏幕顶部朝上"
            orientationListenerDelayTime = System.currentTimeMillis()
        } else if (orientation in 260..280 && operationDelay) {
            //屏幕左边朝上
            textView1.text = "屏幕左边朝上"
            orientationListenerDelayTime = System.currentTimeMillis()
        } else if (orientation in 70..90 && operationDelay) {
            //屏幕右边朝上
            textView1.text = "屏幕右边朝上"
            orientationListenerDelayTime = System.currentTimeMillis()
        }
    }


    class SensorEventListenerImpl(var activity: SensorActivity) : SensorEventListener {
        private var mOrientation:Int = 0

        override fun onSensorChanged(event: SensorEvent) {
            val values = event.values
            var orientation = OrientationEventListener.ORIENTATION_UNKNOWN
            val X = -values[_DATA_X]
            val Y = -values[_DATA_Y]
            val Z = -values[_DATA_Z]
            val magnitude = X * X + Y * Y
            // Don't trust the angle if the magnitude is small compared to the y value
            if (magnitude * 4 >= Z * Z) {
                val OneEightyOverPi = 57.29577957855f
                val angle = Math.atan2(-Y.toDouble(), X.toDouble()).toFloat() * OneEightyOverPi
                orientation = 90 - Math.round(angle)
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360
                }
                while (orientation < 0) {
                    orientation += 360
                }
            }

            if (orientation != mOrientation) {
                mOrientation = orientation
                activity.onOrientationChanged(orientation)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        companion object {
            private const val _DATA_X = 0
            private const val _DATA_Y = 1
            private const val _DATA_Z = 2
        }
    }
}