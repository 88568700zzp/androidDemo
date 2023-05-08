package com.zzp.lib

/**
 *
 * Created by samzhang on 2021/5/7.
 */
open class MainKt: (String) -> Unit {

    companion object{
        fun test():String{
            return "zzp"
        }
    }

    fun main(){
        val lambda = {
                left: Int, right: Int
            ->
            left + right
        }
        println(lambda.invoke(2, 3))
        ddd(){
            println("return unit ${it}")
        }
        val list = ArrayList<(Int,Int)->Int>()
        list.add(lambda)
        list.add(lambda)
        list.add(lambda)
        for(func in list){
            println("class:" + func(2,3))
        }
    }

    private fun example(num1:Int,num2:Int,block:(Int,Int)->Int){
        block.invoke(num1,num2)
    }

    fun example(){
        example (1,2){ left: Int, right: Int
            ->
            left * 2 + right
        }
    }

    fun ddd(block: (String) -> Unit):Unit{
        block.invoke("zzp")
    }

    override fun invoke(p1: String) {
        println("MainKt invoke ${p1}")
    }

}