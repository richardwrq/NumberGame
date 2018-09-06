package com.example.numbergame

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val numberList = ArrayList<ArrayList<GroupNumber>>()

    private val topList = (0..9).toList()
    private val leftList = (3..13).toList()
    private val rightList = (14..24).toList()

    companion object {
        @JvmStatic
        val ROW_COUNT = 8
        @JvmStatic
        val group = mapOf(0 to "#ADD8E6", 5 to "#ADD8E6", 1 to "#6cdd6b", 4 to "#6cdd6b", 6 to "#6cdd6b", 7 to "#6cdd6b", 2 to "#ADD8E6", 3 to "#ADD8E6")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {

        rvTop.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTop.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        val topAdapter = OtherAdapter(rvTop, topList)
        rvTop.adapter = topAdapter
        topAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                numberList.forEach {
                    it.forEach { groupNumber ->
                        if (groupNumber.contain(position)) {
                            groupNumber.clear()
                        }
                    }
                }
                numberList.check()
                rvCenter.adapter.notifyDataSetChanged()
            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        })

        rvLeft.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvLeft.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val leftAdapter = OtherAdapter(rvLeft, leftList)
        rvLeft.adapter = leftAdapter
        leftAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                numberList.forEach {
                    it.forEach { groupNumber ->
                        if (leftList[position] == groupNumber?.sum()) {
                            groupNumber.clear()
                        }
                    }

                }
                numberList.check()
                rvCenter.adapter.notifyDataSetChanged()
            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        })

        initNumber()
        rvCenter.layoutManager = GridLayoutManager(this, 8, GridLayoutManager.VERTICAL, false)
        rvCenter.addItemDecoration(MDGridRvDividerDecoration(this))
        val centerAdapter = CenterAdapter(numberList)
        rvCenter.adapter = centerAdapter
        centerAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                numberList[position / ROW_COUNT][position % ROW_COUNT].clear()
                numberList.check()
                rvCenter.adapter.notifyDataSetChanged()
            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        })

        rvRight.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvRight.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val rightAdapter = OtherAdapter(rvRight, rightList)
        rvRight.adapter = rightAdapter
        rightAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                numberList.forEach {
                    it.forEach { groupNumber ->
                        if (rightList[position] == groupNumber.sum()) {
                            groupNumber.clear()
                        }
                    }
                }
                numberList.check()
                rvCenter.adapter.notifyDataSetChanged()
            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        })

    }

    private fun initNumber() {
        numberList.clear()
        group.forEach { i, _ ->
            addGroupNumber(i)
        }
    }

    private fun addGroupNumber(first: Int) {
        val color = group[first] ?: ""
        for (i in (first + 1)..8) {
            val groupList = ArrayList<GroupNumber>(8)
            for (j in (i + 1)..9) {
                val groupNumber = GroupNumber(first, i, j)
                groupNumber.backgroundColor = color
                groupList.add(groupNumber)
            }
            for (k in groupList.size until ROW_COUNT) {
                groupList.add(GroupNumber(0, 0, 0).apply { backgroundColor = color })
            }
            numberList.add(groupList)
        }
    }

    fun reset(view: View) {
        initNumber()
        (rvTop.adapter as OtherAdapter).clear()
        (rvLeft.adapter as OtherAdapter).clear()
        (rvRight.adapter as OtherAdapter).clear()

        rvCenter.adapter.notifyDataSetChanged()
        rvTop.adapter.notifyDataSetChanged()
        rvLeft.adapter.notifyDataSetChanged()
        rvRight.adapter.notifyDataSetChanged()
    }
}
