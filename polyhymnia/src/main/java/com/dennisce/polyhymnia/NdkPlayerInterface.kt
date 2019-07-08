package com.dennisce.polyhymnia

interface NdkPlayerInterface{
    /**
     * 初始化
     */
     fun init(paths: Array<String>)
    /**
     * 播放
     */
     fun play()

    /**
     * 暂停
     */
     fun pause()
    /**
     * 释放资源
     */
     fun release()

    /**
     * 修改每个音量
     */
     fun changeVolumes(volumes: Array<String>)

    /**
     * 变速
     */
     fun changeTempo(tempo: String)

    /**
     * 总时长 秒
     */
     fun duration(): Double

    /**
     * 当前进度 秒
     */
     fun position(): Double

    /**
     * 进度跳转
     */
     fun seek(sec: Double)

     fun isInit():Boolean
}