package com.tranpos.vpos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.tranpos.vpos.entity.RegistrationCode
import com.tranpos.vpos.foundation.Global
import com.tranpos.vpos.ui.main.LoginActivity
import com.tranpos.vpos.ui.main.RegisterActivity
import com.tranpos.vpos.utils.KeyConstrant
import com.tranpos.vpos.utils.UiUtils
import com.tranpos.vpos.utils.startActivity
import com.transpos.sale.thread.ThreadDispatcher
import com.transpos.tools.tputils.TPUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermission()) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    ), Global.REQUEST_CODE
            )
        } else {
            init()
        }
    }

    private fun init(){
        ThreadDispatcher.getDispatcher().postOnMainDelayed({
            if(TPUtils.getObject(this, KeyConstrant.KEY_AUTH_REGISTER,RegistrationCode::class.java) != null){
                startActivity<LoginActivity>()
            } else {
                startActivity<RegisterActivity>()
            }
        },400)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            Global.REQUEST_CODE -> {
                if(grantResults.isNotEmpty()){
                    var isAllGranted  = true
                    for (result in grantResults) {
                        if(result != PackageManager.PERMISSION_GRANTED){
                            isAllGranted  = false
                            break
                        }
                    }
                    if(!isAllGranted){
                        UiUtils.showToastShort("你拒绝了权限")
                    } else {
                        init()
                    }
                } else {
                    UiUtils.showToastShort("你拒绝了权限")
                }
            }
        }
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val store = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission_group.STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            val camera = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission_group.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            store && camera
        }

        else
            true
    }
}