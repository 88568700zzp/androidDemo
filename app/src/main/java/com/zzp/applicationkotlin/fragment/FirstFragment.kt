package com.zzp.applicationkotlin.fragment

import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.NinePatchDrawable
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zzp.applicationkotlin.R
import com.zzp.applicationkotlin.service.FirstService
import com.zzp.applicationkotlin.service.SecondService
import kotlinx.android.synthetic.main.fragment_first.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    val TAG = "FirstFragment"

    var mWifiManager:WifiManager ?= null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_first.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        textview_first.setOnClickListener {
            var intent = Intent()
            intent.setClassName(activity?.packageName!!,FirstService::class.java.name)
            if(Build.VERSION.SDK_INT >= 26){
                requireActivity().startForegroundService(intent)
            }else{
                requireActivity().startService(intent)
            }

        }

        textview_second.setOnClickListener {
            var intent = Intent()
            intent.setClassName(activity?.packageName!!,SecondService::class.java.name)
            if(Build.VERSION.SDK_INT >= 26){
                requireActivity().startForegroundService(intent)
            }else{
                requireActivity().startService(intent)
            }
        }

        with(requireActivity().resources.displayMetrics){
            Log.d(TAG,"bitmap density:" + density + " densityDpi:" + densityDpi)
        }


        image_first.setImageResource(R.drawable.bg_user_task_bottom)
        image_first.postDelayed(object :Runnable{
            override fun run() {
                var drawable = image_first.drawable as NinePatchDrawable
                drawable.getConstantState().apply {
                    Log.d(TAG,"bitmap width:"  + " heigth:" )
                }
            }
        },1000)

        val wifiFilter = IntentFilter()
        wifiFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        wifiFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)
        requireActivity().registerReceiver(receiver,wifiFilter)

        mWifiManager =
            requireActivity().applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        mWifiManager?.scanResults?.forEach {
            Log.d(TAG,"scanResult:${it}")
        }


        Log.d(TAG,"wifi ssid:${getCurentWifiSSID()}")

        val animator: ObjectAnimator = ObjectAnimator.ofFloat(textview_second, "translationY", 0f, 100f)
        animator.interpolator = AccelerateInterpolator()
        animator.duration = 450
        animator.repeatMode = android.animation.ValueAnimator.REVERSE
        animator.repeatCount = Animation.INFINITE
        //animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(receiver)
    }

    fun getWifiList(): List<ScanResult>? {
        val scanWifiList: List<ScanResult>? = mWifiManager?.scanResults
        val wifiList: MutableList<ScanResult> = ArrayList()
        if (scanWifiList != null && scanWifiList.size > 0) {
            val signalStrength = HashMap<String, Int?>()
            for (i in scanWifiList.indices) {
                val scanResult: ScanResult = scanWifiList[i]
                Log.e(TAG, "搜索的wifi-ssid:" + scanResult.SSID)
                if (!scanResult.SSID.isEmpty()) {
                    val key: String =
                        scanResult.SSID.toString() + " " + scanResult.capabilities
                    if (!signalStrength.containsKey(key)) {
                        signalStrength[key] = i
                        wifiList.add(scanResult)
                    }
                }
            }
        } else {
            Log.e(TAG, "没有搜索到wifi")
        }
        return wifiList
    }

    var receiver:BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i(TAG, "onReceive: intent action" + intent?.getAction());
            if (intent?.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                getWifiList()?.forEach {
                    Log.d(TAG,"result:${it.toString()}")
                }
            }
        }
    }

    //得到当前连接的WiFi  SSID
    fun getCurentWifiSSID(): String? {
        var ssid = ""
        var wifiInfo = mWifiManager!!.connectionInfo
        ssid = wifiInfo.ssid
        if (ssid.substring(0, 1) == "\"" && ssid.substring(ssid.length - 1) == "\"") {
            ssid = ssid.substring(1, ssid.length - 1)
        }
        return ssid
    }

}