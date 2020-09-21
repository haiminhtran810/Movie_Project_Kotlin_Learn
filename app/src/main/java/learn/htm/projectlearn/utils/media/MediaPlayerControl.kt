package learn.htm.projectlearn.utils.media

interface MediaPlayerControl {
    fun start()

    fun pause()

    fun getDuration(): Long

    fun getCurrentPosition(): Long

    fun seekTo(pos: Long)

    fun isPlaying(): Boolean

    fun onReplayVideo()
}