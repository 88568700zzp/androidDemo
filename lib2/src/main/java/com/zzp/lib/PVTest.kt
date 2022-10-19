package com.zzp.lib

/**
 *
 * Created by samzhang on 2021/7/13.
 */
class PVTest{

    private var name:String

    constructor(name:String,index:Int){
        this.name = name
        println("doConstructor")
    }

    init {
        println("doInit")
    }

    fun doJob(){
        /*var call = {
            println("call")
        }
        doFunction(call)

        var ss:Float ?= null
        println(ss.toSafe())*/

    }

    fun Float?.toSafe(): Float{
        return this?:40f
    }


    private fun doFunction(call:()->Unit){
        call.invoke()
    }

    fun doNewClass(){
        println("doNewClass ${this@PVTest}")

        try {
            var teachClass = Class.forName("com.zzp.lib.Teacher")
            var constructor = teachClass.getConstructor(String::class.java)
            var newInstance = constructor.newInstance("zzp")
            (newInstance as Teacher).teach()

            teachClass.constructors.forEach {
                it.takeIf {
                    it.parameterCount == 0
                }?.let {
                    System.out.println(it.parameterCount)
                }
            }
        }catch (e:Exception){
            println(e)
        }
    }
}