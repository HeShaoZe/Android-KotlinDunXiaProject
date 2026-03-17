package PlayMedia

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dunxiaweather.R
import android.net.Uri
import android.widget.Button
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayMediaActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play_media)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadPalyerArea()

        findViewById<Button>(R.id.start_paly).setOnClickListener{
            onStart()
        }

        findViewById<Button>(R.id.stop_play).setOnClickListener{
            onPause()
        }

        findViewById<Button>(R.id.replay).setOnClickListener{
            onResume()
        }

        findViewById<Button>(R.id.paly_page_back).setOnClickListener{
            finish()
        }
    }


    private lateinit var playerView: PlayerView
    private var player: ExoPlayer? = null

    // 视频地址示例 (可以是 http, https, file, asset)
    private val videoUrl = "https://media.w3.org/2010/05/sintel/trailer.mp4"

    ///实例话播放区域
    fun loadPalyerArea() {
        playerView = findViewById(R.id.playerView)
        // 构建 ExoPlayer
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            // 将 Player 绑定到 View (自动显示控制栏)
            playerView.player = exoPlayer

            // 创建媒体项
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))

            // 设置媒体项
            exoPlayer.setMediaItem(mediaItem)

            // 准备播放 (自动开始，如果 setPlayWhenReady(true))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true // true: 准备好就自动播放; false: 准备好但暂停等待用户点击

            // 监听播放状态 (可选)
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_BUFFERING -> println("缓冲中...")
                        Player.STATE_READY -> println("开始播放")
                        Player.STATE_ENDED -> println("播放结束")
                        Player.STATE_IDLE -> println("空闲")
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    println("是否正在播放: $isPlaying")
                }
            })
        }

        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    // 视频播完了，可以在这里跳转下一页或重播
                    player?.seekTo(0)
                    player?.play()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        // 如果之前释放了，这里可以重新初始化，或者保持 player 实例复用
        if (player == null) loadPalyerArea()
        else playerView.player = player
    }

    override fun onResume() {
        super.onResume()
        // 确保界面可见时播放
        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        // 界面不可见时暂停 (节省流量和电量)，但不释放资源
        player?.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        // 彻底停止并释放资源 (内存、解码器)
        player?.release()
        player = null
        playerView.player = null
    }


}