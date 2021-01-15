package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/9/15
 *DESCRIPTION:
 **/
class TicketTemplete {
    var type : Int = TicketType.type_58
    /***显示称重***/
    var show_weight = true
    var top_shop_name = true
    /***单据类型***/
    var top_order_type = true
    /***流水号***/
    var top_serial_num = true
    /***收银员***/
    var top_casher = true
    /***单号***/
    var top_order_no = true
    /***导购***/
    var top_sales = false
    /***销售时间***/
    var top_sale_time = true

    var list_code = true
    var list_goods_name = true
    var list_quantity = true
    var list_single_price = true
    var list_origin_price = true
    /***小计***/
    var list_total = true
    /***商品总数  实付金额***/
    var bottom_receive = true
    /***优惠金额***/
    var bottom_reduce = true
    var bottom_origin_price = true
    var bottom_moling = true
    var bottom_pay_detail = true
    var bottom_address = false
    var bottom_print_time = true
    var bottom_phone = false
    var bottom_memo = false
    var bottom_memo_info = ""
    /***票号条码***/
    var bottom_code = true

    var vip_no = true
    var vip_name = true
    var vip_point = true
    var vip_money = true
    var vip_coupon = true
    var vip_plus = true

    var logoPath = ""
    var QRCodePath = ""
    var show_head_logo = false
    var show_tail_logo = false
    var serialBit = 1

    fun restore(){
        type = TicketType.type_58
        top_shop_name = true
        top_order_type = true
        /***流水号***/
        top_serial_num = true
        /***收银员***/
        top_casher = true
        /***单号***/
        top_order_no = true
        /***导购***/
        top_sales = true
        /***销售时间***/
        top_sale_time = true
        serialBit = 1

        list_code = true
        list_goods_name = true
        list_quantity = true
        list_single_price = true
        list_origin_price = true
        /***小计***/
        list_total = true
        /***商品总数  实付金额***/
        bottom_receive = true
        /***优惠金额***/
        bottom_reduce = true
        bottom_origin_price = true
        bottom_moling = true
        bottom_pay_detail = true
        bottom_address = true
        bottom_print_time = false
        bottom_phone = false
        bottom_memo = false
        /***票号条码***/
        bottom_code = true

        vip_no = true
        vip_name = true
        vip_point = true
        vip_money = true
        vip_coupon = true
        vip_plus = true
        logoPath = ""
        QRCodePath = ""
        bottom_memo_info = ""
        show_head_logo = false
    }
}

interface TicketType{
    companion object{
        const val type_58 = 0
        const val type_76 = 1
        const val type_80 = 2
    }
}