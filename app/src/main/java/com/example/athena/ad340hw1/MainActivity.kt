package com.example.athena.ad340hw1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.graphics.Color



const val EXTRA_MESSAGE = "MESSAGE"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val grdview: GridView = findViewById(R.id.gridview)

        grdview.adapter = ImageAdapter(this)
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



    class ImageAdapter(private val mContext: MainActivity) : BaseAdapter() {

        private val mThumbIds = arrayOf<Int>(
                R.drawable.zombie, R.drawable.sample_3,
                R.drawable.sample_4, R.drawable.sample_5)

        private val bttnLables = arrayOf<String>(
                "Zombie Movies","Robot Movies","Favorite Acids","Do not click"
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
                }else {
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

   /* class ButtonAdapter(private val mContext: MainActivity) : BaseAdapter() {

        private val bArray = arrayOfNulls<Button>(4)

        private var count = 0

        override fun getCount(): Int = count+1

        override fun getItem(position: Int): Any? = null

        fun getButton(position: Int): Button{
            return bArray[position] as Button
        }

        fun changeText(position: Int, msg: String){
            bArray[position]?.text = msg
        }

        override fun getItemId(position: Int): Long = 0L

        // create a new ImageView for each item referenced by the Adapter
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val buttonView: Button
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                buttonView = Button(mContext)
                buttonView.layoutParams = ViewGroup.LayoutParams(275, 175)
                // imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                //buttonView.setPadding(8, 8, 8, 8)

                //Heaven forgive me, this is so sloppy
                if(count == 0){
                    buttonView.text = "Zombie Movies"
                    buttonView.setOnClickListener {_ -> Toast.makeText(mContext,"Hello",Toast.LENGTH_SHORT)}
                   // buttonView.setText("Hello")
                }
                else if(count == 1){
                    buttonView.text = "Robot Movies"
                }
                else if(count == 2){
                    buttonView.text = "Favorite Acids"
                }
                else if(count == 3){
                    buttonView.text = "Do not click"
                }

                bArray.set(count,buttonView)
                count++
            } else {
                buttonView = convertView as Button
            }

            //imageView.setImageResource(mThumbIds[position])
            return buttonView
        }
    }*/

}



