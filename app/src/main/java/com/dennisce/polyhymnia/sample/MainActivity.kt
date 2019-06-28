package com.dennisce.polyhymnia.sample

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.dennisce.polyhymnia.PolyhymniaPlayer
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val player by lazy {
        PolyhymniaPlayer(PolyhymniaPlayer.Type.MAIN).apply {
            isLoop=true
            onStateChangeListener=object :PolyhymniaPlayer.OnStateChangeListener{
                override fun onChange(state: PolyhymniaPlayer.State) {
                     Log.d("PolyhymniaPlayer",state.toString())
                    if (state==PolyhymniaPlayer.State.COMPLETE){
                        //stop()
                    }
                }
            }
            onProgressListener=object :PolyhymniaPlayer.OnProgressListener{
                override fun onProgress(progress: Int) {
                    Log.d("PolyhymniaPlayer",progress.toString())
                }

            }
        }
    }



    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxPermissions(this)
            .request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .subscribe { granted ->
                if (!granted) {
                    finish()
                }
            }

        btn_prepare.setOnClickListener {
            val path1="sdcard/test/a.mp3"
            player.init(arrayOf(path1))
        }
        btn_play.setOnClickListener {
            player.play()
        }

        btn_pause.setOnClickListener {
            player.pause()
        }
        btn_stop.setOnClickListener {
            player.stop()
        }
    }
}
