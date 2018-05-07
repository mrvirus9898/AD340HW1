package com.example.athena.ad340hw1

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast


class RobotActivity1 : AppCompatActivity() {

    val ACT = RobotActivity1::class.java.getSimpleName()
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            Log.d(ACT, "onCreate() Restoring previous state")
            /* restore state */
        }else {
            Log.d(ACT, "onCreate() No saved state available")
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_robot_activity1)
            this.setTitle("Robot")
            setSupportActionBar(findViewById(R.id.my_toolbar))
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            val beratement = "? What an clunker!"
            prefs = getSharedPreferences("AD340",Context.MODE_PRIVATE)


            // Get the Intent that started this activity and extract the string
            //val message = intent.getStringExtra(EXTRA_MESSAGE)
            val message =  prefs.getString("favRobot", "")

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()


        if (id == R.id.action_favorite || id == R.id.action_settings) {
            Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
