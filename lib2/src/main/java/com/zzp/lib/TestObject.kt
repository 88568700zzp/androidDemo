package com.zzp.lib

/**
 *
 * Created by samzhang on 2021/7/21.
 */
object TestObject {
    fun zzp(time:Long = 0L){
        var adUnitIdStr = "887612956,b618922f032fe4"
        for(i in 0..100) {
            val adUnitIdSplit = adUnitIdStr.split(",")
            System.out.println(adUnitIdSplit[1])
        }

    }


}

