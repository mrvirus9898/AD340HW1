package com.example.athena.ad340hw1

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import android.graphics.drawable.Drawable
import android.util.AttributeSet


/**
 * isEmpty supports both input validation and read write validation
 */
fun isEmpty(msg: String): Boolean{
    val whiteSpace = msg.replace(" ","").length == 0
    if(whiteSpace || msg.toString() == "" || msg == null){
        return true
    }
    return false
}

fun saveString(prefs: SharedPreferences, key: String, value: String): Boolean{
    val editor = prefs.edit()
    editor.putString(key,value)
    return editor.commit()
}

fun loadString(prefs: SharedPreferences, key: String): String{
    return prefs.getString(key, "")
}

fun getViewString(view: View){

}

fun preferTester(currentPref: SharedPreferences, key: String, test_string: String): Boolean{
    var editor = currentPref.edit()
    val original_string: String = currentPref.getString(key,"")


    editor.putString(key,test_string)
    editor.commit()
    val isWorking = (currentPref.getString(key,"").equals(test_string))
    editor.putString(key, original_string)
    editor.commit()

    return isWorking
}

@Throws(IOException::class)
fun downloadUrl(url: URL): String? {
    var stream: InputStream? = null
    var connection: HttpsURLConnection? = null
    var result: String? = null

    try {
        connection = url.openConnection() as HttpsURLConnection
        // Timeout for reading InputStream arbitrarily set to 3000ms.
        connection.setReadTimeout(3000)
        // Timeout for connection.connect() arbitrarily set to 3000ms.
        connection.setConnectTimeout(3000)
        // For this use case, set HTTP method to GET.
        connection.setRequestMethod("GET")
        // Already true by default but setting just in case; needs to be true since this request
        // is carrying an input (response) body.
        connection.setDoInput(true)
        // Open communications link (network traffic occurs here).
        connection.connect()
        //publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS)
        val responseCode = connection.getResponseCode()
        if (responseCode != HttpsURLConnection.HTTP_OK) {
            throw IOException("HTTP error code: $responseCode")
        }
        // Retrieve the response body as an InputStream.
        stream = connection.getInputStream()
        //publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0)
        if (stream != null) {
            var reader = InputStreamReader(stream, "UTF-8")

            result = reader.readText()
        }
    } finally {
        // Close Stream and disconnect HTTPS connection.
        if (stream != null) {
            stream!!.close()
        }
        if (connection != null) {
            connection!!.disconnect()
        }
    }
    return result
}

fun getData(url: String): String{
    return URL(url).readText()
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

class camAdapter(private val myDataset: Array<cameraStat>, private val mContext : cameraActivity) :
        RecyclerView.Adapter<camAdapter.ViewHolder>() {
    var creationCounter = 0;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(val imgView: ImageView) : RecyclerView.ViewHolder(imgView)



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): camAdapter.ViewHolder {
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

class ProportionalImageView : ImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d = drawable
        if (d != null) {
            val w = View.MeasureSpec.getSize(widthMeasureSpec)
            val h = w * d.intrinsicHeight / d.intrinsicWidth
            setMeasuredDimension(w, h)
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}