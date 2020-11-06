package learn.htm.projectlearn.base

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import learn.htm.projectlearn.R
import learn.htm.projectlearn.model.NetworkStatus
import learn.htm.projectlearn.model.NetworkType
import learn.htm.projectlearn.utils.extensions.toMediaSource
import learn.htm.projectlearn.utils.network.ConnectionStateMonitor
import timber.log.Timber

open class BaseService : MediaBrowserServiceCompat() {

    private var networkStatusLiveData: Observer<NetworkStatus>? = null
    protected lateinit var mediaSession: MediaSessionCompat
    protected lateinit var mediaSessionConnector: MediaSessionConnector
    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()

    private var isForegroundService = false

    private val uAmpAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    private val playerListener = PlayerEventListener()

    // The current player will either be an ExoPlayer (for local playback) or a CastPlayer (for
    // remote playback through a Cast device).
    private lateinit var currentPlayer: Player

    /**
     * Configure ExoPlayer to handle audio focus for us.
     * See [Player.AudioComponent.setAudioAttributes] for details.
     */
    private val exoPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(uAmpAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.i("BaseService: onCreate")

        // Build a PendingIntent that can be used to launch the UI.
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }

        // Create a new MediaSession.
        mediaSession = MediaSessionCompat(this, "MusicService")
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }

        /**
         * In order for [MediaBrowserCompat.ConnectionCallback.onConnected] to be called,
         * a [MediaSessionCompat.Token] needs to be set on the [MediaBrowserServiceCompat].
         *
         * It is possible to wait to set the session token, if required for a specific use-case.
         * However, the token *must* be set by the time [MediaBrowserServiceCompat.onGetRoot]
         * returns, or the connection will fail silently. (The system will not even call
         * [MediaBrowserCompat.ConnectionCallback.onConnectionFailed].)
         */
        sessionToken = mediaSession.sessionToken

        switchToPlayer(
            previousPlayer = null,
            newPlayer = exoPlayer
        )

        // ExoPlayer will manage the MediaSession for us.
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(UampPlaybackPreparer())
        //mediaSessionConnector.setQueueNavigator(UampQueueNavigator(mediaSession))

        networkStatusLiveData = Observer {
            when (it.networkType) {
                NetworkType.WIFI -> {
                    showToast(message = getString(R.string.network_wifi), isCenterScreen = true)
                }
                NetworkType.CELLULAR -> {
                    showToast(message = getString(R.string.network_cellular), isCenterScreen = true)
                }
                NetworkType.LOSE -> {
                    showToast(message = getString(R.string.network_lost), isCenterScreen = true)
                }
                NetworkType.VPN -> {
                    showToast(message = getString(R.string.network_vpn), isCenterScreen = true)
                }
                else -> {
                    showToast(message = "Network other", isCenterScreen = true)
                }
            }
        }

        networkStatusLiveData?.let {
            ConnectionStateMonitor(this).observeForever(it)
        }
    }


    /**
     * This is the code that causes UAMP to stop playing when swiping the activity away from
     * recents. The choice to do this is app specific. Some apps stop playback, while others allow
     * playback to continue and allow users to stop it with the notification.
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        /**
         * By stopping playback, the player will transition to [Player.STATE_IDLE] triggering
         * [Player.EventListener.onPlayerStateChanged] to be called. This will cause the
         * notification to be hidden and trigger
         * [PlayerNotificationManager.NotificationListener.onNotificationCancelled] to be called.
         * The service will then remove itself as a foreground service, and will call
         * [stopSelf].
         */
        currentPlayer.stop(true)
    }

    private val dataSourceFactory: DefaultDataSourceFactory by lazy {
        DefaultDataSourceFactory(
            /* context= */ this,
            Util.getUserAgent(/* context= */ this, UAMP_USER_AGENT), /* listener= */
            null
        )
    }

    /**
     * Load the supplied list of songs and the song to play into the current player.
     */
    private fun preparePlaylist(
        metadataList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean,
        playbackStartPositionMs: Long
    ) {
        // Since the playlist was probably based on some ordering (such as tracks
        // on an album), find which window index to play first so that the song the
        // user actually wants to hear plays first.
        val initialWindowIndex = if (itemToPlay == null) 0 else metadataList.indexOf(itemToPlay)
        currentPlaylistItems = metadataList

        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.stop(true)
        if (currentPlayer == exoPlayer) {
            val mediaSource = metadataList.toMediaSource(dataSourceFactory)
            exoPlayer.prepare(mediaSource)
            exoPlayer.seekTo(initialWindowIndex, playbackStartPositionMs)
        } else {
            /* currentPlayer == castPlayer */
//            val items: Array<MediaQueueItem> = metadataList.map {
//                it.toMediaQueueItem()
//            }.toTypedArray()
//            castPlayer.loadItems(
//                items,
//                initialWindowIndex,
//                playbackStartPositionMs,
//                Player.REPEAT_MODE_OFF
//            )
        }
    }

    private fun switchToPlayer(previousPlayer: Player?, newPlayer: Player) {
        if (previousPlayer == newPlayer) {
            return
        }
        currentPlayer = newPlayer
        if (previousPlayer != null) {
            val playbackState = previousPlayer.playbackState
            if (currentPlaylistItems.isEmpty()) {
                // We are joining a playback session. Loading the session from the new player is
                // not supported, so we stop playback.
                currentPlayer.stop(/* reset= */true)
            } else if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
                preparePlaylist(
                    metadataList = currentPlaylistItems,
                    itemToPlay = currentPlaylistItems[previousPlayer.currentWindowIndex],
                    playWhenReady = previousPlayer.playWhenReady,
                    playbackStartPositionMs = previousPlayer.currentPosition
                )
            }
        }
        mediaSessionConnector.setPlayer(newPlayer)
        previousPlayer?.stop(/* reset= */true)
    }

    private fun showToast(message: String, isCenterScreen: Boolean = false) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).apply {
            if (isCenterScreen) {
                setGravity(Gravity.CENTER, 0, 0)
            }
            show()
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        Timber.d("On get root: \"clientPackageName: $clientPackageName \\n clientUid: $clientUid \\n rootHints: ${rootHints.toString()}\"")
        val rootExtras = Bundle().apply {
            putBoolean(CONTENT_STYLE_SUPPORTED, true)
            putInt(CONTENT_STYLE_BROWSABLE_HINT, CONTENT_STYLE_GRID)
            putInt(CONTENT_STYLE_PLAYABLE_HINT, CONTENT_STYLE_LIST)
        }
        return BrowserRoot(UAMP_EMPTY_ROOT, rootExtras)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(null)
    }

    private inner class PlayerEventListener : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY -> {
                    //notificationManager.showNotificationForPlayer(currentPlayer)
                    if (playbackState == Player.STATE_READY) {

                        // When playing/paused save the current media item in persistent
                        // storage so that playback can be resumed between device reboots.
                        // Search for "media resumption" for more information.
                        //saveRecentSongToStorage()

                        if (!playWhenReady) {
                            // If playback is paused we remove the foreground state which allows the
                            // notification to be dismissed. An alternative would be to provide a
                            // "close" button in the notification which stops playback and clears
                            // the notification.
                            stopForeground(false)
                        }
                    }
                }
                else -> {
                    //notificationManager.hideNotification()
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            //var message = R.string.generic_error;
            //var message = R.string.generic_error
            when (error.type) {
                // If the data from MediaSource object could not be loaded the Exoplayer raises
                // a type_source error.
                // An error message is printed to UI via Toast message to inform the user.
                ExoPlaybackException.TYPE_SOURCE -> {
                    // message = R.string.error_media_not_found;
                    Timber.d("TYPE_SOURCE - ${error.sourceException.message}")
                }
                // If the error occurs in a render component, Exoplayer raises a type_remote error.
                ExoPlaybackException.TYPE_RENDERER -> {
                    Timber.d("TYPE_RENDERER - ${error.rendererException.message}")
                }
                // If occurs an unexpected RuntimeException Exoplayer raises a type_unexpected error.
                ExoPlaybackException.TYPE_UNEXPECTED -> {
                    Timber.d("TYPE_UNEXPECTED - ${error.unexpectedException.message}")
                }
                // Occurs when there is a OutOfMemory error.
                ExoPlaybackException.TYPE_OUT_OF_MEMORY -> {
                    Timber.d("TYPE_OUT_OF_MEMORY - ${error.outOfMemoryError.message}")
                }
                // If the error occurs in a remote component, Exoplayer raises a type_remote error.
                ExoPlaybackException.TYPE_REMOTE -> {
                    Timber.d("TYPE_REMOTE - ${error.message}")
                }
            }
//            Toast.makeText(
//                applicationContext,
//                message,
//                Toast.LENGTH_LONG
//            ).show()
        }
    }

    private inner class UampPlaybackPreparer : MediaSessionConnector.PlaybackPreparer {
        override fun onCommand(
            player: Player,
            controlDispatcher: ControlDispatcher,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun getSupportedPrepareActions(): Long {
            TODO("Not yet implemented")
        }

        override fun onPrepare(playWhenReady: Boolean) {
            TODO("Not yet implemented")
        }

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?
        ) {
            TODO("Not yet implemented")
        }

        override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) {
            TODO("Not yet implemented")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.run {
            isActive = false
            release()
        }

        // Free ExoPlayer resources.
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    companion object {
        const val UAMP_EMPTY_ROOT = "_EMPTY_"
        private const val UAMP_USER_AGENT = "UAMP_USER_AGENT"
        /*
        * (Media) Session events
        */

        const val NETWORK_FAILURE = "com.example.android.uamp.media.session.NETWORK_FAILURE"

        /** Content styling constants */
        private const val CONTENT_STYLE_BROWSABLE_HINT =
            "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT"
        private const val CONTENT_STYLE_PLAYABLE_HINT =
            "android.media.browse.CONTENT_STYLE_PLAYABLE_HINT"
        private const val CONTENT_STYLE_SUPPORTED = "android.media.browse.CONTENT_STYLE_SUPPORTED"
        private const val CONTENT_STYLE_LIST = 1
        private const val CONTENT_STYLE_GRID = 2
    }
}