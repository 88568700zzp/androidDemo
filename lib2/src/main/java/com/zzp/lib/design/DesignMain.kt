package com.zzp.lib.design

import com.zzp.lib.design.decorate.DoctorPerson
import com.zzp.lib.design.factory.DragProduct
import com.zzp.lib.design.factory.Product
import com.zzp.lib.design.factory.ProductFactory
import com.zzp.lib.design.observe.Subject
import com.zzp.lib.design.proxy.DocterJob
import com.zzp.lib.design.proxy.Proxy

class DesignMain {

    fun doMain(){
        /*with(ProductFactory){
            println(getProduct("drag")?.toString())
            println(getProduct("toy")?.toString())
        }*/
        //Proxy(DocterJob()).doJob()
        //DoctorPerson().doFun()
        Subject.notifyData("66666")
    }
}