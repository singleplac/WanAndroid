package com.example.demo.adpater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.Communicator
import com.example.demo.DataX
import com.example.demo.MainActivity
import com.example.demo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ItemAdapter(private var newsList: ArrayList<DataX>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    private lateinit var communicator: Communicator


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //继承RecyclerView.ViewHolder

        var title: TextView = view.findViewById(R.id.title)
        var shareUser: TextView = view.findViewById(R.id.shareUser)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //创建ViewHolder实例

        //引入单个item的XML

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        val holder = ViewHolder(view)
        val title: TextView = view.findViewById(R.id.title)

        communicator = parent.context as MainActivity
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

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        val item = newsList[position]
        //设置参数
        holder.title.text = item.title
        holder.shareUser.text = item.shareUser
    }

    override fun getItemCount() = newsList.size


}