package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.PromotionTicketDao
import com.tranpos.vpos.entity.PromotionTicket


/**
 *CREATED BY AM
 *ON 2020/8/25
 *DESCRIPTION:
 **/
object PromotionTicketDbManger : BaseDBManager<PromotionTicket>() {
    override fun getDataBaseDao(): BaseDao<PromotionTicket> {
        return AppDatabase.getDatabase().promotionTicketDao
    }

    override fun loadAll(): MutableList<PromotionTicket> {
        val dao : PromotionTicketDao = baseDao as PromotionTicketDao
        return dao.loadAll()
    }

    override fun deleteAll(): Int {
        val dao : PromotionTicketDao = baseDao as PromotionTicketDao
        return dao.deleteAll()
    }
}