package com.example.athena.ad340hw1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.util.Log
import android.widget.ImageView


class RobotActivity1 : AppCompatActivity() {

    val ACT = RobotActivity1::class.java.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            Log.d(ACT, "onCreate() Restoring previous state")
            /* restore state */
        }else {
            Log.d(ACT, "onCreate() No saved state available")
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_robot_activity1)
            val beratement = "? What an clunker!"

            // Get the Intent that started this activity and extract the string
            val message = intent.getStringExtra(EXTRA_MESSAGE)

            if(message == "R2-D2" || message == "r2-d2" || message == "R2-d2" || message == "r2-D2"){
                Log.d(ACT, "R2-D2 detected")
                val imgView = findViewById<ImageView>(R.id.robotView).setImageResource(R.drawable.brokenr2)
            }

            // Capture the layout's TextView and set the string as its text
            val textView = findViewById<TextView>(R.id.RobotMsg).apply {
                text = message + beratement
            }
        }
    }
}
