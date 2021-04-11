package edu.itesm.pandemia

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var url = "https://disease.sh/v3/covid-19/countries" //"https://gist.githubusercontent.com/rjdc97/58309431360488776376187703fd8f8e/raw/412cae8444682a14daffba104240819efc7cf5ec/db.json"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        cargaDatos()
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



    }

    fun viewTests(view: View){
        for (pais in data){
            mMap.addMarker(MarkerOptions()
                    .position(LatLng(pais.latitude, pais.longitude))
                    .title(pais.nombre)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.test)))
        }
    }
    fun viewDefunciones(view: View){
        for (pais in data){
            mMap.addMarker(MarkerOptions()
                    .position(LatLng(pais.latitude, pais.longitude))
                    .title(pais.nombre)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.death)))
        }
    }
    fun viewCasos(view: View){

        for (pais in data){

            mMap.addMarker(MarkerOptions()
                    .position(LatLng(pais.latitude, pais.longitude))
                    .title(pais.nombre)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cases)))
        }
    }


    private val data = mutableListOf<Pais>()

    fun cargaDatos(){
        val requestQuee = Volley.newRequestQueue(this)
        val peticion = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener {
            val jsonArray = it
            for (i in 0 until jsonArray.length()){
                val pais = jsonArray.getJSONObject(i)
                val nombre = pais.getString("country")
                val countryInfoData = pais.getJSONObject("countryInfo")

                val latitude = countryInfoData.getDouble("lat")
                val longitude = countryInfoData.getDouble("long")
                val tests = pais.getDouble("tests")
                val defunciones = pais.getDouble("deaths")
                val casos = pais.getDouble("cases")

                val paisObject = Pais(nombre, latitude, longitude, tests, defunciones,casos)
                data.add(paisObject)


            }
        },Response.ErrorListener {

        })

        requestQuee.add(peticion)
    }

}