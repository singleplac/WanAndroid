package com.example.demo
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.demo.ui.*
import com.example.demo.ui.base.BaseActivity
import com.example.demo.ui.home.HomeFragment
import com.example.demo.ui.project.ProjectsFragmentViewPage
import com.example.demo.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity(), Communicator {

    companion object {
        const val HOME_TAG = "home"
        const val COMPLETE_PROJECT_TAG = "completeProject"
        const val ME_TAG = "me"
        const val COLLECTION_TAG = "collection"
    }

    private var lastIndex = 0
    private var mFragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        因为BaseActivity已经bindLayout，所以这里不需要再setContentView，否则UI会被顶替掉

    }

    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView(view: View?) {
        initBottomNavigation()
        initData()
    }

    override fun doBusiness(mContext: Context?) {

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
        mFragments.add(HomeFragment()) //ItemFrag is Homepage
        mFragments.add(SearchFragment())
        mFragments.add(ProjectsFragmentViewPage())
        mFragments.add(MyFragment())
//        mFragments.add(CollectionFragment())
        //初始化展示MessageFragment
        setFragmentPosition(0)

    }
    private fun initBottomNavigation() {
        var bottomBar: BottomNavigationView = findViewById(R.id.bottomBar)
        bottomBar.setOnItemSelectedListener{ item ->
        when(item.itemId){
            R.id.tab_menu_home -> setFragmentPosition(0)
            R.id.tab_menu_search -> setFragmentPosition(1)
//            R.id.tab_test_fragment ->setFragmentPosition(2)
            R.id.tab_menu_project ->setFragmentPosition(2)
            R.id.tab_menu_my -> setFragmentPosition(3)
            else -> { }
        }
        true
        }


    }
    private fun setFragmentPosition(position: Int) {
        val fragmentManager = supportFragmentManager.beginTransaction()

        //如果栈顶是collectionFragment，remove collectionFragment （不会写）

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