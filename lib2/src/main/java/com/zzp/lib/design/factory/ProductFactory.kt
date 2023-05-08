package com.zzp.lib.design.factory

object ProductFactory :IProductFactory{

    //private constructor()

    override fun getProduct(name:String): Product? {
        if("drag" == name) {
            return DragProduct(12f)
        }else if("toy" == name){
            return ToyProduct()
        }
        return null
    }

    /*companion object{
        private var productFactory:ProductFactory? = null

        fun getInstance():ProductFactory{
            if(productFactory == null){
                synchronized(javaClass){
                    if(productFactory == null)
                        productFactory = ProductFactory()
                }
            }
            return productFactory!!
        }
    }*/

}