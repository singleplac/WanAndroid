package com.example.demo.adpater

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demo.R
import com.example.demo.model.DataBanner

class BannerAdapter( private var bannerList: ArrayList<DataBanner>) : RecyclerView.Adapter<BannerAdapter.viewHolder> (){
    private lateinit var image :ImageView
    private lateinit  var view: View
    inner class viewHolder (view: View): RecyclerView.ViewHolder(view){
        var image:ImageView = view.findViewById(R.id.bannerImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): viewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.banner,parent,false)
        image = view.findViewById(R.id.bannerImage)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val bannerUrl = bannerList[position].imagePath
        Glide.with(view.context).load(bannerUrl).into(image)
    }

    override fun getItemCount() = bannerList.size
}