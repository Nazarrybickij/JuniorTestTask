package com.nazarrybickij.juniortesttask.activity


import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nazarrybickij.juniortesttask.databinding.ActivityDetailsBinding
import com.nazarrybickij.juniortesttask.entity.Car


class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var car: Car
    private lateinit var simpleVideoView: VideoView
    private lateinit var mediaControls: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        car = intent.getParcelableExtra<Car>(EXTRA_CAR) as Car
        simpleVideoView = binding.videoView

        mediaControls = MediaController(this)
        mediaControls.setAnchorView(this.simpleVideoView)

        setData()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData() {
        Glide.with(this).load(car.image).into(binding.image)
        binding.nameTxt.text = "${car.carMake} ${car.carModel}"
        binding.yearTxt.text = car.carModelYear.toString()
        binding.descriptionTxt.text = car.description
        setVideo()
    }

    private fun setVideo() {
        simpleVideoView.setMediaController(mediaControls)
        simpleVideoView.setVideoURI(
            Uri.parse(car.video)
        )
        simpleVideoView.requestFocus()
        simpleVideoView.setOnInfoListener { _, p1, _ ->
            if (MediaPlayer.MEDIA_INFO_BUFFERING_END == p1) {
                binding.progressBar.visibility = LinearLayout.GONE
                return@setOnInfoListener true
            }
            if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == p1) {
                binding.progressBar.visibility = LinearLayout.GONE
                return@setOnInfoListener true
            }
            if (MediaPlayer.MEDIA_INFO_BUFFERING_START == p1) {
                binding.progressBar.visibility = LinearLayout.VISIBLE
            }
            return@setOnInfoListener false
        }
        simpleVideoView.setOnPreparedListener { mp -> mp.isLooping = true }
        simpleVideoView.start()
    }
    companion object {
        const val EXTRA_CAR = "EXTRA_CAR"

        @JvmStatic
        fun startActivity(context: Context, car: Car) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            intent.putExtra(EXTRA_CAR, car)
            context.startActivity(intent)
        }
    }
}


