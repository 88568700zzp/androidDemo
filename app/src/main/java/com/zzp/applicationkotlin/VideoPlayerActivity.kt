package com.zzp.applicationkotlin

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_player.*
/*import org.salient.artplayer.MediaPlayerManager
import org.salient.artplayer.ijk.IjkPlayer
import org.salient.artplayer.player.SystemMediaPlayer
import org.salient.artplayer.ui.FullscreenVideoView
import org.salient.artplayer.ui.VideoView
import org.salient.artplayer.ui.extend.OrientationEventManager*/

/**
 *
 * Created by samzhang on 2021/4/1.
 */
class VideoPlayerActivity:AppCompatActivity(){

  /*  private val orientationEventManager = OrientationEventManager()
    private val orientationEventListener = object : OrientationEventManager.OnOrientationChangeListener {
        override fun onOrientationLandscape(videoView: VideoView?) {
            //横屏
            videoView?.let {
                MediaPlayerManager.startFullscreen(this@VideoPlayerActivity, it as FullscreenVideoView)
            }
        }

        override fun onOrientationReverseLandscape(videoView: VideoView?) {
            //反向横屏
            videoView?.let {
                MediaPlayerManager.startFullscreenReverse(this@VideoPlayerActivity, it as FullscreenVideoView)
            }
        }

        override fun onOrientationPortrait(videoView: VideoView?) {
            //竖屏
            videoView?.let {
                MediaPlayerManager.dismissFullscreen(this@VideoPlayerActivity)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.start -> {
                val fullScreenVideoView = FullscreenVideoView(context = this, origin = video_view).apply {
                    this.isVolumeGestureEnable = cb_volume_gesture_enable.isChecked
                    this.isBrightnessGestureEnable = cb_brightness_gesture_enable.isChecked
                    this.isProgressGestureEnable = cb_progress_gesture_enable.isChecked
                }
                val systemMediaPlayer = IjkPlayer()
                systemMediaPlayer.setDataSource(this, Uri.parse("http://vfx.mtime.cn/Video/2018/07/06/mp4/180706094003288023.mp4"))
                fullScreenVideoView.mediaPlayer = systemMediaPlayer
                video_view.mediaPlayer = systemMediaPlayer
                if (cb_auto_orientate_enable.isChecked) {
                    orientationEventManager.orientationEnable(this, fullScreenVideoView, orientationEventListener)
                } else {
                    orientationEventManager.orientationDisable()
                }
                //开始播放
                fullScreenVideoView.prepare()
                MediaPlayerManager.startFullscreen(this, fullScreenVideoView)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }*/
}