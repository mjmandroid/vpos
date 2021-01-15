package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.PromotionMixRuleDao
import com.tranpos.vpos.entity.PromotionMixRule


/**
 *CREATED BY AM
 *ON 2020/8/25
 *DESCRIPTION:
 **/
object PromotionMixRuleDbManger : BaseDBManager<PromotionMixRule>() {
    override fun getDataBaseDao(): BaseDao<PromotionMixRule> {
        return AppDatabase.getDatabase().promotionMixRuleDao
    }

    override fun deleteAll(): Int {
        val dao : PromotionMixRuleDao = baseDao as PromotionMixRuleDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<PromotionMixRule> {
        val dao : PromotionMixRuleDao = baseDao as PromotionMixRuleDao
        return dao.loadAll()
    }

    fun checkRuleById(promotionId : String) : MutableList<PromotionMixRule>{
        val dao : PromotionMixRuleDao = baseDao as PromotionMixRuleDao
        return dao.checkRuleById(promotionId)
    }
}