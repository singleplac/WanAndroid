package com.example.demo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.demo.MainActivity
import com.example.demo.R
import com.example.demo.WanAndroidApplication.Companion.appService
import com.example.demo.model.DataModel
import com.example.demo.network.AppService
import com.example.demo.utils.LoginUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyFragment : Fragment() {

    private lateinit var myCollect : LinearLayout
    private lateinit var myLogout :LinearLayout
    private lateinit var myAbout : LinearLayout
    private lateinit var myUsername: TextView


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
        myLogout = view.findViewById(R.id.log_out)
        myAbout = view.findViewById(R.id.about_me)

        myUsername= view.findViewById(R.id.My_username)


        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myCollect.setOnClickListener {
            val transaction = (requireActivity() as MainActivity).supportFragmentManager.beginTransaction()
            //点击切换到收藏页面CollectionFragment
            if(LoginUtil.isLogin()){
                val collectionFragment = CollectionFragment()
                transaction.replace(R.id.AppLayout, collectionFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }else{
                Toast.makeText(requireActivity(),"Pls login first", Toast.LENGTH_SHORT).show()
                val logInFragment = LogInFragment()
                transaction.replace(R.id.AppLayout, logInFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

        }
        myAbout.setOnClickListener {
            Toast.makeText(requireActivity(),"Have a nice day!", Toast.LENGTH_SHORT).show()
        }
        myLogout.setOnClickListener {
            if(LoginUtil.isLogin()){
                appService.logout().enqueue(object : Callback<DataModel>{
                    override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                        LoginUtil.clearLoginInfo()
                        myUsername.setText("")

                    }

                    override fun onFailure(call: Call<DataModel>, t: Throwable) {
                        Toast.makeText(requireActivity(),"Logout failed", Toast.LENGTH_SHORT).show()
                    }

                })

            }else{
                Toast.makeText(requireActivity(),"You didn't login at all", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val loginUserName: String = LoginUtil.getLoginUser()
        Log.d("MyFragment","username = $loginUserName")
        myUsername.setText(loginUserName)
    }

}