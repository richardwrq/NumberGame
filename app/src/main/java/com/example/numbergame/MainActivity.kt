package com.example.numbergame

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mFragmensts: Array<Fragment> = arrayOf(GameFragment(), SettingFragment(), WebFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                onTabItemSelected(tab.position)
                //改变Tab 状态
                for (i in 0 until tabLayout.tabCount) {
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        tabLayout.addTab(tabLayout.newTab().setText("组6"))
        tabLayout.addTab(tabLayout.newTab().setText("设置"))
        tabLayout.addTab(tabLayout.newTab().setText("WEB"))

    }

    private fun onTabItemSelected(position: Int) {
        if (position < mFragmensts.size) {
            supportFragmentManager.beginTransaction().replace(R.id.ltFragment,  mFragmensts[position]).commit()
        }
    }
}
