package com.example.demo.ui.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.ui.adpater.ItemAdapter
import com.example.demo.relateddata.model.DataModel
import com.example.demo.relateddata.model.DataX
import com.example.demo.network.ServiceCreator
import retrofit2.Call
import retrofit2.Response


private const val ARG_PARAM1 = "page"
private const val ARG_PARAM2 = "cid"

class ProjectArticlesFragment2 : Fragment() {
    private var page: Int? = null
    private var cid: Int? = null
    private var articleList = ArrayList<DataX>()

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var articleRecyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.getInt(ARG_PARAM1)
            cid = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_project_articles, container, false)

        //初始化RecyclerView
        articleRecyclerView = view.findViewById(R.id.projectArticle_rv)


        //给recyclerView添加layoutManager
        layoutManager = LinearLayoutManager(activity)
        articleRecyclerView.layoutManager = layoutManager
        //给recyclerView添加adapter
        adapter = ItemAdapter(articleList, requireContext(),object:ItemAdapter.AdapterItemClickListener{
            override fun onPositionClicked(v: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onLongClicked(v: View?, position: Int) {
                TODO("Not yet implemented")
            }

        })
        articleRecyclerView.adapter = adapter


        applyArticles()

        return view
    }



    private fun applyArticles() {
        ServiceCreator.checkWithoutLog().getProjectList(page!!, cid!!).enqueue(object : retrofit2.Callback<DataModel> {
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                var responseData = response.body()
                if(responseData!=null) {
                    articleList.addAll(responseData.data.datas)
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param page page.
         * @param cid category id.
         * @return A new instance of fragment ProjectArticlesFragment.
         */
        @JvmStatic
        fun newInstance(page:Int, cid: Int) =
            ProjectArticlesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, page)
                    putInt(ARG_PARAM2, cid)
                }
            }
    }
}