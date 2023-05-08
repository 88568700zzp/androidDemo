package com.zzp.lib.design.decorate

open class Person {
    fun doFun(){
        System.out.println(getMessage())
    }

    open fun getMessage():String{
        return "i am person"
    }
}