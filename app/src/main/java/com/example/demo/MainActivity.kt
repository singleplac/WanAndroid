package com.example.demo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), Communicator {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val itemFrag = ItemFrag()
        supportFragmentManager.beginTransaction().replace(R.id.AppLayout, itemFrag).commit()
        

    }

    override fun passDataCom(editTextInput: String) {

        val bundle = Bundle()
        bundle.putString("link", editTextInput)

        val transaction = this.supportFragmentManager.beginTransaction()

        val webFrag = webFrag()
        webFrag.arguments = bundle

        transaction.replace(R.id.AppLayout, webFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}