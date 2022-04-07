package com.example.demo.ui.adpater

import android.content.Context
import android.content.Intent
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.Communicator
import com.example.demo.MainActivity
import com.example.demo.R
import com.example.demo.WanAndroidApplication
import com.example.demo.databinding.ItemBinding
import com.example.demo.relateddata.model.DataX
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator
import com.example.demo.relateddata.repository.CollectionRepository
import com.example.demo.relateddata.repository.base.BaseResult
import com.example.demo.ui.login.LoginActivity
import com.example.demo.utils.LogUtil
import com.example.demo.utils.LoginUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ItemAdapter(private var articleList: ArrayList<DataX>, private var context: Context,private val adapterItemClickListener: AdapterItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var  title: TextView
    private  lateinit var collectImg : ImageView
    private  lateinit var communicator: Communicator

    companion object{
        @JvmStatic
        val TAG =" ItemAdapter"
    }

    interface AdapterItemClickListener {
        fun onPositionClicked(v: View?, position: Int)
        fun onLongClicked(v: View?, position: Int)
    }

    fun setData(newArticleList: ArrayList<DataX>) {
        articleList= newArticleList
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {
        fun bind(article: DataX) {
            binding.apply {

                title.text = article.title
                shareUser.text =
                    if (article.author.isNotEmpty()) article.author else article.shareUser
                niceDate.text = article.niceShareDate
                chapterName.text = article.chapterName
                if (article.collect) {
                    itemCollect.setImageResource(R.drawable.collect_selected_icon)
                } else {
                    itemCollect.setImageResource(R.drawable.collect_unselected_icon)
                }
                itemCollect.setOnClickListener {
                    if (!LoginUtil.isLogin()) {
                        Toast.makeText(WanAndroidApplication.context, " You should log in first", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            val result = withContext(Dispatchers.IO) {
                                if (article.collect) {
                                    LogUtil.instance.addSth(TAG," 取消收藏")
                                    CollectionRepository().unCollectArticle(article.id)
                                } else {
                                    LogUtil.instance.addSth(TAG," 添加收藏")
                                    CollectionRepository().collectArticle(article.id)
                                }
                            }
                            if (result is BaseResult.Success) {
                                article.collect = if (article.collect) {
                                    itemCollect.setImageResource(R.drawable.collect_unselected_icon)
                                    false
                                } else {
                                    itemCollect.setImageResource(R.drawable.collect_selected_icon)
                                    true
                                }
                            } else if (result is BaseResult.Error) {
                                Toast.makeText(
                                    WanAndroidApplication.context,
                                    result.exception.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                singleItemMC.setOnClickListener(this@ArticleViewHolder)
            }
        }

        override fun onClick(v: View?) {
            adapterItemClickListener.onPositionClicked(v, adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            adapterItemClickListener.onLongClicked(v, adapterPosition)
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //创建ViewHolder实例
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val articleViewHolder: ItemAdapter.ArticleViewHolder = holder as ItemAdapter.ArticleViewHolder
        articleViewHolder.bind(articleList[position])
    }

    override fun getItemCount() = articleList.size

}