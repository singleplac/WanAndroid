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



// 设置下拉进度
        val swipeRefreshView: SwipeRefreshLayout = findViewById(R.id.refresh)
        //下拉刷新颜色
        swipeRefreshView.setColorSchemeColors(Color.rgb(122, 223, 189))

        swipeRefreshView.setOnRefreshListener {
            thread {
                Thread.sleep(2000)
                runOnUiThread {
                    Toast.makeText(this, "Refresh successfully", Toast.LENGTH_SHORT).show()
                    swipeRefreshView.isRefreshing = false
                }

            }

        }


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