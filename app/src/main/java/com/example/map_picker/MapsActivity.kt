package com.example.map_picker

import android.content.res.Resources
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.util.Log.i
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
import com.google.maps.android.data.Layer
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
    lateinit var onclcik:Layer.OnFeatureClickListener

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
        {
        if(code.toLowerCase()!="do")
        {

            var result: Boolean
            val test: Int = getResources().getIdentifier(code.toLowerCase(),
                    "raw", getPackageName())
            if (test != 0) {
                Log.i("active", code.toLowerCase())
                val lay: GeoJsonLayer? = GeoJsonLayer(googleMap, getResources().getIdentifier(code.toLowerCase(),
                        "raw", getPackageName()), this,markerManager, polygonManager, polylineManager, groundOverlayManager)

                lay?.defaultPolygonStyle?.strokeColor = Color.TRANSPARENT
                lay?.defaultPolygonStyle?.strokeWidth = 2F
                lay?.defaultPolygonStyle?.fillColor = Color.TRANSPARENT

                layers.put(code.toString().toLowerCase(),
                        lay)
                layers.get(code.toString().toLowerCase())?.addLayerToMap()
                onclcik = Layer.OnFeatureClickListener {
                    feature ->
                    val begin = System.nanoTime()
                    var a: Int? = layers.get(code.toString().toLowerCase())?.defaultPolygonStyle?.fillColor
                    var b: Int?
                    if(a == Color.TRANSPARENT)
                    {
                        a = Color.parseColor("#f44949")
                        b = Color.BLACK
                    }
                    else
                    {
                        a = Color.TRANSPARENT
                        b = Color.TRANSPARENT
                        country = ""
                    }
                    layers.get(country)?.defaultPolygonStyle?.fillColor = Color.TRANSPARENT
                    layers.get(country)?.defaultPolygonStyle?.strokeColor = Color.TRANSPARENT
                    layers.get(code.toString().toLowerCase())?.defaultPolygonStyle?.fillColor = a
                    layers.get(code.toString().toLowerCase())?.defaultPolygonStyle?.strokeColor = b
                    if(a != Color.TRANSPARENT)
                    country = code.toString().toLowerCase()
                    val end = System.nanoTime()
                    Log.i("Elapsed time in nanoseconds:", (end - begin).toString())


                }
                layers.get(code.toString().toLowerCase())?.setOnFeatureClickListener(onclcik)
            }
        }
            else
        {
            val lay: GeoJsonLayer? = GeoJsonLayer(googleMap, getResources().getIdentifier("doo",
                    "raw", getPackageName()), this, markerManager, polygonManager, polylineManager, groundOverlayManager)
            lay?.defaultPolygonStyle?.strokeColor = Color.BLACK
            lay?.defaultPolygonStyle?.strokeWidth = 2F
            lay?.defaultPolygonStyle?.fillColor = Color.rgb(197, 163, 204)

            layers.put(code.toString().toLowerCase(), lay)
            layers.get(code.toString().toLowerCase())?.setOnFeatureClickListener(onclcik)
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





    }
}