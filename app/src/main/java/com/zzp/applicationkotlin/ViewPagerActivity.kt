package com.zzp.applicationkotlin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_view_pager.*

class ViewPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        view_pager.adapter = object:FragmentPagerAdapter(supportFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
            override fun getCount(): Int {
                return 5
            }

            override fun getItem(position: Int): Fragment {
                var fragment = PagerFragment()
                fragment.position = position
                fragment.TAG = "PagerFragment$position"
                return fragment
            }
        }

    }

    class PagerFragment:Fragment(){
        var position:Int = 0

        var TAG = "PagerFragment"

        private lateinit var mTextView:TextView
        private var mLoaded = false
        private lateinit var viewModel:DataViewModel

        override fun onAttach(activity: Activity) {
            super.onAttach(activity)
            Log.d(TAG,"onAttach")
            viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
            viewModel.setValue("load success")
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            Log.d(TAG,"onCreateView")
            var textView = TextView(container!!.context)
            textView.text = "loading"
            mTextView = textView
            return textView
        }

        override fun onResume() {
            super.onResume()
            Log.d(TAG,"onResume:${userVisibleHint}")
            if(!mLoaded){
                mLoaded = true
                requestData()
            }
        }

        override fun onHiddenChanged(hidden: Boolean) {
            super.onHiddenChanged(hidden)
            Log.d(TAG,"onHiddenChanged:$hidden")
        }

        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)
            Log.d(TAG,"setUserVisibleHint:$isVisibleToUser")
        }

        fun requestData(){
            mTextView.text = viewModel.getValue().value
        }


    }

    class DataViewModel: ViewModel() {
        var mutableLiveDatas = MutableLiveData<String>()

        fun getValue(): MutableLiveData<String> {
            return mutableLiveDatas
        }

        fun setValue(value: String) {
            mutableLiveDatas.value = value
        }
    }
}