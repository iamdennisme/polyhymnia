package com.dennisce.polyhymnia

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class PolyhymniaPlayer(type:Type) {

    var isLoop=false

    private val mPlayer by lazy {
        if (type==Type.MAIN)
            NdkPlayer()
        else
            NdkPlayer1()
    }

    private var mProgressDisposable: Disposable? = null

    private var oldProgress: Double = 0.0

    var onStateChangeListener: OnStateChangeListener? = null
    var onProgressListener: OnProgressListener? = null

    /**
     * 初始化
     */
    fun init(paths: Array<String>) {
        mPlayer.init(paths)
        onStateChangeListener?.onChange(State.PREPARE)
    }

    /**
     * 播放
     */
    @SuppressLint("CheckResult")
    fun play() {
        throwNotInit()
        mPlayer.play()
        onStateChangeListener?.onChange(State.PLAY)
        Observable.interval(1000, TimeUnit.MILLISECONDS).subscribe({
            onProgressListener?.onProgress(getPosition().toInt())
            if (oldProgress == getPosition()) {
                onStateChangeListener?.onChange(State.COMPLETE)
                if (isLoop){
                    loop()
                }else{
                    stop()
                }
                return@subscribe
            }
            oldProgress = getPosition()
        }, {
        }).run {
            mProgressDisposable = this
        }
    }

    /**
     * 暂停
     */
    fun pause() {
        throwNotInit()
        mPlayer.pause()
        mProgressDisposable?.dispose()
        onStateChangeListener?.onChange(State.PAUSE)
    }

    /**
     * 停止上新的
     */

    fun stop() {
        if (!isInit()) {
            return
        }
        mPlayer.pause()
        mPlayer.release()
        mProgressDisposable?.dispose()
        onStateChangeListener?.onChange(State.STOP)
    }

    /**
     * 循环
     */

    fun loop() {
        onStateChangeListener?.onChange(State.LOOP)
        if (!isInit()) {
            return
        }
        mProgressDisposable?.dispose()
        play()
    }

    /**
     * 释放资源
     */
    fun release() {
        if (!isInit()){
            return
        }
        mProgressDisposable?.dispose()
        mPlayer.release()
    }

    /**
     * 修改每个音量
     */
    fun changeVolumes(volumes: Array<String>) {
        throwNotInit()
        mPlayer.changeVolumes(volumes)
    }

    /**
     * 变速
     */
    fun changeTempo(tempo: String) {
        throwNotInit()
        mPlayer.changeTempo(tempo)
    }

    /**
     * 总时长 秒
     */
    fun getDuration(): Double {
        throwNotInit()
        return mPlayer.duration()
    }

    /**
     * 当前进度 秒
     */
    fun getPosition(): Double {
        throwNotInit()
        return mPlayer.position()
    }

    /**
     * 进度跳转
     */
    fun seek(sec: Double) {
        throwNotInit()
        mPlayer.seek(sec)
    }

    fun isInit(): Boolean {
        return mPlayer.isInit()
    }

    private fun throwNotInit() {
        if (!isInit()) {
            throw Throwable("you must init first")
        }
    }

    companion object {
        init {
            System.loadLibrary("avutil-55")
            System.loadLibrary("swresample-2")
            System.loadLibrary("avcodec-57")
            System.loadLibrary("avfilter-6")
            System.loadLibrary("swscale-4")
            System.loadLibrary("avformat-57")
            System.loadLibrary("native-lib")
        }
    }


    interface OnStateChangeListener {
        fun onChange(state: State)
    }

    interface OnProgressListener {
        fun onProgress(progress: Int)
    }

    enum class State {
        PLAY,
        PAUSE,
        STOP,
        COMPLETE,
        PREPARE,
        LOOP
    }

    enum class Type{
        MAIN,
        SECOND
    }
}