package com.tranpos.vpos.core

import com.tranpos.vpos.db.manger.*
import com.tranpos.vpos.entity.*
import com.tranpos.vpos.entity.state.PromotionTypeEnumFlag
import com.tranpos.vpos.utils.DateUtil
import com.tranpos.vpos.utils.MathUtil
import com.tranpos.vpos.utils.UiUtils
import com.tranpos.vpos.utils.StringKotlin
import com.transpos.tools.GsonHelper
import com.transpos.tools.logger.Logger
import java.io.Serializable

/**
 *CREATED BY AM
 *ON 2020/9/21
 *DESCRIPTION:
 **/
class PromotionProcessor {

    companion object{

        fun createMemberPromotion(id : String, member: Member?, product: MultipleQueryProduct){
            member?.let {
                var itemPromotion = PromotionItemManger.getItemPromotion(id, "member")
                if(itemPromotion == null){
                    itemPromotion = PromotionItemManger.createPromotionItem(id, "member")
                    itemPromotion.onlineFlag = 0
                    itemPromotion.promotionType = PromotionTypeEnumFlag.MEMBER_REDUCE
                    itemPromotion.price = product.originPrice.toDouble()
                    itemPromotion.amount = product.originPrice.toDouble() * product.getmAmount()
                    itemPromotion.enabled = 1
                    itemPromotion.bargainPrice = product.vipPriceTemp.toDouble()
                    itemPromotion.amount = StringKotlin.formatPrice2(itemPromotion.price * product.getmAmount())
                    itemPromotion.discountAmount = StringKotlin.formatPrice2((itemPromotion.price - itemPromotion.bargainPrice ) )
                    itemPromotion.receivableAmount = StringKotlin.formatPrice2(itemPromotion.bargainPrice * product.getmAmount())
                    itemPromotion.discountRate = if(itemPromotion.price == 0.0) 0.0 else StringKotlin.formatPrice2((itemPromotion.price - itemPromotion.bargainPrice) / itemPromotion.price)
                    itemPromotion.finishDate = DateUtil.getNowDateStr()
                }
            }

        }

        fun getAllPromotionType() : List<String>{
            return listOf(
                PromotionTypeEnum.PRODUCT_DIRECT_DISCOUNT.removeLast(),
                PromotionTypeEnum.PRODUCT_DIRECT_SPECIAL.removeLast(),
                PromotionTypeEnum.PRODUCT_COUNT_SPECIAL.removeLast(),
                PromotionTypeEnum.PRODUCT_EVEN_SPECIAL.removeLast(),
                PromotionTypeEnum.PRODUCT_ANY_SELECT.removeLast()
            )
        }

        @Synchronized
        private fun loadPromotion(dataSet : MutableList<MultipleQueryProduct>,member: Member?){
            val promotionTicks = mutableListOf<PromotionTicket>()
            promotionTicks.addAll(OrderItemManger.promotionTicks)
            for (ticket in promotionTicks) {
                val rule = PromotionMixRuleDbManger.checkRuleById(ticket.id)
                val categoryInfo = PromotionCategoryDbManger.checkPromotionCategory(ticket.id)
                val brandInfo = PromotionBrandDbManger.checkPromotionBrand(ticket.id)
                val productInfo = PromotionProductDbManger.checkPromotionProduct(ticket.id)
                dataSet.forEachIndexed { index, product ->
                    if(OrderItemManger.getList().isEmpty()) {
                        Logger.e(GsonHelper.toJson(dataSet))
                        UiUtils.showToastShort("数据异常")
                        return
                    }
                    if(index >= OrderItemManger.getList().size){
                        return
                    }
                    if(product.isSingleArgue || product.isSingleDiscount || product.roundBucket != null){
                        return@forEachIndexed
                    }
                    val item = OrderItemManger.getList()[index]
                    createMemberPromotion(item.id,member,product)
                    var itemPromotion = PromotionItemManger.getItemPromotion(item.id, ticket.id)
                    if (itemPromotion == null) {
                        itemPromotion = PromotionItemManger.createPromotionItem(item.id, ticket.id)
                        itemPromotion.onlineFlag = 1
                        itemPromotion.promotionType = PromotionTypeEnumFlag.ONLINE
                        itemPromotion.scheduleId = ticket.scheduleId
                        itemPromotion.scheduleSn = ticket.scheduleSn
                        itemPromotion.promotionId = ticket.id
                        itemPromotion.promotionSn = ticket.sn
                        itemPromotion.promotionMode = ticket.mode
                        itemPromotion.scopeType = ticket.scopeType
                        itemPromotion.promotionPlan = ticket.type
                        itemPromotion.price = item.salePrice
                        itemPromotion.amount = item.amount
                        itemPromotion.limitDate = "${ticket.endDate} ${ticket.endTime}"
                    }
                    when(ticket.type){
                        //商品-直接折扣
                        "p_d_s_zjzk" -> {
                            var discountValue = 100.0
                            if (PromotionProductBlackDbManger.checkBlackProduct(item.specId) == null && productInfo.isNotEmpty()) {
                                itemPromotion.reason = "商品-直接折扣"
                                val info = productInfo.find {
                                        p -> p.specId == item.specId
                                }
                                if(info != null){
                                    discountValue = info.discount
                                    val resultPrice = StringKotlin.formatPrice2(product.originPrice.toDouble() * discountValue / 100)
                                    val msg = ItemPromotionMsg()
                                    msg.id = ticket.id
                                    msg.resultPrice = resultPrice
                                    msg.detail = "商品-直接折扣"
                                    msg.type = PromotionTypeEnum.PRODUCT_DIRECT_DISCOUNT
                                    setPromotionDiscount(itemPromotion,discountValue,item)
                                    msg.discountAmount = itemPromotion.discountAmount
                                    msg.promotionInfo = "折扣"
                                    msg.setDiscountName(discountValue)
                                    product.promotionPriceMap[item.id+ticket.id] = msg
                                }
                            }
                        }
                        //商品直接特价
                        "p_t_s_zjtj" -> {
                            itemPromotion.reason = "特价-商品-直接特价"
                            val info = productInfo.find { p -> p.specId == item.specId }
                            if(info != null){
                                val msg = ItemPromotionMsg()
                                msg.id = ticket.id
                                msg.resultPrice = info.specialPrice
                                msg.detail = "商品-直接特价"
                                msg.type = PromotionTypeEnum.PRODUCT_DIRECT_SPECIAL
                                msg.discountAmount = MathUtil.subtractMoney(product.originPrice.toDouble(),msg.resultPrice)
                                val productMap = dataSet.groupBy {
                                    it.specId
                                }

                                val baseCount = productMap[item.specId]!!.fold(0.0){acc, queryProduct -> acc + queryProduct.getmAmount() }
                                if(baseCount > info.orderNumLimit && info.orderNumLimit > 0.0){
                                    msg.isProductDirectSpecialBeyond = true
                                    msg.productDirectSpecialLimitQuantity = info.orderNumLimit
                                }

                                msg.promotionInfo = "特价"
                                product.promotionPriceMap[item.id+ticket.id] = msg
                                itemPromotion.enabled = 1
                                itemPromotion.bargainPrice = info.specialPrice
                                itemPromotion.amount = StringKotlin.formatPrice2(itemPromotion.price * item.quantity)
                                itemPromotion.discountAmount = msg.discountAmount
                                itemPromotion.receivableAmount = StringKotlin.formatPrice2(itemPromotion.bargainPrice * item.quantity)
                                itemPromotion.discountRate = StringKotlin.formatPrice2((itemPromotion.price - itemPromotion.bargainPrice) / itemPromotion.price)
                                itemPromotion.finishDate = DateUtil.getNowDateStr()
                            }
                        }
                        //商品-件数折
                        "p_t_s_fullquantity" ->{
                            val info = productInfo.find { p -> p.specId == item.specId }
                            if(info != null){
                                val productMap = dataSet.groupBy {
                                    it.specId
                                }
                                val baseCount = productMap[item.specId]!!.fold(0.0){acc, queryProduct -> acc + queryProduct.getmAmount() }
                                if(baseCount >= info.lowerQuantityLimit){
                                    val msg = ItemPromotionMsg()
                                    msg.id = ticket.id
                                    msg.resultPrice = info.specialPrice
                                    msg.detail = "商品-件数特价"
                                    msg.type = PromotionTypeEnum.PRODUCT_COUNT_SPECIAL
                                    if(baseCount > info.lowerQuantityLimit){
                                        msg.isProductCountSpecialBeyond = true
                                        msg.productCountSpecialLimitQuantity = info.lowerQuantityLimit
                                    }
                                    msg.promotionInfo = "特价"
                                    product.promotionPriceMap[item.id+ticket.id] = msg
                                }
                            }
                        }
                        //偶数特价
                        "p_t_s_doubleprice" ->{
                            val info = productInfo.find { p -> p.specId == item.specId }
                            if(info != null){
                                val productMap = dataSet.groupBy {
                                    it.specId
                                }
                                val baseCount = productMap[item.specId]!!.fold(0.0){acc, queryProduct -> acc + queryProduct.getmAmount() }
                                //称重商品不参与偶数特价促销
                                if(baseCount > 1 && product.weightFlag == 0){
                                    val msg = ItemPromotionMsg()
                                    msg.id = ticket.id
                                    msg.resultPrice = info.specialPrice
                                    msg.detail = "商品-偶数特价"
                                    msg.type = PromotionTypeEnum.PRODUCT_EVEN_SPECIAL
                                    msg.evenCount = (baseCount.toInt() / 2).toDouble()
                                    msg.promotionInfo = "偶数特价"
                                    product.promotionPriceMap[item.id+ticket.id] = msg
                                }
                            }
                        }
                        //任意选
                        "p_t_s_ryx" ->{
                            val info = productInfo.find { p -> p.specId == item.specId }
                            if(info != null){
                                val productMap = dataSet.groupBy {
                                    it.specId
                                }
                                val baseCount = productMap[item.specId]!!.fold(0.0){acc, queryProduct -> acc + queryProduct.getmAmount() }
                                if(baseCount >= info.lowerQuantityLimit){
                                    val msg = ItemPromotionMsg()
                                    msg.id = ticket.id
                                    msg.resultPrice = StringKotlin.formatPrice2(info.amount / info.lowerQuantityLimit)
                                    msg.detail = "商品-任意选"
                                    msg.type = PromotionTypeEnum.PRODUCT_ANY_SELECT
                                    if(baseCount > info.lowerQuantityLimit){
                                        msg.isProductAnyBeyond = true
                                        msg.productAnyLimitQuantity = info.lowerQuantityLimit
                                    }
                                    msg.promotionInfo = "任意选"
                                    product.promotionPriceMap[item.id+ticket.id] = msg
                                }
                            }
                        }
                    }
                }
            }
        }

        fun process(dataSet : MutableList<MultipleQueryProduct>,member: Member?){
            loadPromotion(dataSet,member)
            //设置product使用了哪种促销
            dataSet.forEachIndexed { index, product ->
                if(index > OrderItemManger.getList().size - 1){
                    return
                }
                val item = OrderItemManger.getList()[index]
                val promotionList = product.promotionPriceMap.map { it.value }
                val minMsg = promotionList.minBy { it.resultPrice }
                if(minMsg != null){
                    if(member != null){
                        if(minMsg.resultPrice < product.vipPriceTemp.toDouble()){
                            val pList = dataSet.groupBy { it.specId }[product.specId]
                            if(product.weightFlag == 1){
                                product.isWeightHasCalc = true
                                if(minMsg.isProductDirectSpecialBeyond){
                                    val baseCount = dataSet.fold(0.0){acc, queryProduct ->
                                        acc + if((queryProduct.weightFlag == 1)&& queryProduct.specId == product.specId && queryProduct.isWeightHasCalc) queryProduct.getmAmount().toDouble() else 0.0 }
                                    if(baseCount == minMsg.productDirectSpecialLimitQuantity){
                                        product.isCritical = true
                                        product.promotionPrice = "${minMsg.resultPrice}"
                                        product.usePromotionType = minMsg.type
                                        product.salePrice = product.promotionPrice
                                        product.isPromotionFlags = true
                                        product.promotionTypeName = minMsg.promotionInfo
                                    } else if(baseCount > minMsg.productDirectSpecialLimitQuantity){
                                        val critical = dataSet.find { it.isCritical }
                                        if(critical != null){
                                            product.salePrice = product.originPrice
                                        } else {
                                            val po = dataSet.find { (it.weightFlag == 1)&& it.specId == product.specId && it.isNeedSplit }
                                            if(po == null){
                                                product.isNeedSplit = true
                                                product.promotionPrice = "${minMsg.resultPrice}"
                                                product.usePromotionType = minMsg.type
                                                product.salePrice = product.promotionPrice
                                                product.isPromotionFlags = true
                                                product.isCritical = true
                                                product.promotionTypeName = minMsg.promotionInfo
                                                product.surplusCount = (baseCount - minMsg.productDirectSpecialLimitQuantity).toFloat()
                                            }
                                        }
                                    } else {
                                        product.promotionPrice = "${minMsg.resultPrice}"
                                        product.usePromotionType = minMsg.type
                                        product.salePrice = product.promotionPrice
                                        product.isPromotionFlags = true
                                        product.promotionTypeName = minMsg.promotionInfo
                                    }
                                } else {
                                    product.promotionPrice = "${minMsg.resultPrice}"
                                    product.usePromotionType = minMsg.type
                                    product.salePrice = product.promotionPrice
                                    product.isPromotionFlags = true
                                    product.promotionTypeName = minMsg.promotionInfo
                                }
                                return@forEachIndexed
                            }
                            if(pList!!.size == 1){
                                product.promotionPrice = "${minMsg.resultPrice}"
                                product.usePromotionType = minMsg.type
                                product.salePrice = product.promotionPrice
                                product.isPromotionFlags = true
                            } else if(pList.size == 2){
                                /////
                                pList[0].promotionPrice = "${minMsg.resultPrice}"
                                pList[0].usePromotionType = minMsg.type
                                pList[0].salePrice = product.promotionPrice
                                pList[0].isPromotionFlags = true
                                if(minMsg.type == PromotionTypeEnum.PRODUCT_EVEN_SPECIAL){
                                    pList[1].usePromotionType = minMsg.type
                                } else if(member != null){
                                    pList[1].usePromotionType = PromotionTypeEnum.MEMBER
                                }
                            }
                        } else {
                            product.usePromotionType = PromotionTypeEnum.MEMBER
                            product.salePrice = product.vipPriceTemp
                        }
                    } else {
                        val pList = dataSet.groupBy { it.specId }[product.specId]
                        if(product.weightFlag == 1){
                            product.isWeightHasCalc = true
                            if(minMsg.isProductDirectSpecialBeyond){
                                val baseCount = dataSet.fold(0.0){acc, queryProduct ->
                                    acc + if((queryProduct.weightFlag == 1)&& queryProduct.specId == product.specId && queryProduct.isWeightHasCalc) queryProduct.getmAmount().toDouble() else 0.0 }
                                if(baseCount == minMsg.productDirectSpecialLimitQuantity){
                                    product.isCritical = true
                                    product.promotionPrice = "${minMsg.resultPrice}"
                                    product.usePromotionType = minMsg.type
                                    product.salePrice = product.promotionPrice
                                    product.isPromotionFlags = true
                                    product.promotionTypeName = minMsg.promotionInfo
                                } else if(baseCount > minMsg.productDirectSpecialLimitQuantity){
                                    val critical = dataSet.find { it.isCritical }
                                    if(critical != null){
                                        product.salePrice = product.originPrice
                                    } else {
                                        val po = dataSet.find { (it.weightFlag == 1)&& it.specId == product.specId && it.isNeedSplit }
                                        if(po == null){
                                            product.isNeedSplit = true
                                            product.promotionPrice = "${minMsg.resultPrice}"
                                            product.usePromotionType = minMsg.type
                                            product.salePrice = product.promotionPrice
                                            product.isPromotionFlags = true
                                            product.isCritical = true
                                            product.promotionTypeName = minMsg.promotionInfo
                                            product.surplusCount = (baseCount - minMsg.productDirectSpecialLimitQuantity).toFloat()
                                        }
                                    }
                                } else {
                                    product.promotionPrice = "${minMsg.resultPrice}"
                                    product.usePromotionType = minMsg.type
                                    product.salePrice = product.promotionPrice
                                    product.isPromotionFlags = true
                                    product.promotionTypeName = minMsg.promotionInfo
                                }
                            } else {
                                product.promotionPrice = "${minMsg.resultPrice}"
                                product.usePromotionType = minMsg.type
                                product.salePrice = product.promotionPrice
                                product.isPromotionFlags = true
                                product.promotionTypeName = minMsg.promotionInfo
                            }
                            return@forEachIndexed
                        }
                        if(pList!!.size == 1){
                            product.promotionPrice = "${minMsg.resultPrice}"
                            product.usePromotionType = minMsg.type
                            product.salePrice = product.promotionPrice
                            product.isPromotionFlags = true
                        } else if(pList.size == 2){
                            /////
                            pList[0].promotionPrice = "${minMsg.resultPrice}"
                            pList[0].usePromotionType = minMsg.type
                            pList[0].salePrice = pList[0].promotionPrice
                            pList[0].isPromotionFlags = true
                            if(minMsg.type == PromotionTypeEnum.PRODUCT_EVEN_SPECIAL){
                                pList[1].usePromotionType = minMsg.type
                            }
                        }

                    }

                } else {
                    if(member != null){
                        product.usePromotionType = PromotionTypeEnum.MEMBER
                        if(!product.isSingleArgue && !product.isSingleDiscount && product.roundBucket == null)
                            product.salePrice = product.vipPriceTemp
                    }
                }

                if(product.usePromotionType == PromotionTypeEnum.PRODUCT_DIRECT_SPECIAL){
                    if(minMsg==null) return@forEachIndexed
                    if(minMsg.isProductDirectSpecialBeyond){
                        product.isNeedSplit = true
                    } else {
                        val pList = dataSet.filter { !it.isGiftFlags }.groupBy { it.specId }[product.specId]
                        if(pList!!.size == 2){
                            pList[0].isNeedMerge = true
                            pList[1].isNeedMerge = true
                        }
                    }
                    product.promotionTypeName = minMsg.promotionInfo
                } else if(product.usePromotionType == PromotionTypeEnum.MEMBER){
                    if(!product.isSingleArgue && !product.isSingleDiscount && product.roundBucket == null)
                        product.salePrice = product.vipPriceTemp
                    val info = PromotionItemManger.getItemPromotion(item.id, "member")
                    if(info != null){
                        val msg = ItemPromotionMsg()
                        msg.id = "m"
                        msg.resultPrice = info.bargainPrice
                        msg.detail = "会员等级折扣"
                        msg.type = PromotionTypeEnum.MEMBER
                        msg.discountAmount = info.discountAmount
                        msg.promotionInfo = "会员等级折扣"
                        product.promotionPriceMap[item.id+"member"] = msg
                    }
                    val tempList = dataSet.filter { !it.isGiftFlags }.filter { it.usePromotionType == PromotionTypeEnum.MEMBER && it.specId == product.specId }
                    if(tempList.size == 2){
                        tempList[0].isNeedMerge = true
                        tempList[1].isNeedMerge = true
                        Logger.d("member="+ GsonHelper.toJson(dataSet))
                    }
                } else if(product.usePromotionType == PromotionTypeEnum.PRODUCT_DIRECT_DISCOUNT){
                    product.salePrice = product.promotionPrice
                    val tempList = dataSet.filter { !it.isGiftFlags }.filter { it.usePromotionType == PromotionTypeEnum.MEMBER && it.specId == product.specId }
                    if(tempList.size == 2){
                        tempList[0].isNeedMerge = true
                        tempList[1].isNeedMerge = true
                    }
                    product.promotionTypeName = minMsg?.promotionInfo
                } else if(product.usePromotionType == PromotionTypeEnum.PRODUCT_COUNT_SPECIAL){
                    if(minMsg==null) return@forEachIndexed
                    product.salePrice = product.promotionPrice
                    product.promotionTypeName = minMsg.promotionInfo
                } else if(product.usePromotionType == PromotionTypeEnum.PRODUCT_EVEN_SPECIAL){
                    if(minMsg==null) return@forEachIndexed
                    product.promotionTypeName = minMsg.promotionInfo
                } else if(product.usePromotionType == PromotionTypeEnum.PRODUCT_ANY_SELECT){
                    if(minMsg==null) return@forEachIndexed
                    product.promotionTypeName = minMsg.promotionInfo
                }
                else {
                    if(!product.isSingleDiscount && !product.isSingleArgue && product.roundBucket == null)
                    product.salePrice = product.originPrice
                    val tempList = dataSet.filter { !it.isGiftFlags }.filter { it.usePromotionType == PromotionTypeEnum.NONE && it.specId == product.specId }
                    if(tempList.size == 2 ){
                        tempList[0].isNeedMerge = true
                        tempList[1].isNeedMerge = true
                    }
                }

            }

            //merge 5.55
            merge(dataSet)

            //split
            splitDirectSpecial(dataSet,member)

            splitAnySelect(dataSet,member)

            evenApply(dataSet,member)

        }


        private fun merge(dataSet : MutableList<MultipleQueryProduct>){
            val deleteProducts = mutableListOf<MultipleQueryProduct>()
            val mps = dataSet.filter { it.isNeedMerge && (it.weightFlag == 0)}
            val tempList = mutableListOf<MultipleQueryProduct>()
            for (mp in mps) {
                tempList.add(mp)
                val mp2 = dataSet.filter {
                    var exists = false
                    for (queryProduct in tempList) {
                        exists = it === queryProduct
                        if(exists)
                            break
                    }
                    !exists
                }.find { !it.isPromotionFlags && it.isNeedMerge }

                var isExits = false
                deleteProducts.forEach {
                    if(it === mp2)
                        isExits = true
                }
                if(isExits) continue
                if(mp2 != null){
                    mp.setmAmount(mp.getmAmount()+mp2.getmAmount())
                    deleteProducts.add(mp2)
                }
            }

            if(deleteProducts.isNotEmpty()){
                deleteProducts.forEach {
                    var i = -1
                    dataSet.forEachIndexed { index, product -> if(product == it) i = index}
                    if(i != -1){
                        val p = dataSet.removeAt(i)
                        OrderItemManger.createItems(p,OrderItemManger.AddItemState.REMOVE,i,null,OrderItemManger.JoinTypeEnmu.SYSTEMADD)
                    }
                }
            }
        }

        private fun evenApply(dataSet: MutableList<MultipleQueryProduct>, member: Member?) {
            val products = dataSet.filter { it.usePromotionType == PromotionTypeEnum.PRODUCT_EVEN_SPECIAL}
            val copyList = mutableListOf<MultipleQueryProduct>()
            for (product in products) {
                val po = dataSet.find { it.specId == product.specId && !it.isPromotionFlags }
                val oldAmount = dataSet.filter{it.specId == product.specId}.fold(0f){acc, queryProduct -> acc + queryProduct.getmAmount() }
                product.setmAmount(product.usePromotionMsg.evenCount.toFloat())
                if(po != null){
                    po.setmAmount(oldAmount - product.usePromotionMsg.evenCount.toFloat())
                    po.salePrice = if(member != null) po.vipPriceTemp else po.originPrice
                } else{
                    val copy = product.clone() as MultipleQueryProduct
                    copy.isPromotionFlags = false
                    copy.setmAmount(oldAmount - product.usePromotionMsg.evenCount.toFloat())
                    copy.salePrice = if(member != null) copy.vipPriceTemp else copy.originPrice
                    if(copy != null){
                        copy.usePromotionType = PromotionTypeEnum.MEMBER
                        copy!!.promotionPriceMap[copy!!.specId+"copy"] = createMemberMsg(copy!!)
                    }
                    copyList.add(copy)
                }
            }
            if(copyList.isNotEmpty()){
                dataSet.addAll(copyList)
                copyList.forEach {
                    OrderItemManger.createItems(it,OrderItemManger.AddItemState.ADD,0,null,OrderItemManger.JoinTypeEnmu.SYSTEMADD)
                }
            }
        }

        private fun splitAnySelect(dataSet : MutableList<MultipleQueryProduct>,member: Member?){
            val products = dataSet.filter { it.usePromotionType == PromotionTypeEnum.PRODUCT_ANY_SELECT}
            val copyList = mutableListOf<MultipleQueryProduct>()
            for (product in products) {
                if(product.usePromotionMsg != null && product.usePromotionMsg.isProductAnyBeyond){
                    val po = dataSet.find { it.specId == product.specId && !it.isPromotionFlags }
                    val baseCount = dataSet.filter { it.specId == product.specId }.fold(0f){acc, queryProduct -> acc + queryProduct.getmAmount() }
                    if(product.weightFlag == 1){
                        val tempList = mutableListOf<MultipleQueryProduct>()
                        dataSet.forEach {
                            if(it.specId == product.specId){
                                tempList.add(it)
                            }
                        }

                        if(tempList.size == 1){
                            val copy = product.clone() as MultipleQueryProduct
                            copy.isPromotionFlags = false
                            copy.salePrice = if(member != null) product.vipPriceTemp else product.originPrice
                            if(member != null){
                                copy.usePromotionType = PromotionTypeEnum.MEMBER
                                if(copy != null){
                                    copy!!.promotionPriceMap[copy!!.specId+"copy"] = createMemberMsg(copy!!)
                                }
                            }
                            copy.setmAmount(baseCount - product.usePromotionMsg.productAnyLimitQuantity.toFloat())
                            product.setmAmount(product.usePromotionMsg.productAnyLimitQuantity.toFloat())
                            copyList.add(copy)
                        } else if(tempList.size > 1){
                            var t = 0.0
                            var len = 0.0
                            var i = -1
                            val size = tempList.size
                            for (index in 0 until size){
                                val queryProduct = tempList[index]
                                t += queryProduct.getmAmount()
                                if(t == queryProduct.usePromotionMsg.productAnyLimitQuantity){
                                    i = index
                                    break
                                } else if(t > queryProduct.usePromotionMsg.productAnyLimitQuantity){
                                    i = index
                                    len = t - queryProduct.usePromotionMsg.productAnyLimitQuantity
                                    Logger.e("len=$len  $index")
                                    break
                                }
                            }

                            if(len == 0.0 && i != -1){
                                for ( k in i+1 until size){
                                    tempList[k].isPromotionFlags = false
                                    tempList[k].salePrice = if(member != null) tempList[k].vipPriceTemp else tempList[k].originPrice
                                }
                            } else if(len > 0.0){
                                var c = 0f
                                for (k in 0..i){
                                    c += tempList[k].getmAmount()
                                }
                                tempList[i].setmAmount(c - len.toFloat() )
                                for ( k in i+1 until size){
                                    tempList[k].isPromotionFlags = false
                                    tempList[k].salePrice = if(member != null) tempList[k].vipPriceTemp else tempList[k].originPrice
                                }
                                c = 0f
                                for (k in 0 until i){
                                    c += tempList[k].getmAmount()
                                }
                                val copy = tempList[i].clone() as MultipleQueryProduct
                                copy.salePrice = if(member != null) tempList[i].vipPriceTemp else tempList[i].originPrice
                                if(member != null){
                                    copy.usePromotionType = PromotionTypeEnum.MEMBER
                                    if(copy != null){
                                        copy!!.promotionPriceMap[copy!!.specId+"copy"] = createMemberMsg(copy!!)
                                    }
                                }
                                copy.setmAmount((len - c).toFloat())
                                copy.isPromotionFlags = false
                                var isExist = false
                                for (q in copyList) {
                                    if(q.specId == copy.specId){
                                        isExist = true
                                        break
                                    }
                                }
                                if(!isExist)
                                    copyList.add(copy)
                            }
                        }
                    } else{
                        if(po != null){
                            po.setmAmount(baseCount - product.usePromotionMsg.productAnyLimitQuantity.toFloat())
                            po.isPromotionFlags = false
                            po.salePrice = if(member != null) po.vipPriceTemp else po.originPrice
                        } else{
                            val copy = product.clone() as MultipleQueryProduct
                            copy.isPromotionFlags = false
                            copy.salePrice = if(member != null) product.vipPriceTemp else product.originPrice
                            if(member != null){
                                copy.usePromotionType = PromotionTypeEnum.MEMBER
                                if(copy != null){
                                    copy!!.promotionPriceMap[copy!!.specId+"copy"] = createMemberMsg(copy!!)
                                }
                            }
                            copy.setmAmount(baseCount - product.usePromotionMsg.productAnyLimitQuantity.toFloat())
                            product.setmAmount(product.usePromotionMsg.productAnyLimitQuantity.toFloat())
                            copyList.add(copy)
                        }
                    }
                }
            }
            if(copyList.isNotEmpty()){
                dataSet.addAll(copyList)
                copyList.forEach {
                    OrderItemManger.createItems(it,OrderItemManger.AddItemState.ADD,0,null,OrderItemManger.JoinTypeEnmu.SYSTEMADD)
                }
            }
        }

        private fun splitDirectSpecial(dataSet : MutableList<MultipleQueryProduct>,member: Member?){
            val products = dataSet.filter { it.isNeedSplit && it.usePromotionType == PromotionTypeEnum.PRODUCT_DIRECT_SPECIAL}
            val copyList = mutableListOf<MultipleQueryProduct>()
            for (product in products) {
                val limitCount = product.usePromotionMsg.productDirectSpecialLimitQuantity
                val copy = product?.clone() as? MultipleQueryProduct

                if(product.weightFlag == 0){
                    if(copy != null){
                        val po = dataSet.find { it.specId == product.specId && !it.isPromotionFlags }
                        if(po != null){
                            po.setmAmount(product.getmAmount() - limitCount.toFloat()+po.getmAmount())
                        } else {
                            copy.setmAmount(copy.getmAmount() - limitCount.toFloat())
                            copy.isPromotionFlags = false
                            copy.promotionPriceMap.clear()
                            if(member != null){
                                copy.salePrice = copy.vipPriceTemp
                                copy.usePromotionType = PromotionTypeEnum.MEMBER
                                if(copy != null){
                                    copy!!.promotionPriceMap[copy!!.specId+"copy"] = createMemberMsg(copy!!)
                                }
                            } else {
                                copy.salePrice = copy.originPrice
                            }
                            copyList.add(copy)
                        }
                    }
                    product?.setmAmount(limitCount.toFloat())
                } else{
                    copy?.setmAmount(copy.surplusCount)
                    copy?.isPromotionFlags = false
                    copy?.salePrice = if(member != null) copy?.vipPriceTemp else copy?.originPrice
                    if(member != null){
                        copy?.usePromotionType = PromotionTypeEnum.MEMBER
                        if(copy != null){
                            copy!!.promotionPriceMap[copy!!.specId+"copy"] = createMemberMsg(copy!!)
                        }
                    }
                    if(copy != null)
                        copyList.add(copy)
                    product?.setmAmount(product.getmAmount() - product.surplusCount)
                }
            }
            if(copyList.isNotEmpty()){
                dataSet.addAll(copyList)
                copyList.forEach {
                    OrderItemManger.createItems(it,OrderItemManger.AddItemState.ADD,0,null,OrderItemManger.JoinTypeEnmu.SYSTEMADD)
                }
            }
        }

        private fun createMemberMsg(product: MultipleQueryProduct) : ItemPromotionMsg{
            val msg = ItemPromotionMsg()
            msg.type = PromotionTypeEnum.MEMBER
            msg.discountAmount = product.originPrice.toDouble() - product.salePrice.toDouble()
            return msg
        }


        private fun setPromotionDiscount(itemPromotion : PromotionItem, discountValue : Double, item : OrderItem){
            itemPromotion.enabled = 1
            itemPromotion.bargainPrice =
                discountValue * itemPromotion.price / 100
            itemPromotion.amount = itemPromotion.price * item.quantity
            itemPromotion.discountAmount =
                itemPromotion.price * (100 - discountValue) / 100
            itemPromotion.receivableAmount =
                discountValue * itemPromotion.price * item.quantity / 100
            itemPromotion.discountRate = (100 - discountValue) / 100
            itemPromotion.finishDate = DateUtil.getNowDateStr()
        }
    }
}

enum class PromotionTypeEnum(val typeName : String,val info : String = "") : Serializable{
    NONE(""),
    MEMBER("member","会员优惠"),
    PRODUCT_DIRECT_DISCOUNT("p_d_s_zjzk","直接折扣优惠"),
    PRODUCT_DIRECT_SPECIAL("p_t_s_zjtj","直接特价优惠"),
    PRODUCT_COUNT_SPECIAL("p_t_s_fullquantity","件数特价优惠"),
    PRODUCT_EVEN_SPECIAL("p_t_s_doubleprice","偶数特价优惠"),
    PRODUCT_ANY_SELECT("p_t_s_ryx","任意选优惠"),


    SINGLE_PRPDUCT_DISCOUNT("","单品折扣"),
    SINGLE_PRPDUCT_ARGUE("","单品议价"),
    SINGLE_ALL_DISCOUNT("","整单折扣"),
    SINGLE_ALL_BARGIN("","整单议价");
    fun removeLast() : String{
        var target = ""
        if(info.isNotBlank() && info.contains("优惠")){
            target = info.substring(0,info.indexOf("优惠"))
        }
        return target
    }
}

class ItemPromotionMsg : Serializable{
    var id : String = ""
    var resultPrice = 0.0
    var detail = ""
    var promotionInfo = ""
    var discountAmount = 0.0
    var type = PromotionTypeEnum.NONE
    var productDirectSpecialLimitQuantity = -1.0
    var isProductDirectSpecialBeyond = false
    var productCountSpecialLimitQuantity = 0.0
    var isProductCountSpecialBeyond = false
    var evenCount = 0.0
    var isProductAnyBeyond = false
    var productAnyLimitQuantity = 0.0
    var discountName = ""

    fun setDiscountName(value : Double){
        discountName = "(${StringKotlin.formatSmart(value / 10)}折)"
    }
}