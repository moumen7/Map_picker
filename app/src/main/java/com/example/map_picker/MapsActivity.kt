package com.example.map_picker

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.map_picker.Reversegeo.ReverseGeoCoder
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.data.geojson.GeoJsonLayer
import java.io.InputStream
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var layers: MutableMap<String?, GeoJsonLayer?> = mutableMapOf<String?, GeoJsonLayer?>()
    lateinit var reverseGeoCode: ReverseGeoCoder
    var country: String? = ""
    var prevcountry: String? = ""
    var r: InputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //Intialize Map
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Intialize reverse geocoder
        r = resources.openRawResource(R.raw.cities15000)
        reverseGeoCode = ReverseGeoCoder(r, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //get all countries
        val isoCountryCodes = Locale.getISOCountries()

        //Iterate through all countries and preprocess GeoJsonLayer to be added onclick
        for (code in isoCountryCodes) {
            var iso2code: String = code.toLowerCase()
            //Handle corner cases, do is the code for domincain republic and it is a keyword in java so it can't be used as do
            if (iso2code == "do") {
                iso2code = "doo"
            }
            //Remove Israel
            if (iso2code == "il") {
                iso2code = "ps"
            }

            //Check if Geojson file exists
            val test: Int = getResources().getIdentifier(
                    iso2code,
                    "raw", getPackageName()
            )
            //If the file exists. Preprocess the Geojson Layer
            if (test != 0) {

                //Read the file for the country
                val lay: GeoJsonLayer? = GeoJsonLayer(
                        googleMap, getResources().getIdentifier(
                        iso2code,
                        "raw", getPackageName()
                ), this
                )

                //Styling of Layer
                lay?.defaultPolygonStyle?.fillColor = Color.BLACK
                lay?.defaultPolygonStyle?.strokeColor = Color.BLACK
                lay?.defaultPolygonStyle?.strokeWidth = 1F


                //add to a map ---> key is the iso2code, value is the corresponding geojson Layer
                layers.put(
                        iso2code,
                        lay
                )

            }
        }


        googleMap.setOnMapClickListener {
            // reversegeocode the clicked position
            country = reverseGeoCode.nearestPlace(it.latitude, it.longitude).country.toLowerCase()
            // add layer to map
            layers.get(country)?.addLayerToMap()
            layers.get(prevcountry)?.removeLayerFromMap()
            // Show Toast
            Toast.makeText(this, "$country Highlighted" ,Toast.LENGTH_SHORT ).show()
            prevcountry = country
        }
    }


}
