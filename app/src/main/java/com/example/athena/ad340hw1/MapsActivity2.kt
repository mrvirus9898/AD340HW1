package com.example.athena.ad340hw1

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var infoView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps2)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        infoView = findViewById(R.id.place_info)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


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
            Toast.makeText(this@MapsActivity2, "Action clicked", Toast.LENGTH_LONG).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnInfoWindowClickListener { infoView.visibility = View.VISIBLE }

        // Add a marker in Sydney and move the camera
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)

       // val titleStr = getAddress(location)
        val titleStr = "Here, no picture available"
        markerOptions.title(titleStr)
        //markerOptions.

        mMap.addMarker(markerOptions)
    }

    private fun placeMarkerOnMap(location: LatLng, cam: cameraStat) {
        /*
        val markerOptions = MarkerOptions().position(location).title(cam.id)
        val tempMark = mMap.addMarker(markerOptions)
        tempMark.tag = cam*/

        //ENGAGE TURBO KOTLIN ENGINE
        //SET NONSENSE TO 11
        mMap.addMarker(MarkerOptions().position(location).title(cam.id + " Click here for picture, if available")).tag = cam
    }

    private fun setUpMap() {


        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        //My location pip
        mMap.isMyLocationEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        //gets current location
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                //Move camera to current location, and add address to map pip
                placeMarkerOnMap(currentLatLng)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
        JsonTask(this).execute("https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2")

    }
    //No longer used, replaced by cameraStat being placed in the tag of the Marker
   /* private fun fillMarkers(){
        for(tempCam:cameraStat in camArray){

            val lat = tempCam.coordinates.substring(1,tempCam.coordinates.indexOf(",")).toDouble()
            val long = tempCam.coordinates.substring(tempCam.coordinates.indexOf(",")+1,tempCam.coordinates.length-1).toDouble()
            //println("[" + tempCam.coordinates + "]")
            //LatLng(lat,long)
            //println(LatLng(lat,long))
            //println(lat.toString() + " " + long.toString())
            placeMarkerOnMap(LatLng(lat,long), tempCam)
        }
    }*/

    private fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        //println("Start")
        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                return addresses[0].getAddressLine(0)
                /*println(address.getAddressLine(0))
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
                println(addressText)*/
            }
        } catch (e: IOException) {
            Log.e("MapsActivity2", e.localizedMessage)
        }

        return "Address not found"
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        //Toast.makeText(this@MapsActivity2, p0!!.title, Toast.LENGTH_LONG).show()
        /*
        Intent*/
        if(p0!!.tag != null){
            val tempCam = p0!!.tag as cameraStat
            DownloadImageTask(infoView).execute(tempCam.imgUrl)
            //infoView.visibility = View.VISIBLE
        }
        p0!!.snippet = getAddress(p0!!.position)
        //p0!!.showInfoWindow()
        return false
    }

    fun setInvis(view: View){
        infoView.visibility = View.INVISIBLE
    }

    private inner class JsonTask(var mContext: MapsActivity2) : AsyncTask<String, String, String>() {

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
            var jObj: JSONObject = JSONObject(result)
            var coords: JSONArray = jObj.getJSONArray("Features")
            for(i in 0..(coords.length()-1)) {
                val tempJason = JSONObject(coords[i].toString())
                val cameraCount = JSONArray(tempJason.getString("Cameras")).length() - 1

                for(j in 0..cameraCount) {

                    var tempCam: cameraStat = cameraStat()
                    tempCam.coordinates = JSONObject(coords[i].toString()).getString("PointCoordinate")
                    tempCam.id = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Id")
                    tempCam.description = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Description")

                    tempCam.type = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Type")
                    if(tempCam.type.equals("sdot")){
                        tempCam.imgUrl = "http://www.seattle.gov/trafficcams/images/" + JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("ImageUrl")
                    }else{
                        tempCam.imgUrl = "http://images.wsdot.wa.gov/nw/" + JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("ImageUrl")
                    }

                    val lat = tempCam.coordinates.substring(1,tempCam.coordinates.indexOf(",")).toDouble()
                    val long = tempCam.coordinates.substring(tempCam.coordinates.indexOf(",")+1,tempCam.coordinates.length-1).toDouble()
                    placeMarkerOnMap(LatLng(lat,long), tempCam)


                    /*println(tempCam.coordinates)
                    println(tempCam.id)
                    println(tempCam.description)
                    println(tempCam.imgUrl)
                    println(tempCam.type)*/
                }
            }
        }
    }
}
