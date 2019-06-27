package com.dennisce.polyhymnia

class NdkPlayer1 : NdkPlayerInterface {
    /**
     * 初始化
     */
    external override fun init(paths: Array<String>)
    /**
     * 播放
     */
    external  override fun play()

    /**
     * 暂停
     */
    external override fun pause()

    /**
     * 释放资源
     */
    external override fun release()

    /**
     * 修改每个音量
     */
    external override fun changeVolumes(volumes: Array<String>)

    /**
     * 变速
     */
    external  override fun changeTempo(tempo: String)

    /**
     * 总时长 秒
     */
    external override fun duration(): Double

    /**
     * 当前进度 秒
     */
    external override fun position(): Double

    /**
     * 进度跳转
     */
    external override fun seek(sec: Double)

    external override fun isInit():Boolean


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

}