package com.example.map_picker

import android.content.res.Resources
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.kml.KmlLayer
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
     var layers:MutableMap<String?,GeoJsonLayer?> = mutableMapOf<String?,GeoJsonLayer?>()
    var layer2: KmlLayer? = null
    private lateinit var mMap: GoogleMap
    private var tv: TextView? = null
    var jsonString: String = ""
    var country: String? = ""
    var current:String? = ""
    val TAG = "MyActivity"
    lateinit var obj:JSONObject
    lateinit var myModel: geojson
    var count:Int? = 0

    //private var mapmodel: MapModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        //mapmodel = ViewModelProviders.of(this).get(MapModel::class.java)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment


        mapFragment.getMapAsync(this)


    }



    override fun onMapReady(googleMap: GoogleMap) {
        val isoCountryCodes = Locale.getISOCountries()
        val markerManager = MarkerManager(googleMap)
        val groundOverlayManager = GroundOverlayManager(googleMap)
        val polygonManager = PolygonManager(googleMap)
        val polylineManager = PolylineManager(googleMap)
        for (code in isoCountryCodes)
        {   if(code.toLowerCase()!="do")
        {
            var result: Boolean
            val test: Int = getResources().getIdentifier(code.toLowerCase(),
                    "raw", getPackageName())
            if (test != 0) {
                Log.i("active", code.toLowerCase())
                val lay: GeoJsonLayer? = GeoJsonLayer(googleMap, getResources().getIdentifier(code.toLowerCase(),
                        "raw", getPackageName()), this,markerManager, polygonManager, polylineManager, groundOverlayManager)

                lay?.defaultPolygonStyle?.strokeColor = Color.BLACK
                lay?.defaultPolygonStyle?.strokeWidth = 2F
                lay?.defaultPolygonStyle?.fillColor = Color.rgb(197, 163, 204)
                layers.put(code.toString().toLowerCase(),
                        lay)
            }
        }
            else
        {
            val lay: GeoJsonLayer? = GeoJsonLayer(googleMap, getResources().getIdentifier("doo",
                    "raw", getPackageName()), this, markerManager, polygonManager, polylineManager, groundOverlayManager)
            lay?.defaultPolygonStyle?.strokeColor = Color.BLACK
            lay?.defaultPolygonStyle?.strokeWidth = 2F
            lay?.defaultPolygonStyle?.fillColor = Color.rgb(197, 163, 204)
            layers.put(code.toString().toLowerCase(),
                    lay)
        }



        }

        try {

            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.mapstyle

                )
            )
            if (!success) {

            }
        } catch (e: Resources.NotFoundException) {

        }


        googleMap.setOnMapClickListener {

            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(it.latitude, it.longitude, 1)
               if(country == addresses[0].countryCode.toLowerCase())
               {
                   layers.get(country)?.removeLayerFromMap()
                   country = ""

               }
              else {


                   layers.get(country)?.removeLayerFromMap()
                   layers.get(addresses[0].countryCode.toLowerCase())?.addLayerToMap()

                   country = addresses[0].countryCode.toLowerCase()
                   layers.get(country)?.setOnFeatureClickListener {
                       feature ->
                       layers.get(country)?.removeLayerFromMap()
                       country = ""
                   }
               }

        }
    }
}