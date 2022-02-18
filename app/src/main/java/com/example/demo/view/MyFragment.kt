package com.example.demo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.example.demo.MainActivity
import com.example.demo.R
import com.example.demo.WanAndroidApplication
import com.example.demo.utils.SPUtil
import com.example.demo.constants.Constants


class MyFragment : Fragment() {
    private lateinit var myCollect : LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my, container, false)
        myCollect = view.findViewById(R.id.my_collect)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myCollect.setOnClickListener {

            //点击切换到收藏页面CollectionFragment

            val transaction = (requireActivity() as MainActivity).supportFragmentManager.beginTransaction()
            val collectionFragment = CollectionFragment()
            transaction.replace(R.id.AppLayout, collectionFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }
    }

    override fun onResume() {
        super.onResume()
        val username = SPUtil.get(WanAndroidApplication.context, Constants.USERNAME, "")
        val password = SPUtil.get(WanAndroidApplication.context, Constants.PASSWORD, "")
        if(username !=null && password !=null) {
            username as String
            password as String
            Log.d("MyFragment","username = $username")
            Log.d("MyFragment","pwd = $password")
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                val myUsername: TextView = requireView().findViewById(R.id.My_username)
                Log.d("MyFragment", "username = $username")
                myUsername.text = username

            }
        }

    }

}