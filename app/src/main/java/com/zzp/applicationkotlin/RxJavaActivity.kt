package com.zzp.applicationkotlin

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
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
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_rx_java.*
import java.util.concurrent.TimeUnit


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
        concat.setOnClickListener(this)
        cutThread.setOnClickListener(this)
        count.setOnClickListener(this)
        merge.setOnClickListener(this)
        exception.setOnClickListener(this)
        callNet.setOnClickListener(this)

        val foregroundColorSpan =
            ForegroundColorSpan(getResources().getColor(R.color.design_default_color_primary))
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

                }).observeOn(AndroidSchedulers.mainThread()).subscribe(observer)
            }
            concat->{
                Observable.concat(Observable.create({it->
                    it.onNext(666)
                    Thread.sleep(1000)
                    it.onNext(667)
                    it.onComplete()
                }),Observable.just(1,2),Observable.just(3,4))
                    .observeOn(Schedulers.newThread())
                    .map (object:io.reactivex.functions.Function<Int,String>{
                        override fun apply(t: Int): String {
                            Log.d(TAG,"${Thread.currentThread().name} apply ${t}")
                            return "index:${t}"
                        }
                    })
                    .subscribe(object: Consumer<String> {
                        override fun accept(t: String?) {
                            Log.d(TAG,"${Thread.currentThread().name} accept:${t}")
                        }

                    })
            }
            cutThread->{
                Observable.just("123","zzp123").observeOn(Schedulers.io()).map (object:io.reactivex.functions.Function<String,String>{
                    override fun apply(t: String): String {
                        Log.d(TAG,"1 ${Thread.currentThread().name} apply ${t}")
                        return t
                    }
                }).observeOn(AndroidSchedulers.mainThread()).map (object:io.reactivex.functions.Function<String,String>{
                    override fun apply(t: String): String {
                        Log.d(TAG,"2 ${Thread.currentThread().name} apply ${t}")
                        return t
                    }
                }).observeOn(Schedulers.io()).doOnNext {
                    Log.d(TAG,"3 ${Thread.currentThread().name} doOnNext ${it}")
                }.observeOn(AndroidSchedulers.mainThread()).doOnComplete {
                    Log.d(TAG,"4 ${Thread.currentThread().name} doOnComplete ")
                }.subscribe()
            }
            count->{
                Observable.concat(Observable.just(20,12,13,12),Observable.just(1,2)).count().subscribe { it, throwable ->
                    if(it != null && throwable == null){
                        Log.d(TAG,"${Thread.currentThread().name} doOnNext ${it}")
                    }
                }
            }
            merge->{
                Observable.merge(
                    Observable.intervalRange(1,2,1,1, TimeUnit.SECONDS),
                    Observable.just("123")
                ).observeOn(Schedulers.io()).subscribe(object:Consumer<Any>{
                    override fun accept(t: Any?) {
                        Log.d(TAG,"${Thread.currentThread().name} doOnNext ${t}")
                    }

                },object:Consumer<Throwable>{
                    override fun accept(t: Throwable?) {
                        Log.d(TAG,"${Thread.currentThread().name} doOnError ${t}")
                    }
                })
            }
            exception->{
                Observable.error<Int>(IllegalArgumentException())
                    //.onExceptionResumeNext(Observable.just(404))
                    /*.doOnNext {
                    Log.d(TAG,"${Thread.currentThread().name} doOnNext ${it}")
                }.doOnError {
                    Log.d(TAG,"${Thread.currentThread().name} doOnError ${it}")
                }*/.subscribe(object:Consumer<Int>{
                        override fun accept(t: Int?) {
                            Log.d(TAG,"${Thread.currentThread().name} doOnNext ${t}")
                        }
                    },object:Consumer<Throwable>{
                        override fun accept(t: Throwable?) {
                            Log.d(TAG,"${Thread.currentThread().name} doOnError ${t}")
                        }
                    })
            }
            callNet->{
                var observable1 = Observable.create(object :ObservableOnSubscribe<Int>{
                        override fun subscribe(emitter: ObservableEmitter<Int>) {
                            Thread.sleep(1000)
                            emitter.onNext(200)
                            emitter.onComplete()
                        }
                    }
                )

                observable1
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .flatMap(object:io.reactivex.functions.Function<Int,ObservableSource<String>>{
                        override fun apply(t: Int): ObservableSource<String> {
                            return Observable.create(object :ObservableOnSubscribe<String>{
                                override fun subscribe(emitter: ObservableEmitter<String>) {
                                    Log.d(TAG,"${Thread.currentThread().name} subscribe:${t}")
                                    Thread.sleep(1000)
                                    emitter.onNext("result:${t}")
                                    emitter.onComplete()
                                }
                            }).observeOn(Schedulers.io())
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d(TAG,"${Thread.currentThread().name} onNext:${it}")
                    }
            }
        }
    }

    private var observer = object:Observer<String>{
        override fun onSubscribe(d: Disposable) {
            Log.d(TAG,"onSubscribe")
        }

        override fun onNext(t: String) {
            Log.d(TAG,"${Thread.currentThread().name} onNext:${t}")
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