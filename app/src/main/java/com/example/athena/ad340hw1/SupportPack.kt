package com.example.athena.ad340hw1

import android.content.Context
import android.content.SharedPreferences
import android.view.View

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
