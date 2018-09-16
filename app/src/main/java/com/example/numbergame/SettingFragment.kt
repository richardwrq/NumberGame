package com.example.numbergame

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.numbergame.utils.CxbHttpUtils
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * @author caizeming
 * @email  caizeming@cvte.com
 * @date   2018/9/14
 * @description:
 */
class SettingFragment : Fragment() {

    var isLogin = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sp = activity!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        var userName = sp.getString("userName", "")
        var pwd = sp.getString("pwd", "")
        etUserName.setText(userName)
        etPwd.setText(pwd)
        updateViews()

        btnLogin.setOnClickListener {
            Thread {
                userName = etUserName.text.toString()
                pwd = etPwd.text.toString()
                isLogin = CxbHttpUtils.login(userName, pwd)
                activity!!.runOnUiThread {
                    tvLoginResult.text = CxbHttpUtils.loginResult
                    if (isLogin) {
                        ltLogin.visibility = View.GONE
                        ltCommit.visibility = View.VISIBLE
                        val edit = sp.edit()
                        edit.putString("userName", userName)
                        edit.putString("pwd", pwd)
                        edit.commit()
                    } else {
                        ltLogin.visibility = View.VISIBLE
                        ltCommit.visibility = View.GONE
                    }
                }
            }.start()
        }
        btnCommit.setOnClickListener {
            Thread {
                if (GameFragment.pickedList.size in 1..56) {
                    val params = HashMap<String, String>()
                    params["kenoId"] = "1"
                    GameFragment.pickedList.forEachIndexed { index, groupNumber ->
                        params["cart[$index][playId]"] = "6" //组6
                        params["cart[$index][dtype]"] = "0" //玩法类型
                        params["cart[$index][content]"] = groupNumber.toString()
                        params["cart[$index][isComplex]"] = "true" //是否混合
                        params["cart[$index][pl]"] = "150" //赔率
                        params["cart[$index][money]"] = etMoney.text.toString() //金额
                    }
                    val batchPost = CxbHttpUtils.batchPost(params)
                    activity!!.runOnUiThread {
                        tvCommitResult.text = batchPost.message
                        if (!batchPost.isSuccess &&  CxbHttpUtils.batchPostResult.isEmpty()) {
                            isLogin = false
                            updateViews()
                            Toast.makeText(context, "登录过期，请先登录", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    activity!!.runOnUiThread {
                        tvCommitResult.text = "请返回 组6 筛选号码"
                    }
                }

            }.start()
        }
        etMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                s?.let {
                    try {
                        val toInt = s.toString().toInt()
                        if (toInt < 1) {
                            etMoney.setText("1")
                            Toast.makeText(context, "注额不能小于1", Toast.LENGTH_SHORT).show()
                        } else if (toInt > 100) {
                            etMoney.setText("100")
                            Toast.makeText(context, "注额不能大于100", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun updateViews() {
        if (isLogin) {
            ltLogin.visibility = View.GONE
            ltCommit.visibility = View.VISIBLE
        } else {
            ltLogin.visibility = View.VISIBLE
            ltCommit.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (GameFragment.pickedList.size > 56) {
            tvSelectedNums.text = ""
            return
        }
        var strNumList = "已选号码：\n"
        GameFragment.pickedList.forEachIndexed { index, groupNumber ->
            strNumList += groupNumber.toString() + "  "
        }
        tvSelectedNums.text = strNumList
    }
}