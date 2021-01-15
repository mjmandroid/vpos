package com.tranpos.vpos.core

import com.tranpos.vpos.db.manger.WorkerDbManger
import com.tranpos.vpos.entity.Worker
import com.tranpos.vpos.utils.BCrypt


class AuthorizationModel {
    companion object {
        fun authVerify(user: String, pwd: String): Worker? {
            val allWorkers = WorkerDbManger.getInstance().loadAll()
            return allWorkers.find {
                it.no == user && (BCrypt.hashpw(pwd,it.passwd) == it.passwd)
            }
        }
    }
}