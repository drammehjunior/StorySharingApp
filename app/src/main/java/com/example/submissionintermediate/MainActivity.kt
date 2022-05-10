package com.example.submissionintermediate

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionintermediate.adapter.StoryListAdapter
import com.example.submissionintermediate.databinding.ActivityMainBinding
import com.example.submissionintermediate.model.MainViewModel
import com.example.submissionintermediate.model.ViewModelFactory
import com.example.submissionintermediate.pojo.ListStoryItem


private var tokenGet: String = ""


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    private val mainViewModel: MainViewModel  by viewModels {
        ViewModelFactory(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkLoggedIn()
//
//        if(intent.getBooleanExtra("reload", false)){
//            mainViewModel.story
//        }

        val preferences = getSharedPreferences(preferenceName, MODE_PRIVATE)
        val name = preferences.getString("name", null)
        binding.welcomeMessage.text = "Selamat Datang ${name}"



        mainViewModel.isLoading.observe(this){ it ->
            showLoading(it)
        }

        mainViewModel.story.observe(this){
            getData(it)
        }


        binding.fabAdd.setOnClickListener { view ->
            if(view.id == R.id.fab_add){
                val intent = Intent(this@MainActivity, CameraActivity::class.java)
                intent.putExtra("token", tokenGet)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()

            }
        }

        binding.fabMaps.setOnClickListener { view ->
            if(view.id == R.id.fab_maps){
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu1 -> {
                logUserOut()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
        finishAffinity()
    }


    @SuppressLint("ApplySharedPref")
    private fun logUserOut(){
        val preferences = getSharedPreferences(preferenceName, MODE_PRIVATE)
        preferences.edit().clear().commit()

        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
        finish()
    }

    private fun checkLoggedIn(){
        val preferences = getSharedPreferences(preferenceName, MODE_PRIVATE)
        val token = preferences.getString("token", null)
        if(token == null){
            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
            finish()
        }else{
            tokenGet = token.toString()
            tokenActivity = token.toString()
            mainViewModel.tokenView = token.toString()
        }
    }


    private fun getData(data: PagingData<ListStoryItem>) {

        if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding.rvStories.layoutManager = GridLayoutManager(this, 2)
        }else{
            binding.rvStories.layoutManager = LinearLayoutManager((this))
        }
        val adapter = StoryListAdapter()
        binding.rvStories.adapter = adapter
        adapter.submitData(lifecycle, data)

        adapter.setOnItemClickCallback(object: StoryListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListStoryItem) {
                val intentToDetails = Intent(this@MainActivity, StoryDetails::class.java)
                intentToDetails.putExtra("name", data.name)
                intentToDetails.putExtra("description", data.description)
                intentToDetails.putExtra("photo", data.photoUrl)
                startActivity(intentToDetails)
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val preferenceName = "myPrefs"
        var tokenActivity = ""
    }
}


