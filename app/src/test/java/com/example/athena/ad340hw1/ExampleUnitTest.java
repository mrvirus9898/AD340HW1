package com.example.athena.ad340hw1;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Test;


import static org.junit.Assert.*;
import static com.example.athena.ad340hw1.SupportPackKt.isEmpty;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //@Test
    //public void addition_isCorrect() {
       // assertEquals(4, 2 + 2);
   // }

    @Test
    public void empty_test(){

        assertFalse(isEmpty("Hello"));
        assertFalse(isEmpty("R2-D2"));
        assertFalse(isEmpty("PO 210"));
        assertTrue(isEmpty(" "));
        //assertTrue(isEmpty(null));
    }

    @Test
    public void read_write(){
        //Context dis =
        //SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
    }



}