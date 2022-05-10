package com.example.submissionintermediate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.submissionintermediate.Api.ApiConfig
import com.example.submissionintermediate.databinding.ActivityMapsBinding
import com.example.submissionintermediate.pojo.ListStoryItem
import com.example.submissionintermediate.pojo.ResponseGetAllStory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var token: String = MainActivity.tokenActivity

    val array = ArrayList<ListStoryItem>()


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getLocationLatLng(token)
    }

    fun mapsCreate(){
        val data = array
        data.forEach{
            if(it.lat != null && it.lon != null){
                val coordinate = LatLng(it.lat, it.lon)
                //Toast.makeText(this@MapsActivity, it.lat.toString(), Toast.LENGTH_SHORT).show()
                mMap.addMarker(MarkerOptions().position(coordinate).title(it.name).snippet(it.description))
                val lastLatLng = coordinate

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 1F))

            }
        }
    }







    fun getLocationLatLng(token: String){
        val client: Call<ResponseGetAllStory> = ApiConfig.getApiService().getStoriesLocation("Bearer $token")
        client.enqueue(object: Callback<ResponseGetAllStory>{
            override fun onResponse(
                call: Call<ResponseGetAllStory>,
                response: Response<ResponseGetAllStory>
            ) {
                if(response.isSuccessful) {
                    response.body()?.listStory?.let(array::addAll)
                    mapsCreate()
                }

                response.body()?.listStory?.let { it ->
                    Log.d("COORDINATE", it[0].lat.toString())
                }
            }

            override fun onFailure(call: Call<ResponseGetAllStory>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }



    companion object {
        private const val TAG = "MapsActivity"

    }

}