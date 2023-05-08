package com.zzp.lib.design.observe

object Subject {
    private var list = ArrayList<Observer>()

    init {
        list.add(object : Observer{
            override fun onMessage(data: String) {
                println("msg1:${data}")
            }

        })

        list.add(object : Observer{
            override fun onMessage(data: String) {
                println("msg2:${data}")
            }

        })

        list.add(object : Observer{
            override fun onMessage(data: String) {
                println("msg3:${data}")
            }

        })
    }

    fun add(observer:Observer){
        list.add(observer)
    }

    fun delete(observer:Observer){
        list.remove(observer)
    }

    fun notifyData(data:String){
        list.forEach {
            it.onMessage(data)
        }
    }
}