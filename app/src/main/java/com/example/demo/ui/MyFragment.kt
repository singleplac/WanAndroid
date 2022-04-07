package com.example.demo.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.demo.MainActivity
import com.example.demo.MainActivity.Companion.COLLECTION_TAG
import com.example.demo.R
import com.example.demo.relateddata.model.DataModel
import com.example.demo.network.ServiceCreator
import com.example.demo.ui.login.LoginActivity
import com.example.demo.utils.LogUtil
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
           if(LoginUtil.isLogin()) {
               Toast.makeText(requireActivity(),"collection is clicked", Toast.LENGTH_SHORT).show()
               replaceFragment(CollectionFragment(), COLLECTION_TAG)

           }else{
               Toast.makeText(requireActivity(),"Please login first", Toast.LENGTH_SHORT).show()
               requireContext().startActivity(Intent(context, LoginActivity::class.java))

           }

        }
        myAbout.setOnClickListener {
            Toast.makeText(requireActivity(),"Have a nice day!", Toast.LENGTH_SHORT).show()
        }
        myLogout.setOnClickListener {
            if(LoginUtil.isLogin()){
                ServiceCreator.checkWithoutLog().logout().enqueue(object : Callback<DataModel>{
                    override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                        LoginUtil.clearLoginInfo()
                        LoginUtil.setIsLogin(false)
                        myUsername.setText("")

                    }

                    override fun onFailure(call: Call<DataModel>, t: Throwable) {
                        Toast.makeText(requireActivity(),"Logout failed", Toast.LENGTH_SHORT).show()
                    }

                })

//                LoginUtil.clearLoginInfo()
//                myUsername.setText("")

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
     //待提升：展示好看的dialogue而不是toast


    private fun replaceFragment(fragment: Fragment, tag: String) {
        val supportFragmentManager = requireActivity().supportFragmentManager
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            ft.replace(R.id.AppLayout, fragment, tag)
            ft.addToBackStack(tag)
        } else {
            ft.replace(R.id.AppLayout, fragment, tag)
        }
        ft.commit()
    }
}