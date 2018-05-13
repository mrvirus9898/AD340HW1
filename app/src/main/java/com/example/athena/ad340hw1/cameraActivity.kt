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


    class cameraStat(coordinates: String = "", id: String = "", imgUrl: String = "", description: String = "", type: String = "" ) : Parcelable {
        var coordinates = coordinates
        var id = id
        var imgUrl = imgUrl
        var description = description
        var type = type

        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString())


        //override fun toString() : String{     }
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(coordinates)
            parcel.writeString(id)
            parcel.writeString(imgUrl)
            parcel.writeString(description)
            parcel.writeString(type)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<cameraStat> {
            override fun createFromParcel(parcel: Parcel): cameraStat {
                return cameraStat(parcel)
            }

            override fun newArray(size: Int): Array<cameraStat?> {
                return arrayOfNulls(size)
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

    private inner class JsonTask(var mContext: cameraActivity) : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()

            /*pd = ProgressDialog(this@MainActivity)
            pd.setMessage("Please wait")
            pd.setCancelable(false)
            pd.show()*/
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
                    //Log.d("Response: ", "> $line")   //here u ll get whole response...... :-)
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

                    /*println(tempCam.coordinates)
                    println(tempCam.id)
                    println(tempCam.description)
                    println(tempCam.imgUrl)
                    println(tempCam.type)*/
                    camList.add(tempCam)

                    /*var isCam: cameraStat = camList.get(0)
                    println(isCam.coordinates)
                    println(isCam.id)
                    println(isCam.description)
                    println(isCam.imgUrl)
                    println(isCam.type)*/
                }
            }
            var camArray: Array<cameraStat> = camList.toTypedArray()

            viewManager = LinearLayoutManager(mContext)
            viewAdapter = MyAdapter(camArray, mContext)


            recyclerView = findViewById<RecyclerView>(R.id.cameraRecycler)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = viewManager
            recyclerView.adapter = viewAdapter
        }
    }

    class MyAdapter(private val myDataset: Array<cameraStat>, private val mContext : cameraActivity) :
            RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        var creationCounter = 0;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class ViewHolder(val imgView: ImageView) : RecyclerView.ViewHolder(imgView)



        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): MyAdapter.ViewHolder {
            // create a new view
            val imgView = ImageView(parent.context)
            // set the view's size, margins, paddings and layout parameters
            //textView.textSize = 35.5.toFloat()
            imgView.setPadding(20,20,20,20)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(20,20,20,20)
            imgView.layoutParams = params

            creationCounter++
            return ViewHolder(imgView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            //holder.imgView.text = myDataset[position].id
            //myDataset[position].imgUrl
            var completeURL: String = ""
            //println(myDataset[position].type)
            if(myDataset[position].type.equals("sdot")){
              //  println("http://www.seattle.gov/trafficcams/images/"+myDataset[position].imgUrl)
                completeURL = "http://www.seattle.gov/trafficcams/images/"+myDataset[position].imgUrl
            }else if(myDataset[position].type.equals("wsdot")){
                //println("http://images.wsdot.wa.gov/nw/"+myDataset[position].imgUrl)
                completeURL = "http://images.wsdot.wa.gov/nw/"+myDataset[position].imgUrl
            }
            println(position)
            DownloadImageTask(holder.imgView).execute(completeURL)

        }


        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size-1
    }

    class DownloadImageTask(internal var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

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

        override fun onPostExecute(result: Bitmap?) {
            if(result == null){
                bmImage.setImageResource(R.drawable.clunker)
            }else {
                bmImage.setImageBitmap(result)
            }
        }
    }

}
