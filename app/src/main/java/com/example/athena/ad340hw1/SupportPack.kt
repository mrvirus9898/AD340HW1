package com.example.athena.ad340hw1

import android.content.Context
import android.content.SharedPreferences

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
