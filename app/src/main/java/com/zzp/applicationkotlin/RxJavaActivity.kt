package com.zzp.applicationkotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_rx_java.*


/**
 * create by zhangzhipeng @2021/12/30
 */
class RxJavaActivity : AppCompatActivity(), View.OnClickListener,
    LifecycleProvider<ActivityEvent>{

    private val TAG = "RxJavaActivity_"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleSubject.onNext(ActivityEvent.CREATE)

        setContentView(R.layout.activity_rx_java)

        single.setOnClickListener(this)
        observable.setOnClickListener(this)
        map.setOnClickListener(this)
        flatmap.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            single->{
                Single.create(object : SingleOnSubscribe<String>{
                    override fun subscribe(emitter: SingleEmitter<String>) {
                        Log.d(TAG,"subscribe:${Thread.currentThread().name}")
                        //Thread.sleep(5000)
                        emitter.onSuccess("zzp123")
                    }

                }).compose(bindToLifecycle()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnSuccess {
                    Log.d(TAG,"doOnSuccess:${Thread.currentThread().name}")
                    Log.d(TAG,it)
                }.doOnError {
                    Log.d(TAG,"doOnError:${Thread.currentThread().name}")
                    Log.d(TAG,"throwable")
                }.subscribe()
            }
            observable->{
                Observable.create(object: ObservableOnSubscribe<String> {
                    override fun subscribe(emitter: ObservableEmitter<String>) {
                        emitter.onNext("zzp123")
                        emitter.onNext("zzp124")
                        emitter.onComplete()
                    }

                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer)

            }
            map->{
                Observable.create(object: ObservableOnSubscribe<String> {
                    override fun subscribe(emitter: ObservableEmitter<String>) {
                        emitter.onNext("zzp123")
                        emitter.onNext("zzp124")
                        emitter.onComplete()
                    }

                }).map(object : io.reactivex.functions.Function<String, String> {
                    override fun apply(t: String): String {
                        return "$t add"
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer)
            }
            flatmap->{
                Observable.create(object: ObservableOnSubscribe<String> {
                    override fun subscribe(emitter: ObservableEmitter<String>) {
                        emitter.onNext("zzp123")
                        emitter.onNext("zzp124")
                        emitter.onComplete()
                    }

                }).flatMap (object : io.reactivex.functions.Function<String, Observable<String>> {
                    override fun apply(t: String): Observable<String> {
                        return Observable.just(t + " 123")
                    }

                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer)
            }
        }
    }

    private var observer = object:Observer<String>{
        override fun onSubscribe(d: Disposable) {
            Log.d(TAG,"onSubscribe")
        }

        override fun onNext(t: String) {
            Log.d(TAG,t)
        }

        override fun onError(e: Throwable) {
            Log.d(TAG,"throwable")
        }

        override fun onComplete() {
            Log.d(TAG,"onComplete")
        }

    }

    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject.hide()
    }

    override fun <T : Any?> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return com.trello.rxlifecycle2.RxLifecycle.bindUntilEvent(lifecycleSubject, event!!)
    }


    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }


    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }


    override fun onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }


    override fun onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }


    override fun onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        super.onDestroy()
    }


}