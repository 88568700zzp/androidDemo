package com.zzp.lib.design.proxy

class Proxy(val job:IJob):IJob{

    override fun doJob() {
        job.doJob()
    }
}