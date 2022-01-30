package com.example.demo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), Communicator {

    private var lastIndex = 0
    private var mFragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottomNavigation()
        initData()
    }

    override fun passDataCom(editTextInput: String) {

        val bundle = Bundle()
        bundle.putString("link", editTextInput)

        val transaction = this.supportFragmentManager.beginTransaction()

        val webFrag = WebFragment()
        webFrag.arguments = bundle
        transaction.replace(R.id.AppLayout, webFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun initData() {
        mFragments = ArrayList()
        mFragments.add(ItemFragment()) //ItemFrag is Homepage
        mFragments.add(SearchFragment())
        mFragments.add(MyFragment())
        //初始化展示MessageFragment
        setFragmentPosition(0)

    }
    private fun initBottomNavigation() {
        var bottomBar: BottomNavigationView = findViewById(R.id.bottomBar)
        bottomBar.setOnItemSelectedListener{ item ->
        when(item.itemId){
            R.id.tab_menu_home -> setFragmentPosition(0)
            R.id.tab_menu_search -> setFragmentPosition(1)
            R.id.tab_menu_my -> setFragmentPosition(2)
            else -> { }
        }
        true
        }


    }
    private fun setFragmentPosition(position: Int) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        val currentFragment = mFragments[position]
        val lastFragment = mFragments[lastIndex]
        lastIndex = position
        fragmentManager.hide(lastFragment)
        if(!currentFragment.isAdded) {
            supportFragmentManager.beginTransaction().remove(currentFragment).commit()
            fragmentManager.add(R.id.AppLayout, currentFragment)
        }
        fragmentManager.show(currentFragment)
        fragmentManager.commitAllowingStateLoss()
    }


}