package com.zzp.lib.design.factory

open class Product(val name:String,val price:Float){
    override fun toString(): String {
        return "Product(name='$name', price=$price)"
    }
}