package com.zzp.lib.design.factory

interface IProductFactory {
    fun getProduct(name:String):Product?
}

