package com.tranpos.vpos.ui.main

import com.tranpos.vpos.aop.AopOnclick
import com.tranpos.vpos.base.BaseActivity
import com.tranpos.vpos.databinding.ActivityRegisterBinding
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
class RegisterActivity : BaseActivity<RegisterModel,ActivityRegisterBinding>(){

    private val mLoadingDialog : LoadingDialog by lazy {
        LoadingDialog(this)
    }


    override fun initClick() {
        super.initClick()
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    @AopOnclick
    private fun register() {
        binding.etCode.text.toString().apply {
            if(this.isBlank()){
                UiUtils.showToastShort("请输入注册码")
            } else {
                vm.register(this,this@RegisterActivity){
                    TPUtils.putObject(this@RegisterActivity, KeyConstrant.KEY_AUTH_REGISTER, it)
                    UiUtils.showToastShort("恭喜，注册成功，可以开始正常使用了")
                    startActivity<LoginActivity>()
                }
            }
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
}