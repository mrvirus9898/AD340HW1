package com.example.athena.ad340hw1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_display_zombie.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.os.AsyncTask
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast


class displayZombie : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_zombie)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.setTitle("Zombie Movie")

        val rank = intent.getIntExtra("Rank", 0)
        val title = intent.getStringExtra("Title")
        val year = intent.getIntExtra("Year",0)
        val director = intent.getStringExtra("Director")
        val image = intent.getStringExtra("Image")
        val description = intent.getStringExtra("Description")
        /*println("Hello")
        println(rank)
        println(title)
        println(year)
        println(director)
        println(image)
        println(description)*/

        val headline = findViewById<TextView>(R.id.zombie_headline)
        headline.textSize = 40.5.toFloat()
        headline.text = title + "(" + year.toString() + ")"

        DownloadImageTask(findViewById<ImageView>(R.id.zImageView)).execute(image);
        findViewById<ImageView>(R.id.zImageView).alpha = 0.5.toFloat()

        findViewById<TextView>(R.id.directorText).text = "Directed by: " + director
        findViewById<TextView>(R.id.descriptText).text = description
        findViewById<TextView>(R.id.descriptText).setMovementMethod(ScrollingMovementMethod())


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

    private inner class DownloadImageTask(internal var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                //Log.e("Error", e.message)
                e.printStackTrace()
            }
            return mIcon11
        }

        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
        }
    }

}
