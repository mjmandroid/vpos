package com.tranpos.vpos.db.manger


import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.entity.MultipleQueryProduct
import com.tranpos.vpos.utils.MySQLiteOpenHelper
import com.transpos.tools.logger.Logger


object MultipleQueryProductDbManger {

    fun queryOneProductByCode(barCode : String):MutableList<MultipleQueryProduct>?{
//        var dao = AppDatabase.getDatabase().mutipleQueryProductDao
        var db = AppDatabase.getDatabase().openHelper.readableDatabase
        val sql ="select p.id, \n"+
                " p.tenantId,  \n" +
                " p.categoryId,  \n" +
                " p.categoryPath, \n" +
                " p.type,  \n" +
                " p.no,  \n" +
                " p.barCode,  \n" +
                " p.subNo, \n" +
                " p.otherNo, \n" +
                " p.name,  \n" +
                " p.english,  \n" +
                " p.rem, \n" +
                " p.shortName,  \n" +
                " p.unitId,  \n" +
                " p.brandId,  \n" +
                " p.storageType,  \n" +
                " p.storageAddress,  \n" +
                " sp.supplierId, \n" +
                " sp.managerType,  \n" +
                " p.purchaseControl, \n" +
                " p.purchaserCycle,  \n" +
                " p.validDays,  \n" +
                " p.productArea,  \n" +
                " p.status,  \n" +
                " p.spNum,  \n" +
                " sp.stockFlag,  \n" +
                " p.quickInventoryFlag, \n" +
                " p.posSellFlag, \n" +
                " p .batchStockFlag, \n" +
                " p.weightFlag, \n" +
                " p.weightWay, \n" +
                " p.steelyardCode, \n" +
                " p.labelPrintFlag, \n" +
                " sp.foreDiscount, \n" +
                " sp.foreGift, \n" +
                " p.promotionFlag, \n" +
                " sp.branchPrice, \n" +
                " sp.foreBargain, \n" +
                " p.returnType, \n" +
                " p.returnRate, \n" +
                " sp.pointFlag, \n" +
                " p.pointValue, \n" +
                " p.introduction, \n" +
                " p.purchaseTax, \n" +
                " p.saleTax, \n" +
                " p.lyRate, \n" +
                " p.allCode, \n" +
                " p.deleteFlag, \n" +
                " p.allowEditSup, \n" +
                " p.ext1, \n" +
                " p.ext2, \n" +
                " p.ext3, \n" +
                " p.createUser, \n" +
                " p.createDate, \n" +
                " p.modifyUser, \n" +
                " p.modifyDate, \n" +
                " ps.specification as specName, \n" +
                " ps.id as specId, \n" +
                " pc.name as categoryName, \n" +
                " pc.code as categoryNo, \n" +
                " pu.name as unitName, \n" +
                " pb.name as brandName, \n" +
                " sp.batchPrice, \n" +
                " sp.batchPrice2, \n" +
                " sp.batchPrice3, \n" +
                " sp.batchPrice4, \n" +
                " sp.batchPrice5, \n" +
                " sp.batchPrice6, \n" +
                " sp.batchPrice7, \n" +
                " sp.batchPrice8, \n" +
                " sp.minPrice, \n"  +
                " sp.otherPrice, \n" +
                " sp.postPrice, \n" +
                " sp.purPrice, \n" +
                " sp.salePrice, \n" +
                " sp.vipPrice, \n" +
                " sp.vipPrice2, \n" +
                " sp.vipPrice3, \n" +
                " sp.vipPrice4, \n" +
                " sp.vipPrice5, \n" +
                " ps.isDefault, \n" +
                " ps.purchaseSpec, \n" +
                " su.name as supplierName \n" +
                " from pos_product p  \n" +
                " inner join pos_product_spec ps on p.id = ps.productId  \n" +
                " inner join pos_store_product sp on ps.id = sp.specId  \n" +
                " left join pos_product_unit pu on p.unitId = pu.id  \n" +
                " left join pos_product_category pc on p.categoryId = pc.id  \n" +
                " left join pos_product_brand pb on p.brandId = pb.id  \n" +
                " left join pos_supplier su on sp.supplierId = su.id  \n" +
                " where sp.status in (1) and ps.deleteFlag = 0 and p.barCode='$barCode' and p.posSellFlag= 1 order by p.categoryId, p.barCode;"

//        Log.e("debug",sql)

        var list = MySQLiteOpenHelper.getInstance().query2Entity(MultipleQueryProduct::class.java, db, sql)
        if(list != null && list.size > 0){
            list.forEach {
                it.originPrice = it.salePrice
            }
            return list
        }
        return null
    }

    fun queryOneProductById(specId : String): MultipleQueryProduct?{
//        var dao = AppDatabase.getDatabase().mutipleQueryProductDao
        var db = AppDatabase.getDatabase().openHelper.readableDatabase
        val sql ="select p.id, \n"+
                " p.tenantId,  \n" +
                " p.categoryId,  \n" +
                " p.categoryPath, \n" +
                " p.type,  \n" +
                " p.no,  \n" +
                " p.barCode,  \n" +
                " p.subNo, \n" +
                " p.otherNo, \n" +
                " p.name,  \n" +
                " p.english,  \n" +
                " p.rem, \n" +
                " p.shortName,  \n" +
                " p.unitId,  \n" +
                " p.brandId,  \n" +
                " p.storageType,  \n" +
                " p.storageAddress,  \n" +
                " sp.supplierId, \n" +
                " sp.managerType,  \n" +
                " p.purchaseControl, \n" +
                " p.purchaserCycle,  \n" +
                " p.validDays,  \n" +
                " p.productArea,  \n" +
                " p.status,  \n" +
                " p.spNum,  \n" +
                " sp.stockFlag,  \n" +
                " p.quickInventoryFlag, \n" +
                " p.posSellFlag, \n" +
                " p .batchStockFlag, \n" +
                " p.weightFlag, \n" +
                " p.weightWay, \n" +
                " p.steelyardCode, \n" +
                " p.labelPrintFlag, \n" +
                " sp.foreDiscount, \n" +
                " sp.foreGift, \n" +
                " p.promotionFlag, \n" +
                " sp.branchPrice, \n" +
                " sp.foreBargain, \n" +
                " p.returnType, \n" +
                " p.returnRate, \n" +
                " sp.pointFlag, \n" +
                " p.pointValue, \n" +
                " p.introduction, \n" +
                " p.purchaseTax, \n" +
                " p.saleTax, \n" +
                " p.lyRate, \n" +
                " p.allCode, \n" +
                " p.deleteFlag, \n" +
                " p.allowEditSup, \n" +
                " p.ext1, \n" +
                " p.ext2, \n" +
                " p.ext3, \n" +
                " p.createUser, \n" +
                " p.createDate, \n" +
                " p.modifyUser, \n" +
                " p.modifyDate, \n" +
                " ps.specification as specName, \n" +
                " ps.id as specId, \n" +
                " pc.name as categoryName, \n" +
                " pc.code as categoryNo, \n" +
                " pu.name as unitName, \n" +
                " pb.name as brandName, \n" +
                " sp.batchPrice, \n" +
                " sp.batchPrice2, \n" +
                " sp.batchPrice3, \n" +
                " sp.batchPrice4, \n" +
                " sp.batchPrice5, \n" +
                " sp.batchPrice6, \n" +
                " sp.batchPrice7, \n" +
                " sp.batchPrice8, \n" +
                " sp.minPrice, \n"  +
                " sp.otherPrice, \n" +
                " sp.postPrice, \n" +
                " sp.purPrice, \n" +
                " sp.salePrice, \n" +
                " sp.vipPrice, \n" +
                " sp.vipPrice2, \n" +
                " sp.vipPrice3, \n" +
                " sp.vipPrice4, \n" +
                " sp.vipPrice5, \n" +
                " ps.isDefault, \n" +
                " ps.purchaseSpec, \n" +
                " su.name as supplierName \n" +
                " from pos_product p  \n" +
                " inner join pos_product_spec ps on p.id = ps.productId  \n" +
                " inner join pos_store_product sp on ps.id = sp.specId  \n" +
                " left join pos_product_unit pu on p.unitId = pu.id  \n" +
                " left join pos_product_category pc on p.categoryId = pc.id  \n" +
                " left join pos_product_brand pb on p.brandId = pb.id  \n" +
                " left join pos_supplier su on sp.supplierId = su.id  \n" +
                " where sp.status in (1, 2, 3) and ps.deleteFlag = 0 and ps.id='$specId' and p.posSellFlag= 1 order by p.categoryId, p.barCode;"

//        Log.e("debug",sql)

        val list = MySQLiteOpenHelper.getInstance().query2Entity(MultipleQueryProduct::class.java, db, sql)
        if(list != null && list.size > 0){
            list.first().originPrice = list.first().salePrice
            return list.first()
        }
        return null
    }


    fun queryOneProductBysubCode(barCode : String):MutableList<MultipleQueryProduct>?{
        var db = AppDatabase.getDatabase().openHelper.readableDatabase
        val sql ="select p.id, \n"+
                " p.tenantId,  \n" +
                " p.categoryId,  \n" +
                " p.categoryPath, \n" +
                " p.type,  \n" +
                " p.no,  \n" +
                " p.barCode,  \n" +
                " p.subNo, \n" +
                " p.otherNo, \n" +
                " p.name,  \n" +
                " p.english,  \n" +
                " p.rem, \n" +
                " p.shortName,  \n" +
                " p.unitId,  \n" +
                " p.brandId,  \n" +
                " p.storageType,  \n" +
                " p.storageAddress,  \n" +
                " sp.supplierId, \n" +
                " sp.managerType,  \n" +
                " p.purchaseControl, \n" +
                " p.purchaserCycle,  \n" +
                " p.validDays,  \n" +
                " p.productArea,  \n" +
                " p.status,  \n" +
                " p.spNum,  \n" +
                " sp.stockFlag,  \n" +
                " p.quickInventoryFlag, \n" +
                " p.posSellFlag, \n" +
                " p .batchStockFlag, \n" +
                " p.weightFlag, \n" +
                " p.weightWay, \n" +
                " p.steelyardCode, \n" +
                " p.labelPrintFlag, \n" +
                " sp.foreDiscount, \n" +
                " sp.foreGift, \n" +
                " p.promotionFlag, \n" +
                " sp.branchPrice, \n" +
                " sp.foreBargain, \n" +
                " p.returnType, \n" +
                " p.returnRate, \n" +
                " sp.pointFlag, \n" +
                " p.pointValue, \n" +
                " p.introduction, \n" +
                " p.purchaseTax, \n" +
                " p.saleTax, \n" +
                " p.lyRate, \n" +
                " p.allCode, \n" +
                " p.deleteFlag, \n" +
                " p.allowEditSup, \n" +
                " p.ext1, \n" +
                " p.ext2, \n" +
                " p.ext3, \n" +
                " p.createUser, \n" +
                " p.createDate, \n" +
                " p.modifyUser, \n" +
                " p.modifyDate, \n" +
                " ps.specification as specName, \n" +
                " ps.id as specId, \n" +
                " pc.name as categoryName, \n" +
                " pc.code as categoryNo, \n" +
                " pu.name as unitName, \n" +
                " pb.name as brandName, \n" +
                " sp.batchPrice, \n" +
                " sp.batchPrice2, \n" +
                " sp.batchPrice3, \n" +
                " sp.batchPrice4, \n" +
                " sp.batchPrice5, \n" +
                " sp.batchPrice6, \n" +
                " sp.batchPrice7, \n" +
                " sp.batchPrice8, \n" +
                " sp.minPrice, \n"  +
                " sp.otherPrice, \n" +
                " sp.postPrice, \n" +
                " sp.purPrice, \n" +
                " sp.salePrice, \n" +
                " sp.vipPrice, \n" +
                " sp.vipPrice2, \n" +
                " sp.vipPrice3, \n" +
                " sp.vipPrice4, \n" +
                " sp.vipPrice5, \n" +
                " ps.isDefault, \n" +
                " ps.purchaseSpec, \n" +
                " su.name as supplierName \n" +
                " from pos_product p  \n" +
                " inner join pos_product_spec ps on p.id = ps.productId  \n" +
                " inner join pos_store_product sp on ps.id = sp.specId  \n" +
                " left join pos_product_unit pu on p.unitId = pu.id  \n" +
                " left join pos_product_category pc on p.categoryId = pc.id  \n" +
                " left join pos_product_brand pb on p.brandId = pb.id  \n" +
                " left join pos_supplier su on sp.supplierId = su.id  \n" +
                " where sp.status in (1, 2, 3) and ps.deleteFlag = 0 and p.subNo='$barCode' and p.posSellFlag= 1 order by p.categoryId, p.barCode;"

//        Log.e("debug",sql)

        var list = MySQLiteOpenHelper.getInstance().query2Entity(MultipleQueryProduct::class.java, db, sql)
        if(list != null && list.size > 0){
            return list
        }
        return null
    }

    fun queryOneProductByAllcode(barCode : String):MutableList<MultipleQueryProduct>?{
        val db = AppDatabase.getDatabase().openHelper.readableDatabase
        val sql ="select p.id, \n"+
                " p.tenantId,  \n" +
                " p.categoryId,  \n" +
                " p.categoryPath, \n" +
                " p.type,  \n" +
                " p.no,  \n" +
                " p.barCode,  \n" +
                " p.subNo, \n" +
                " p.otherNo, \n" +
                " p.name,  \n" +
                " p.english,  \n" +
                " p.rem, \n" +
                " p.shortName,  \n" +
                " p.unitId,  \n" +
                " p.brandId,  \n" +
                " p.storageType,  \n" +
                " p.storageAddress,  \n" +
                " sp.supplierId, \n" +
                " sp.managerType,  \n" +
                " p.purchaseControl, \n" +
                " p.purchaserCycle,  \n" +
                " p.validDays,  \n" +
                " p.productArea,  \n" +
                " p.status,  \n" +
                " p.spNum,  \n" +
                " sp.stockFlag,  \n" +
                " p.quickInventoryFlag, \n" +
                " p.posSellFlag, \n" +
                " p .batchStockFlag, \n" +
                " p.weightFlag, \n" +
                " p.weightWay, \n" +
                " p.steelyardCode, \n" +
                " p.labelPrintFlag, \n" +
                " sp.foreDiscount, \n" +
                " sp.foreGift, \n" +
                " p.promotionFlag, \n" +
                " sp.branchPrice, \n" +
                " sp.foreBargain, \n" +
                " p.returnType, \n" +
                " p.returnRate, \n" +
                " sp.pointFlag, \n" +
                " p.pointValue, \n" +
                " p.introduction, \n" +
                " p.purchaseTax, \n" +
                " p.saleTax, \n" +
                " p.lyRate, \n" +
                " p.allCode, \n" +
                " p.deleteFlag, \n" +
                " p.allowEditSup, \n" +
                " p.ext1, \n" +
                " p.ext2, \n" +
                " p.ext3, \n" +
                " p.createUser, \n" +
                " p.createDate, \n" +
                " p.modifyUser, \n" +
                " p.modifyDate, \n" +
                " ps.specification as specName, \n" +
                " ps.id as specId, \n" +
                " pc.name as categoryName, \n" +
                " pc.code as categoryNo, \n" +
                " pu.name as unitName, \n" +
                " pb.name as brandName, \n" +
                " sp.batchPrice, \n" +
                " sp.batchPrice2, \n" +
                " sp.batchPrice3, \n" +
                " sp.batchPrice4, \n" +
                " sp.batchPrice5, \n" +
                " sp.batchPrice6, \n" +
                " sp.batchPrice7, \n" +
                " sp.batchPrice8, \n" +
                " sp.minPrice, \n"  +
                " sp.otherPrice, \n" +
                " sp.postPrice, \n" +
                " sp.purPrice, \n" +
                " sp.salePrice, \n" +
                " sp.vipPrice, \n" +
                " sp.vipPrice2, \n" +
                " sp.vipPrice3, \n" +
                " sp.vipPrice4, \n" +
                " sp.vipPrice5, \n" +
                " ps.isDefault, \n" +
                " ps.purchaseSpec, \n" +
                " su.name as supplierName \n" +
                " from pos_product p  \n" +
                " inner join pos_product_spec ps on p.id = ps.productId  \n" +
                " inner join pos_store_product sp on ps.id = sp.specId  \n" +
                " left join pos_product_unit pu on p.unitId = pu.id  \n" +
                " left join pos_product_category pc on p.categoryId = pc.id  \n" +
                " left join pos_product_brand pb on p.brandId = pb.id  \n" +
                " left join pos_supplier su on sp.supplierId = su.id  \n" +
                " where sp.status in (1, 2, 3) and ps.deleteFlag = 0 and p.allCode and p.posSellFlag= 1 like '%$barCode%' order by p.categoryId, p.barCode;"

//        Log.e("debug",sql)

        var list = MySQLiteOpenHelper.getInstance().query2Entity(MultipleQueryProduct::class.java, db, sql)
        if(list != null && list.size > 0){
            return list
        }
        return null
    }

    fun queryProductsByMnemonic(barCode : String) : MutableList<MultipleQueryProduct>?{
        val db = AppDatabase.getDatabase().openHelper.readableDatabase
        val sql ="select p.id, \n"+
                " p.tenantId,  \n" +
                " p.categoryId,  \n" +
                " p.categoryPath, \n" +
                " p.type,  \n" +
                " p.no,  \n" +
                " p.barCode,  \n" +
                " p.subNo, \n" +
                " p.otherNo, \n" +
                " p.name,  \n" +
                " p.english,  \n" +
                " p.rem, \n" +
                " p.shortName,  \n" +
                " p.unitId,  \n" +
                " p.brandId,  \n" +
                " p.storageType,  \n" +
                " p.storageAddress,  \n" +
                " sp.supplierId, \n" +
                " sp.managerType,  \n" +
                " p.purchaseControl, \n" +
                " p.purchaserCycle,  \n" +
                " p.validDays,  \n" +
                " p.productArea,  \n" +
                " p.status,  \n" +
                " p.spNum,  \n" +
                " sp.stockFlag,  \n" +
                " p.quickInventoryFlag, \n" +
                " p.posSellFlag, \n" +
                " p .batchStockFlag, \n" +
                " p.weightFlag, \n" +
                " p.weightWay, \n" +
                " p.steelyardCode, \n" +
                " p.labelPrintFlag, \n" +
                " sp.foreDiscount, \n" +
                " sp.foreGift, \n" +
                " p.promotionFlag, \n" +
                " sp.branchPrice, \n" +
                " sp.foreBargain, \n" +
                " p.returnType, \n" +
                " p.returnRate, \n" +
                " sp.pointFlag, \n" +
                " p.pointValue, \n" +
                " p.introduction, \n" +
                " p.purchaseTax, \n" +
                " p.saleTax, \n" +
                " p.lyRate, \n" +
                " p.allCode, \n" +
                " p.deleteFlag, \n" +
                " p.allowEditSup, \n" +
                " p.ext1, \n" +
                " p.ext2, \n" +
                " p.ext3, \n" +
                " p.createUser, \n" +
                " p.createDate, \n" +
                " p.modifyUser, \n" +
                " p.modifyDate, \n" +
                " ps.specification as specName, \n" +
                " ps.id as specId, \n" +
                " pc.name as categoryName, \n" +
                " pc.code as categoryNo, \n" +
                " pu.name as unitName, \n" +
                " pb.name as brandName, \n" +
                " sp.batchPrice, \n" +
                " sp.batchPrice2, \n" +
                " sp.batchPrice3, \n" +
                " sp.batchPrice4, \n" +
                " sp.batchPrice5, \n" +
                " sp.batchPrice6, \n" +
                " sp.batchPrice7, \n" +
                " sp.batchPrice8, \n" +
                " sp.minPrice, \n"  +
                " sp.otherPrice, \n" +
                " sp.postPrice, \n" +
                " sp.purPrice, \n" +
                " sp.salePrice, \n" +
                " sp.vipPrice, \n" +
                " sp.vipPrice2, \n" +
                " sp.vipPrice3, \n" +
                " sp.vipPrice4, \n" +
                " sp.vipPrice5, \n" +
                " ps.isDefault, \n" +
                " ps.purchaseSpec, \n" +
                " su.name as supplierName \n" +
                " from pos_product p  \n" +
                " inner join pos_product_spec ps on p.id = ps.productId  \n" +
                " inner join pos_store_product sp on ps.id = sp.specId  \n" +
                " left join pos_product_unit pu on p.unitId = pu.id  \n" +
                " left join pos_product_category pc on p.categoryId = pc.id  \n" +
                " left join pos_product_brand pb on p.brandId = pb.id  \n" +
                " left join pos_supplier su on sp.supplierId = su.id  \n" +
                " where sp.status in (1, 2, 3) and ps.deleteFlag = 0 and p.rem like '%${barCode}%' and p.posSellFlag= 1 order by p.categoryId, p.barCode;"

//        Logger.e(sql)

        var list = MySQLiteOpenHelper.getInstance().query2Entity(MultipleQueryProduct::class.java, db, sql)
        if(list != null && list.size > 0){
            return list
        }
        return null
    }

    fun queryProductsByName(name : String) : MutableList<MultipleQueryProduct>?{
        val db = AppDatabase.getDatabase().openHelper.readableDatabase
        val sql ="select p.id, \n"+
                " p.tenantId,  \n" +
                " p.categoryId,  \n" +
                " p.categoryPath, \n" +
                " p.type,  \n" +
                " p.no,  \n" +
                " p.barCode,  \n" +
                " p.subNo, \n" +
                " p.otherNo, \n" +
                " p.name,  \n" +
                " p.english,  \n" +
                " p.rem, \n" +
                " p.shortName,  \n" +
                " p.unitId,  \n" +
                " p.brandId,  \n" +
                " p.storageType,  \n" +
                " p.storageAddress,  \n" +
                " sp.supplierId, \n" +
                " sp.managerType,  \n" +
                " p.purchaseControl, \n" +
                " p.purchaserCycle,  \n" +
                " p.validDays,  \n" +
                " p.productArea,  \n" +
                " p.status,  \n" +
                " p.spNum,  \n" +
                " sp.stockFlag,  \n" +
                " p.quickInventoryFlag, \n" +
                " p.posSellFlag, \n" +
                " p .batchStockFlag, \n" +
                " p.weightFlag, \n" +
                " p.weightWay, \n" +
                " p.steelyardCode, \n" +
                " p.labelPrintFlag, \n" +
                " sp.foreDiscount, \n" +
                " sp.foreGift, \n" +
                " p.promotionFlag, \n" +
                " sp.branchPrice, \n" +
                " sp.foreBargain, \n" +
                " p.returnType, \n" +
                " p.returnRate, \n" +
                " sp.pointFlag, \n" +
                " p.pointValue, \n" +
                " p.introduction, \n" +
                " p.purchaseTax, \n" +
                " p.saleTax, \n" +
                " p.lyRate, \n" +
                " p.allCode, \n" +
                " p.deleteFlag, \n" +
                " p.allowEditSup, \n" +
                " p.ext1, \n" +
                " p.ext2, \n" +
                " p.ext3, \n" +
                " p.createUser, \n" +
                " p.createDate, \n" +
                " p.modifyUser, \n" +
                " p.modifyDate, \n" +
                " ps.specification as specName, \n" +
                " ps.id as specId, \n" +
                " pc.name as categoryName, \n" +
                " pc.code as categoryNo, \n" +
                " pu.name as unitName, \n" +
                " pb.name as brandName, \n" +
                " sp.batchPrice, \n" +
                " sp.batchPrice2, \n" +
                " sp.batchPrice3, \n" +
                " sp.batchPrice4, \n" +
                " sp.batchPrice5, \n" +
                " sp.batchPrice6, \n" +
                " sp.batchPrice7, \n" +
                " sp.batchPrice8, \n" +
                " sp.minPrice, \n"  +
                " sp.otherPrice, \n" +
                " sp.postPrice, \n" +
                " sp.purPrice, \n" +
                " sp.salePrice, \n" +
                " sp.vipPrice, \n" +
                " sp.vipPrice2, \n" +
                " sp.vipPrice3, \n" +
                " sp.vipPrice4, \n" +
                " sp.vipPrice5, \n" +
                " ps.isDefault, \n" +
                " ps.purchaseSpec, \n" +
                " su.name as supplierName \n" +
                " from pos_product p  \n" +
                " inner join pos_product_spec ps on p.id = ps.productId  \n" +
                " inner join pos_store_product sp on ps.id = sp.specId  \n" +
                " left join pos_product_unit pu on p.unitId = pu.id  \n" +
                " left join pos_product_category pc on p.categoryId = pc.id  \n" +
                " left join pos_product_brand pb on p.brandId = pb.id  \n" +
                " left join pos_supplier su on sp.supplierId = su.id  \n" +
                " where sp.status in (1, 2, 3) and ps.deleteFlag = 0 and p.name = '${name}' and p.posSellFlag= 1 order by p.categoryId, p.barCode;"

        val list = MySQLiteOpenHelper.getInstance().query2Entity(MultipleQueryProduct::class.java, db, sql)
        if(list != null && list.size > 0){
            return list
        }
        return null
    }
    /**
     * @description:    根据分类id查询
     * @param:
     * @return:
     * @author:         PC-202001
     * @time:           2020/10/14 18:16
     */
    fun queryProductsByCategory(categoryId : String) : MutableList<MultipleQueryProduct>{
        val db = AppDatabase.getDatabase().openHelper.readableDatabase
        val sql ="select p.id, \n"+
                " p.tenantId,  \n" +
                " p.categoryId,  \n" +
                " p.categoryPath, \n" +
                " p.type,  \n" +
                " p.no,  \n" +
                " p.barCode,  \n" +
                " p.subNo, \n" +
                " p.otherNo, \n" +
                " p.name,  \n" +
                " p.english,  \n" +
                " p.rem, \n" +
                " p.shortName,  \n" +
                " p.unitId,  \n" +
                " p.brandId,  \n" +
                " p.storageType,  \n" +
                " p.storageAddress,  \n" +
                " sp.supplierId, \n" +
                " sp.managerType,  \n" +
                " p.purchaseControl, \n" +
                " p.purchaserCycle,  \n" +
                " p.validDays,  \n" +
                " p.productArea,  \n" +
                " p.status,  \n" +
                " p.spNum,  \n" +
                " sp.stockFlag,  \n" +
                " p.quickInventoryFlag, \n" +
                " p.posSellFlag, \n" +
                " p .batchStockFlag, \n" +
                " p.weightFlag, \n" +
                " p.weightWay, \n" +
                " p.steelyardCode, \n" +
                " p.labelPrintFlag, \n" +
                " sp.foreDiscount, \n" +
                " sp.foreGift, \n" +
                " p.promotionFlag, \n" +
                " sp.branchPrice, \n" +
                " sp.foreBargain, \n" +
                " p.returnType, \n" +
                " p.returnRate, \n" +
                " sp.pointFlag, \n" +
                " p.pointValue, \n" +
                " p.introduction, \n" +
                " p.purchaseTax, \n" +
                " p.saleTax, \n" +
                " p.lyRate, \n" +
                " p.allCode, \n" +
                " p.deleteFlag, \n" +
                " p.allowEditSup, \n" +
                " p.ext1, \n" +
                " p.ext2, \n" +
                " p.ext3, \n" +
                " p.createUser, \n" +
                " p.createDate, \n" +
                " p.modifyUser, \n" +
                " p.modifyDate, \n" +
                " ps.specification as specName, \n" +
                " ps.id as specId, \n" +
                " pc.name as categoryName, \n" +
                " pc.code as categoryNo, \n" +
                " pu.name as unitName, \n" +
                " pb.name as brandName, \n" +
                " sp.batchPrice, \n" +
                " sp.batchPrice2, \n" +
                " sp.batchPrice3, \n" +
                " sp.batchPrice4, \n" +
                " sp.batchPrice5, \n" +
                " sp.batchPrice6, \n" +
                " sp.batchPrice7, \n" +
                " sp.batchPrice8, \n" +
                " sp.minPrice, \n"  +
                " sp.otherPrice, \n" +
                " sp.postPrice, \n" +
                " sp.purPrice, \n" +
                " sp.salePrice, \n" +
                " sp.vipPrice, \n" +
                " sp.vipPrice2, \n" +
                " sp.vipPrice3, \n" +
                " sp.vipPrice4, \n" +
                " sp.vipPrice5, \n" +
                " ps.isDefault, \n" +
                " ps.purchaseSpec, \n" +
                " su.name as supplierName \n" +
                " from pos_product p  \n" +
                " inner join pos_product_spec ps on p.id = ps.productId  \n" +
                " inner join pos_store_product sp on ps.id = sp.specId  \n" +
                " left join pos_product_unit pu on p.unitId = pu.id  \n" +
                " left join pos_product_category pc on p.categoryId = pc.id  \n" +
                " left join pos_product_brand pb on p.brandId = pb.id  \n" +
                " left join pos_supplier su on sp.supplierId = su.id  \n" +
                " where sp.status in (1, 2, 3) and ps.deleteFlag = 0 and p.categoryId = '${categoryId}' and p.posSellFlag= 1 order by p.categoryId, p.barCode;"
        return MySQLiteOpenHelper.getInstance().query2Entity(MultipleQueryProduct::class.java, db, sql)
    }
    /**
     * @description:    
     * @param:          加载所有的快速盘点商品
     * @return:         
     * @author:         PC-202001
     * @time:           2020/12/4 12:28
     */
    fun loadAllQuickCheck() : List<MultipleQueryProduct>{
        val db = AppDatabase.getDatabase().openHelper.readableDatabase
        val sql ="select p.id, \n"+
                " p.tenantId,  \n" +
                " p.categoryId,  \n" +
                " p.categoryPath, \n" +
                " p.type,  \n" +
                " p.no,  \n" +
                " p.barCode,  \n" +
                " p.subNo, \n" +
                " p.otherNo, \n" +
                " p.name,  \n" +
                " p.english,  \n" +
                " p.rem, \n" +
                " p.shortName,  \n" +
                " p.unitId,  \n" +
                " p.brandId,  \n" +
                " p.storageType,  \n" +
                " p.storageAddress,  \n" +
                " sp.supplierId, \n" +
                " sp.managerType,  \n" +
                " p.purchaseControl, \n" +
                " p.purchaserCycle,  \n" +
                " p.validDays,  \n" +
                " p.productArea,  \n" +
                " p.status,  \n" +
                " p.spNum,  \n" +
                " sp.stockFlag,  \n" +
                " p.quickInventoryFlag, \n" +
                " p.posSellFlag, \n" +
                " p .batchStockFlag, \n" +
                " p.weightFlag, \n" +
                " p.weightWay, \n" +
                " p.steelyardCode, \n" +
                " p.labelPrintFlag, \n" +
                " sp.foreDiscount, \n" +
                " sp.foreGift, \n" +
                " p.promotionFlag, \n" +
                " sp.branchPrice, \n" +
                " sp.foreBargain, \n" +
                " p.returnType, \n" +
                " p.returnRate, \n" +
                " sp.pointFlag, \n" +
                " p.pointValue, \n" +
                " p.introduction, \n" +
                " p.purchaseTax, \n" +
                " p.saleTax, \n" +
                " p.lyRate, \n" +
                " p.allCode, \n" +
                " p.deleteFlag, \n" +
                " p.allowEditSup, \n" +
                " p.ext1, \n" +
                " p.ext2, \n" +
                " p.ext3, \n" +
                " p.createUser, \n" +
                " p.createDate, \n" +
                " p.modifyUser, \n" +
                " p.modifyDate, \n" +
                " ps.specification as specName, \n" +
                " ps.id as specId, \n" +
                " pc.name as categoryName, \n" +
                " pc.code as categoryNo, \n" +
                " pu.name as unitName, \n" +
                " pb.name as brandName, \n" +
                " sp.batchPrice, \n" +
                " sp.batchPrice2, \n" +
                " sp.batchPrice3, \n" +
                " sp.batchPrice4, \n" +
                " sp.batchPrice5, \n" +
                " sp.batchPrice6, \n" +
                " sp.batchPrice7, \n" +
                " sp.batchPrice8, \n" +
                " sp.minPrice, \n"  +
                " sp.otherPrice, \n" +
                " sp.postPrice, \n" +
                " sp.purPrice, \n" +
                " sp.salePrice, \n" +
                " sp.vipPrice, \n" +
                " sp.vipPrice2, \n" +
                " sp.vipPrice3, \n" +
                " sp.vipPrice4, \n" +
                " sp.vipPrice5, \n" +
                " ps.isDefault, \n" +
                " ps.purchaseSpec, \n" +
                " su.name as supplierName \n" +
                " from pos_product p  \n" +
                " inner join pos_product_spec ps on p.id = ps.productId  \n" +
                " inner join pos_store_product sp on ps.id = sp.specId  \n" +
                " left join pos_product_unit pu on p.unitId = pu.id  \n" +
                " left join pos_product_category pc on p.categoryId = pc.id  \n" +
                " left join pos_product_brand pb on p.brandId = pb.id  \n" +
                " left join pos_supplier su on sp.supplierId = su.id  \n" +
                " where sp.status in (1, 2, 3) and ps.deleteFlag = 0 and p.quickInventoryFlag = 1 and p.posSellFlag= 1 order by p.categoryId, p.barCode;"
        return MySQLiteOpenHelper.getInstance().query2Entity(MultipleQueryProduct::class.java, db, sql)
    }
}