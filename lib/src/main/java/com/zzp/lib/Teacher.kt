package com.zzp.lib

/**
 *
 * Created by samzhang on 2021/5/13.
 */
class Teacher{

    init {
        println("do init")
    }

    constructor(){
        println("constructor 0")
    }

    constructor(name:String):this(){
        println("constructor 1")
    }

    constructor(name:String,age:Int):this(name){
        println("constructor 2")
    }

    fun teach(){
        println("doTeach")
    }
}