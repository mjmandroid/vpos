package com.tranpos.vpos.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

open class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity(),IBaseView{
    lateinit var mContext: FragmentActivity
    lateinit var vm: VM
    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //注意 type.actualTypeArguments[0]=BaseViewModel，type.actualTypeArguments[1]=ViewBinding
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz1 = type.actualTypeArguments[0] as Class<VM>
        vm = ViewModelProvider(this).get(clazz1)

        val clazz2 = type.actualTypeArguments[1] as Class<VB>
        val method = clazz2.getMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as VB

        setContentView(binding.root)

        initView()
        initClick()
        initData()
        initVM()
    }

    protected open fun initData() {

    }

    protected open fun initClick(){

    }

    protected open fun initView() {

    }

    protected open fun initVM(){

    }

    override fun showToast(message: String) {

    }

    override fun hideLoading() {

    }

    override fun showLoading() {

    }

    override fun handleLogin() {

    }

    override fun handleException(throwable: Throwable){


    }


}