package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.MemberCardTypeDao
import com.tranpos.vpos.entity.MemberCardType


/**
 *CREATED BY AM
 *ON 2020/10/31
 *DESCRIPTION:
 **/
object MemberCardTypeDbManager : BaseDBManager<MemberCardType>() {
    override fun getDataBaseDao(): BaseDao<MemberCardType> {
        return AppDatabase.getDatabase().memberCardTypeDao
    }

    override fun loadAll(): MutableList<MemberCardType> {
        val dao = baseDao as MemberCardTypeDao
        return dao.loadAll()
    }

    override fun deleteAll(): Int {
        val dao = baseDao as MemberCardTypeDao
        return dao.deleteAll()
    }
}