package com.example.athena.ad340hw1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.graphics.Color
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast




const val EXTRA_MESSAGE = "MESSAGE"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit  var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        val grdview: GridView = findViewById(R.id.gridview)
        grdview.adapter = ImageAdapter(this)

        val naview: NavigationView = findViewById(R.id.nav_view)
        //What really messed me up was the act of setting the listener while also overriding the
        // selected function. Seems like something that could be condensed into one action
        naview.setNavigationItemSelectedListener(this)
        drawer = findViewById(R.id.drawer)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_about -> {
                val aboutIntent = Intent(this, AboutActivity::class.java)
                startActivity(aboutIntent)
            }

            R.id.nav_zombies -> {
                //item.setOnMenuItemClickListener {Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();  true}
                val aboutIntent = Intent(this, ZombieMovies::class.java)
                startActivity(aboutIntent)
            }
            R.id.nav_slideshow -> {
                Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
            }
            R.id.nav_manage -> {
                Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
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
            Toast.makeText(this@MainActivity, "Action clicked", Toast.LENGTH_LONG).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun robotMessage(view: View) {
        val editText = findViewById<EditText>(R.id.textInputRobot)

        val message = editText.text.toString()
        val intent = Intent(this, RobotActivity1::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    /*fun wowClick(){
        //Toast.makeText(this, "Wow", Toast.LENGTH_SHORT).show()
    }*/



    class ImageAdapter(private val mContext: MainActivity) : BaseAdapter() {

        private val mThumbIds = arrayOf<Int>(
                R.drawable.zombie, R.drawable.sample_3,
                R.drawable.sample_4, R.drawable.sample_5)

        private val bttnLables = arrayOf<String>(
                "Zombie Movies","About","Favorite Acids","Do not click"
        )

        override fun getCount(): Int = mThumbIds.size

        override fun getItem(position: Int): Any? = null

        override fun getItemId(position: Int): Long = 0L

        // create a new ImageView for each item referenced by the Adapter
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val imageView: Button
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = Button(mContext)
                imageView.layoutParams = ViewGroup.LayoutParams(275, 175)
                //imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                //imageView.setPadding(15, 15, 15, 15)
                if(position == 0) {
                    imageView.setOnClickListener {
                        val intent = Intent(mContext, ZombieMovies::class.java).apply {
                            putExtra(EXTRA_MESSAGE, "R2-D2")
                        }
                        mContext.startActivity(intent)
                    }
                }else if(position == 1){
                    imageView.setOnClickListener {
                        val intent = Intent(mContext, AboutActivity::class.java).apply {
                        }
                        mContext.startActivity(intent)
                    }
                }
                else {
                    imageView.setOnClickListener(View.OnClickListener {
                        Toast.makeText(mContext, bttnLables[position], Toast.LENGTH_SHORT).show()
                    })
                }

            } else {
                imageView = convertView as Button
            }

            //imageView.setImageResource(mThumbIds[position])
            imageView.text = bttnLables[position]
            imageView.setTextColor(Color.LTGRAY)
            imageView.setBackgroundResource(mThumbIds[position])
            return imageView
        }
    }


    /** Called when the user taps the Send button */



}



