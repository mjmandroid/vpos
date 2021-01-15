package com.tranpos.vpos.utils.usbPrint

import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.entity.TicketTemplete
import com.tranpos.vpos.entity.TicketType
import com.tranpos.vpos.utils.KeyConstrant
import com.transpos.tools.tputils.TPUtils

/**
 *CREATED BY AM
 *ON 2021/1/6
 *DESCRIPTION:
 **/
class FormatTextUtils {
    companion object{
        internal fun getTextContent(text1 : String,text2 : String) : String{
            val ticketSet = TPUtils.getObject(
                    BaseApp.getApplication(),
                    KeyConstrant.KEY_TICKET_TEMPLETE,
                    TicketTemplete::class.java
            ) ?: TicketTemplete()
            var scope = 32
            if(ticketSet.type == TicketType.type_80){
                scope = 48
            }
            var content = ""
            if(UsbPrintUtils.blength(text1+text2) >= scope){
                content = text1 + "\n"
                content += UsbPrintUtils.getSpaceString(scope - UsbPrintUtils.blength(text2)) + text2 + "\n"
            } else {
                content += text1 + text2 + "\n"
            }
            return content
        }
    }
}