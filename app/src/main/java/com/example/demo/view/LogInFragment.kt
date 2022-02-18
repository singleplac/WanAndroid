package com.example.demo.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.demo.MainActivity
import com.example.demo.R
import com.example.demo.WanAndroidApplication
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator
import retrofit2.Call
import retrofit2.Response
import com.example.demo.utils.SPUtil
import com.example.demo.constants.Constants
import com.example.demo.model.LogInModel
import retrofit2.Callback

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var username:EditText
    private lateinit var password:EditText
    private lateinit var register: Button
    private lateinit var login :Button
    private lateinit var appService: AppService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  =  inflater.inflate(R.layout.fragment_log_in, container, false)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        register = view.findViewById(R.id.register)
        login = view.findViewById(R.id.login)
        appService = ServiceCreator.create(AppService::class.java)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        login.setOnClickListener {

            if(isValidate(username,password)){
                appService.logIn(username.text.toString().trim(), password.text.toString().trim()).enqueue(object : Callback<LogInModel>{
                    override fun onResponse(
                        call: Call<LogInModel>,
                        response: Response<LogInModel>
                    ) {
                        if(response.body()?.errorCode ==0) {
//                                保存用户名和密码
                            SPUtil.put(WanAndroidApplication.context,Constants.USERNAME, username)
                            SPUtil.put(WanAndroidApplication.context,Constants.PASSWORD, password)
                            Log.d("LogInFragment","log in successfully")
                        }else{
                            Log.d("LogInFragment","log in failed")
                        }
                    }

                    override fun onFailure(call: Call<LogInModel>, t: Throwable) {
                        Log.d("LogInFragment","log in failed")
                        t.printStackTrace()
                    }


                })
            }

            //点击切换到收藏页面CollectionFragment
            val transaction = (requireActivity() as MainActivity).supportFragmentManager.beginTransaction()
            val collectionFragment = CollectionFragment()
            transaction.replace(R.id.AppLayout, collectionFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }

    }

    private fun isValidate(username: EditText, password: EditText): Boolean {
        var flag = true
        if (TextUtils.isEmpty(username.text.toString())) {
            username.error = "请输入用户名"
            flag = false
        }
        if (TextUtils.isEmpty(password.text.toString())) {
            password.error = "请输入密码"
            flag = false
        }
        return flag
    }

}