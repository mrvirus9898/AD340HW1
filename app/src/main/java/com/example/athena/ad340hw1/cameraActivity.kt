package com.example.athena.ad340hw1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import java.net.URL

import kotlinx.android.synthetic.main.activity_camera.*
//import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException


class cameraActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.setTitle("Camera Status")

        JsonTask(this).execute("https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2")
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

    private inner class JsonTask(var mContext: cameraActivity) : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String): String {


            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null

            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()


                val stream = connection.getInputStream()

                reader = BufferedReader(InputStreamReader(stream))

                val buffer = StringBuffer()
                var line = reader.readLine()

                while ((line) != null) {
                    buffer.append(line + "\n")
                    line = reader.readLine()
                }

                return buffer.toString()


            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
                try {
                    if (reader != null) {
                        reader.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return ""
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            println("START")
            var camList: MutableList<cameraStat> = mutableListOf<cameraStat>()
            var jObj: JSONObject = JSONObject(result)
            var coords: JSONArray = jObj.getJSONArray("Features")
            for(i in 0..(coords.length()-1)) {
                val tempJason = JSONObject(coords[i].toString())
                val cameraCount = JSONArray(tempJason.getString("Cameras")).length() - 1

                for(j in 0..cameraCount) {

                    /*println(JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Id"))
                    println(JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Description"))
                    println(JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("ImageUrl"))
                    println(JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Type"))*/

                    var tempCam: cameraStat = cameraStat()
                    tempCam.coordinates = JSONObject(coords[i].toString()).getString("PointCoordinate")
                    tempCam.id = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Id")
                    tempCam.description = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Description")
                    tempCam.imgUrl = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("ImageUrl")
                    tempCam.type = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Type")

                    camList.add(tempCam)

                }
            }
            var camArray: Array<cameraStat> = camList.toTypedArray()

            viewManager = LinearLayoutManager(mContext)
            viewAdapter = camAdapter(camArray, mContext)


            recyclerView = findViewById<RecyclerView>(R.id.cameraRecycler)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = viewManager
            recyclerView.adapter = viewAdapter
        }
    }





}
