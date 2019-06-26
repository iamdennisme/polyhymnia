package com.dennisce.polyhymnia

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class PolyhymniaPlayer {


    private val mPlayer by lazy {
        NdkPlayer()
    }

    private var mProgressDisposable: Disposable?=null

    private var oldProgress: Double = 0.0

    var onStateChangeListener:OnStateChangeListener?=null
     var onProgressListener:OnProgressListener?=null

    /**
     * 初始化
     */
    fun init(paths: Array<String>){
        mPlayer.init(paths)
        onStateChangeListener?.onChange(State.PREPARE)
    }
    /**
     * 播放
     */
    @SuppressLint("CheckResult")
    fun play(){
        mPlayer.play()
        onStateChangeListener?.onChange(State.PLAY)
        Observable.interval(1000,TimeUnit.MILLISECONDS).subscribe({
            onProgressListener?.onProgress(getPosition().toInt())
            if (oldProgress==getPosition()){
                onStateChangeListener?.onChange(State.COMPLETE)
                mProgressDisposable?.dispose()
            }
            oldProgress=getPosition()
        },{

        }).run {
            mProgressDisposable=this
        }
    }

    /**
     * 暂停
     */
     fun pause(){
        mPlayer.pause()
        mProgressDisposable?.dispose()
        onStateChangeListener?.onChange(State.PAUSE)
    }

    /**
     * 释放资源
     */
     fun release(){
        mProgressDisposable?.dispose()
        mPlayer.release()
    }

    /**
     * 修改每个音量
     */
    fun changeVolumes(volumes: Array<String>){
        mPlayer.changeVolumes(volumes)
    }

    /**
     * 变速
     */
    fun changeTempo(tempo: String){
        mPlayer.changeTempo(tempo)
    }
    /**
     * 总时长 秒
     */
    fun getDuration(): Double{
        return mPlayer.duration()
    }

    /**
     * 当前进度 秒
     */
    fun getPosition(): Double{
        return mPlayer.position()
    }

    /**
     * 进度跳转
     */
     fun seek(sec: Double){
        mPlayer.seek(sec)
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


    interface OnStateChangeListener{
        fun onChange(state:State)
    }
    interface OnProgressListener{
        fun onProgress(progress:Int)
    }

    enum class State{
        PLAY,
        PAUSE,
        COMPLETE,
        PREPARE
    }
}