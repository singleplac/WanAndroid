package com.example.demo.ui.adpater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.Communicator
import com.example.demo.MainActivity
import com.example.demo.R
import com.example.demo.relateddata.model.DataXCollect
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator
import java.util.*
import android.widget.AdapterView.OnItemClickListener
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.demo.relateddata.model.DataModel
import com.example.demo.relateddata.model.DataX


class CollectAdapter(private var newsList: ArrayList<DataXCollect>) : RecyclerView.Adapter<CollectAdapter.ViewHolder>(){
    private lateinit var communicator: Communicator

    private lateinit var  title: TextView
    private lateinit var  shareUser: TextView
    private lateinit var niceData: TextView
    private lateinit var chapterName: TextView
    private  lateinit var collectImg : ImageView

    companion object{
        @JvmStatic
        val TAG = "CollectAdapter"
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //继承RecyclerView.ViewHolder

        var title: TextView = view.findViewById(R.id.title)
        var shareUser: TextView = view.findViewById(R.id.shareUser)
        val niceData: TextView = view.findViewById(R.id.niceDate)
        val chapterName: TextView = view.findViewById(R.id.chapterName)
        val collectImg : ImageView = view.findViewById(R.id.itemCollect)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //创建ViewHolder实例

        //引入单个item的XML

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        val holder = ViewHolder(view)
        title= view.findViewById(R.id.title)

        collectImg = view.findViewById(R.id.itemCollect)

        communicator = parent.context as MainActivity
        //借助activity传递web link
//        communicator = requireContext() as MainActivity
        //点击标题跳转到另一个fragment
        title.setOnClickListener {
            Toast.makeText(parent.context, "${title.text} is clicked", Toast.LENGTH_SHORT)
                .show()

            val item = newsList[holder.adapterPosition]

            //调用接口传参
            communicator.passDataCom(item.link)

        }


        return holder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = newsList[position]
        //设置参数
        holder.title.text = item.title
        holder.shareUser.text = item.author
        holder.chapterName.text = item.chapterName
        holder.niceData.text = item.niceDate
      //收藏列表里的文章必然是被收藏了的
        collectImg.setImageResource(R.drawable.collect_selected_icon)

    }

    override fun getItemCount() = newsList.size


}