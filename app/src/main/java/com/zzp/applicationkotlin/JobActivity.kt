package com.zzp.applicationkotlin

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.zzp.applicationkotlin.service.JobSchedulerService
import kotlinx.android.synthetic.main.activity_job.*
import java.util.concurrent.TimeUnit


class JobActivity : AppCompatActivity() {

    private val TAG = "JobActivity"

    private var mJobIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)

        job.setOnClickListener {
            val builder = JobInfo.Builder(
                mJobIndex++, ComponentName(
                    this,
                    JobSchedulerService::class.java
                )
            )

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setPeriodic(5000L,60 * 1000L)
            }else{
                builder.setPeriodic(5000L)
            }*/

            builder.setPersisted(true)
            builder.setMinimumLatency(5000L)
            builder.setOverrideDeadline(10000L)

            //设置网络类型 - 设定工作需要的基本网络描述
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            //如果你需要对网络能力进行更精确的控制
            //builder.setRequiredNetwork()

            //是否需要Idle - 默认false
            //如果你需要对网络能力进行更精确的控制
            //builder.setRequiredNetwork()

            //是否需要Idle - 默认false
            builder.setRequiresDeviceIdle(false)
            //是否需要充电 - 默认false
            //是否需要充电 - 默认false
            builder.setRequiresCharging(false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //指定要运行此作业，设备的电池电量不得过低。
                builder.setRequiresBatteryNotLow(false)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //指定要运行此作业，设备的可用存储空间不得过低
                builder.setRequiresStorageNotLow(false)
            }

            val jobInfo = builder.build()
            val tm: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            tm.schedule(jobInfo)
        }



        workManager.setOnClickListener{

            val uploadWorkRequest = PeriodicWorkRequestBuilder<UploadWorker>(1, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(this).enqueue(uploadWorkRequest)
        }
    }

    class UploadWorker(appContext: Context, workerParams: WorkerParameters):
        Worker(appContext, workerParams) {
        override fun doWork(): Result {

            // Do the work here--in this case, upload the images.
            Log.d("JobActivity","doWork:${Thread.currentThread().name}")

            // Indicate whether the work finished successfully with the Result
            return Result.success()
        }
    }
}