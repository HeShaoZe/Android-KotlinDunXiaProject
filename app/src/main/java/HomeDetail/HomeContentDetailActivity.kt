package HomeDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dunxiaweather.R
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import com.bumptech.glide.Glide // 假设你用了 Glide 加载图片

class HomeContentDetailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_content_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.my_back_button).setOnClickListener{
            finish();
        }

        findViewById<Button>(R.id.select_source_info).setOnClickListener{
            openLocationAlbum()
        }
    }


    private lateinit var imageView: ImageView
    // 1. 定义启动器 (Launcher)
    // 当用户从相册返回时，这个 lambda 会被调用，uri 就是选中的图片地址
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent() // 专门用于获取单个媒体内容
    ) { uri: Uri? ->
        // 2. 处理结果
        if (uri != null) {
            // 用户选择了图片
            Toast.makeText(this, "获取到图片: $uri", Toast.LENGTH_SHORT).show()

            // 加载图片到 ImageView (使用 Glide 或 Coil)
            loadImage(uri)
        } else {
            // 用户取消了选择
            Toast.makeText(this, "未选择图片", Toast.LENGTH_SHORT).show()
        }
    }

    val fromAlbum = 2
    ///打开相册
    fun openLocationAlbum() {
        imageView = findViewById(R.id.content_data_source)
        // 启动相册，"image/*" 表示只筛选图片类型
        // 也可以传 "video/*" 或 "*/*"
        pickImageLauncher.launch("image/*")
    }

    private fun loadImage(uri: Uri) {
        // 使用 Glide 加载 (推荐)
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(imageView)

        // 或者使用 Coil: imageView.load(uri)
    }

}