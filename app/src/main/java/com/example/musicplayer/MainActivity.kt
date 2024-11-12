package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLayout()

        // Set up button listeners for player buttons
        binding.shuffleBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Shuffle clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
            startActivity(intent)
        }

        binding.favouriteBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, FavouriteActivity::class.java)
            startActivity(intent)
        }

        binding.playlistBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, PlaylistActivity::class.java)
            startActivity(intent)
        }

        // Request storage permission at runtime

    }

    private fun requestRuntimePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+ use media-specific permissions
            val permissions = mutableListOf<String>(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 13)
        } else {
            // For Android 12 and below, use READ_EXTERNAL_STORAGE
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    13
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "All Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle toggle selection
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun initializeLayout() {

        setTheme(R.style.Theme_MusicPlayer)
        requestRuntimePermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)

        // Initialize drawer toggle
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Disable drawer swipe
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        // Open drawer only when menu button is clicked
        binding.materialToolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(binding.navigationView)

            val musicList = arrayListOf(
                MusicAdapter.MusicItem("Song 1", "Album 1", "3:15", R.mipmap.music_player_icon),
                MusicAdapter.MusicItem("Song 2", "Album 2", "4:22", R.mipmap.music_player_icon),
                MusicAdapter.MusicItem("Song 3", "Album 3", "2:45", R.mipmap.music_player_icon)
            )
            binding.musicRV.setHasFixedSize(true)
            binding.musicRV.setItemViewCacheSize(13)
            binding.musicRV.layoutManager = LinearLayoutManager(this@MainActivity)
            musicAdapter = MusicAdapter(this@MainActivity, musicList)
            binding.musicRV.adapter = musicAdapter
            binding.totalSong.text = "Total Song: ${musicAdapter.itemCount}"
        }
    }
}
