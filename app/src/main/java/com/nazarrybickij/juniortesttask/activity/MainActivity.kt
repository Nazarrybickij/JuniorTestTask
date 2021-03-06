package com.nazarrybickij.juniortesttask.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.nazarrybickij.juniortesttask.adapter.Adapter
import com.nazarrybickij.juniortesttask.entity.Car
import com.nazarrybickij.juniortesttask.viewmodel.MainViewModel
import com.nazarrybickij.juniortesttask.R
import com.nazarrybickij.juniortesttask.databinding.ActivityMainBinding
import com.nazarrybickij.juniortesttask.utils.Resource
import kotlinx.coroutines.InternalCoroutinesApi


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: Adapter
    private lateinit var viewModel: MainViewModel
    private var listAds = mutableListOf<NativeAd>()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        title = "List"
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        MobileAds.initialize(this)
        setRecyclerView()
    }

    @InternalCoroutinesApi
    fun setRecyclerView() {
        val callback = object : Adapter.ClickListener {
            override fun itemClick(it: Car) {
                val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("car", it)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        viewModel.getUsers().observe(this, {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = LinearLayout.VISIBLE
                    binding.recyclerView.visibility = LinearLayout.GONE
                }
                is Resource.Success -> {
                    val cars = it.data
                    adapter = Adapter(cars, callback)
                    binding.recyclerView.adapter = adapter
                    loadNativeAds()
                    binding.progressBar.visibility = LinearLayout.GONE
                    binding.recyclerView.visibility = LinearLayout.VISIBLE
                }
                is Resource.Failed -> {
                    Toast.makeText(this, "I DO NOT KNOW", Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = LinearLayout.GONE
                    binding.recyclerView.visibility = LinearLayout.GONE
                }
                else -> Unit
            }
        })
    }

    private fun loadNativeAds() {
        val builder: AdLoader.Builder = AdLoader.Builder(this, getString(R.string.native_ad))
        val adLoader = builder.forNativeAd {
            listAds.add(it)
            if (listAds.size == 5){
                adapter.adList = listAds
            }
        }.build()
        adLoader.loadAds(AdRequest.Builder().build(),5)
    }
}