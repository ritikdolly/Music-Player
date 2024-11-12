package com.example.musicplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.MusicViewBinding

class MusicAdapter(private val context: Context, private val musicList: ArrayList<MusicItem>) : RecyclerView.Adapter<MusicAdapter.MyHolder>() {

    data class MusicItem(val title: String, val album: String, val duration: String, val imageResId: Int)

    class MyHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val image = binding.imageMV
        val duration = binding.songDuration
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val musicItem = musicList[position]
        holder.title.text = musicItem.title
        holder.album.text = musicItem.album
        holder.duration.text = musicItem.duration
        holder.image.setImageResource(musicItem.imageResId)
        
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}
