package com.example.numbergame

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_game.*

/**
 * @author caizeming
 * @email  caizeming@cvte.com
 * @date   2018/9/14
 * @description:
 */
class GameFragment : Fragment() {

    private val numberList = ArrayList<ArrayList<GroupNumber>>()


    private val topList = (0..9).toList()
    private val leftList = (3..13).toList()
    private val rightList = (14..24).toList()
    private var isExported = false

    companion object {
        @JvmStatic
        val pickedList = ArrayList<GroupNumber>()
        @JvmStatic
        val ROW_COUNT = 8
        @JvmStatic
        val group = mapOf(0 to "#ADD8E6", 5 to "#ADD8E6", 1 to "#6cdd6b", 4 to "#6cdd6b", 6 to "#6cdd6b", 7 to "#6cdd6b", 2 to "#ADD8E6", 3 to "#ADD8E6")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        isExported = false
        rvTop.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvTop.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        val topAdapter = OtherAdapter(rvTop, topList)
        rvTop.adapter = topAdapter
        topAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (isExported) {
                    Toast.makeText(context, "需复位后才能重新筛选", Toast.LENGTH_SHORT).show()
                    return
                }
                numberList.forEach {
                    it.forEach { groupNumber ->
                        if (groupNumber.contain(position)) {
//                            if (groupNumber.isValid()) {
//                                pickedList.add(groupNumber.clone())
//                            }
                            groupNumber.clear()
                        }
                    }
                }
                refresh()
            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        })

        rvLeft.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvLeft.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        val leftAdapter = OtherAdapter(rvLeft, leftList)
        rvLeft.adapter = leftAdapter
        leftAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (isExported) {
                    Toast.makeText(context, "需复位后才能重新筛选", Toast.LENGTH_SHORT).show()
                    return
                }
                numberList.forEach {
                    it.forEach { groupNumber ->
                        if (leftList[position] == groupNumber.sum()) {
//                            if (groupNumber.isValid()) {
//                                pickedList.add(groupNumber.clone())
//                            }
                            groupNumber.clear()
                        }
                    }

                }
                refresh()
            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        })

        rvCenter.layoutManager = GridLayoutManager(context, 8, GridLayoutManager.VERTICAL, false)
        rvCenter.addItemDecoration(MDGridRvDividerDecoration(context))
        val centerAdapter = CenterAdapter(numberList)
        rvCenter.adapter = centerAdapter
        centerAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (isExported) {
                    Toast.makeText(context, "需复位后才能重新筛选", Toast.LENGTH_SHORT).show()
                    return
                }
                val groupNumber = numberList[position / ROW_COUNT][position % ROW_COUNT]
//                if (groupNumber.isValid()) {
//                    pickedList.add(groupNumber.clone())
//                }
                groupNumber.clear()
                refresh()
            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        })

        rvRight.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvRight.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        val rightAdapter = OtherAdapter(rvRight, rightList)
        rvRight.adapter = rightAdapter
        rightAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (isExported) {
                    Toast.makeText(context, "需复位后才能重新筛选", Toast.LENGTH_SHORT).show()
                    return
                }
                numberList.forEach {
                    it.forEach { groupNumber ->
                        if (rightList[position] == groupNumber.sum()) {
//                            if (groupNumber.isValid()) {
//                                pickedList.add(groupNumber.clone())
//                            }
                            groupNumber.clear()
                        }
                    }
                }
                refresh()
            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        })

        initNumber()
        btnExport.setOnClickListener { view ->
            export(view)
        }
        btnReset.setOnClickListener { view ->
            reset(view)
        }
    }

    private fun initNumber() {
        numberList.clear()
        group.forEach { i, _ ->
            addGroupNumber(i)
        }
        refresh()
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
        pickedList.clear()
        initNumber()
        (rvTop.adapter as OtherAdapter).clear()
        (rvLeft.adapter as OtherAdapter).clear()
        (rvRight.adapter as OtherAdapter).clear()

        rvCenter.adapter.notifyDataSetChanged()
        rvTop.adapter.notifyDataSetChanged()
        rvLeft.adapter.notifyDataSetChanged()
        rvRight.adapter.notifyDataSetChanged()
        isExported = false
    }

    fun export(view: View) {
        if (isExported) {
            Toast.makeText(context, "需复位后才能重新筛选", Toast.LENGTH_SHORT).show()
            return
        }
        numberList.forEach {
            it.forEach { groupNumber ->
                if (groupNumber.isValid() && !pickedList.contains(groupNumber)) {
                    pickedList.add(groupNumber)
                }
            }
        }
        pickedList.clearInvalid()
        if (pickedList.isEmpty()) {
            Toast.makeText(context, "您尚未选择号码", Toast.LENGTH_SHORT).show()
            return
        }

        if (pickedList.size > 56 ) {
            Toast.makeText(context, "筛选结果必须小于56", Toast.LENGTH_SHORT).show()
            return
        }
        isExported = true
        val string = pickedList.joinToString(separator = " ") { "" + it.n1 + it.n2 + it.n3 }
        Toast.makeText(context, string + "已锁定号码", Toast.LENGTH_SHORT).show()

    }

    private fun ArrayList<GroupNumber>.clearInvalid() {
        val iterator = iterator()
        while (iterator.hasNext()) {
            if (!iterator.next().isValid()) {
                iterator.remove()
            }
        }
    }

    fun refresh() {
        val groupNumber = numberList.check()
        tv1.text = "" + groupNumber.n1
        tv2.text = "" + groupNumber.n2
        tv3.text = "" + groupNumber.n3
        rvCenter.adapter.notifyDataSetChanged()
    }

}