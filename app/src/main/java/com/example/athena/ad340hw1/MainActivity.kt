package com.example.athena.ad340hw1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText


const val EXTRA_MESSAGE = "MESSAGE"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** Called when the user taps the Send button */
    fun robotMessage(view: View) {
        val editText = findViewById<EditText>(R.id.textInputRobot)
        val message = editText.text.toString()
        val intent = Intent(this, RobotActivity1::class.java).apply {
           putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

}
