package com.example.athena.ad340hw1

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
import javax.net.ssl.HttpsURLConnection


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


