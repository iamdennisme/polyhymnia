package com.dennisce.polyhymnia.sample

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.dennisce.polyhymnia.PolyhymniaPlayer
import com.tbruyelle.rxpermissions2.RxPermissions

class MainActivity : AppCompatActivity() {

    private val player by lazy {
        PolyhymniaPlayer()
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
                } else {
                    val path = "${Environment.getExternalStorageDirectory()}/test"
                    val srcs3 = arrayOf(
                        "$path/Timetravel.mp3"
                    )
                    player.init(srcs3)
                    player.play()
                }
            }
    }
}
