package com.zzp.lib

class PvKt(val name:String,val age:Int):Cat(){


    companion object{
        fun doJob(){
            PvKt("zzoi",12).test2()
        }
    }

    fun test(lengthFun: (String) -> Int):Int{
        var ss :String? = null
        ss?.length
        println("name:$name age:$age")
        TestObject.zzp(12321321L)


        return lengthFun.invoke(name)
    }

    fun test2(){
        test({input ->
            run {
                println("length:${input.length}")
            }
            input.length
        })
    }
}