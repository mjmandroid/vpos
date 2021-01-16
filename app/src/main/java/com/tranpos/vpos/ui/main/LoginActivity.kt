package com.tranpos.vpos.ui.main

import android.view.View
import com.tranpos.vpos.aop.AopOnclick
import com.tranpos.vpos.base.BaseActivity
import com.tranpos.vpos.databinding.ActivityLoginBinding
import com.tranpos.vpos.utils.KeyConstrant
import com.tranpos.vpos.utils.UiUtils
import com.tranpos.vpos.utils.startActivity
import com.tranpos.vpos.wedget.LoadingDialog
import com.transpos.tools.tputils.TPUtils

/**
 *CREATED BY AM
 *ON 2021/1/16
 *DESCRIPTION:
 **/
class LoginActivity : BaseActivity<LoginModel,ActivityLoginBinding>(), View.OnClickListener {

    private val mLoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun initView() {
        super.initView()
    }

    override fun initClick() {
        super.initClick()
        binding.btnLogin.setOnClickListener(this)
    }

    @AopOnclick
    override fun onClick(v: View?) {
        val userName = binding.etUser.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if(userName.isBlank()){
            UiUtils.showToastLong("请输入用户名")
        } else if(password.isBlank()){
            UiUtils.showToastShort("请输入密码")
        } else {
            login(userName,password)
        }
    }

    override fun showLoading() {
        super.showLoading()
        mLoadingDialog.show()
    }

    override fun hideLoading() {
        super.hideLoading()
        mLoadingDialog.dismiss()
    }

    private fun login(userName: String, password: String) {
        vm.login(userName,password,this){
            worker ->
            TPUtils.putObject(this, KeyConstrant.KEY_WORKER, worker)
            TPUtils.remove(this, KeyConstrant.KEY_MEMBER)
            startActivity<MainActivity>()
        }
    }
}