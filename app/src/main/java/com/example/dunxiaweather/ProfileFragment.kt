package com.example.dunxiaweather

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private lateinit var ivAvatar: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvUserId: TextView
    private lateinit var btnLogin: Button
    private lateinit var layoutUserInfo: LinearLayout
    private lateinit var tvNickname: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnLogout: Button
    private lateinit var switchNotify: SwitchCompat
    private lateinit var switchTheme: SwitchCompat

    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        initViews(view)
        updateUI()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun initViews(view: View) {
        ivAvatar = view.findViewById(R.id.iv_avatar)
        tvUsername = view.findViewById(R.id.tv_username)
        tvUserId = view.findViewById(R.id.tv_user_id)
        btnLogin = view.findViewById(R.id.btn_login)
        layoutUserInfo = view.findViewById(R.id.layout_user_info)
        tvNickname = view.findViewById(R.id.tv_nickname)
        tvPhone = view.findViewById(R.id.tv_phone)
        tvEmail = view.findViewById(R.id.tv_email)
        btnLogout = view.findViewById(R.id.btn_logout)
        switchNotify = view.findViewById(R.id.switch_notify)
        switchTheme = view.findViewById(R.id.switch_theme)
    }

    private fun updateUI() {
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            btnLogin.visibility = View.GONE
            layoutUserInfo.visibility = View.VISIBLE

            val nickname = prefs.getString("nickname", "用户")
            val userId = prefs.getString("user_id", "")
            val phone = prefs.getString("phone", "未设置")
            val email = prefs.getString("email", "未设置")
            val avatarUrl = prefs.getString("avatar_url", "")

            tvUsername.text = nickname
            tvUserId.text = "ID: $userId"
            tvNickname.text = nickname
            tvPhone.text = phone
            tvEmail.text = email

            if (avatarUrl != null) {
                if (avatarUrl.isNotEmpty()) {
                    Glide.with(this)
                        .load(avatarUrl)
                        .circleCrop()
                        .into(ivAvatar)
                }
            }
        } else {
            btnLogin.visibility = View.VISIBLE
            layoutUserInfo.visibility = View.GONE
            tvUsername.text = "点击登录"
            tvUserId.text = "ID: --"
        }
    }

    private fun setupListeners() {
        // 登录按钮
        btnLogin.setOnClickListener {
            showLoginDialog()
        }

        // 头像点击登录/编辑
        ivAvatar.setOnClickListener {
            val isLoggedIn = prefs.getBoolean("is_logged_in", false)
            if (!isLoggedIn) {
                showLoginDialog()
            }
        }

        // 退出登录
        btnLogout.setOnClickListener {
            prefs.edit().clear().apply()
            Toast.makeText(requireContext(), "已退出登录", Toast.LENGTH_SHORT).show()
            updateUI()
        }

        // 通知开关
        switchNotify.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notify_enabled", isChecked).apply()
            Toast.makeText(
                requireContext(),
                if (isChecked) "已开启通知" else "已关闭通知",
                Toast.LENGTH_SHORT
            ).show()
        }

        // 主题开关
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            Toast.makeText(
                requireContext(),
                if (isChecked) "已开启深色模式" else "已关闭深色模式",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showLoginDialog() {
        // 模拟登录 - 实际项目中应该调用登录API
        val dialogView = layoutInflater.inflate(R.layout.dialog_login, null)
        val etUsername = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_username)
        val etPassword = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_password)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btn_submit)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("登录")
            .setView(dialogView)
            .setNegativeButton("取消", null)
            .create()

        btnSubmit.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "请输入用户名和密码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 模拟登录成功
            prefs.edit().apply {
                putBoolean("is_logged_in", true)
                putString("nickname", username)
                putString("user_id", "100${(1000..9999).random()}")
                putString("phone", "138****${(1000..9999).random()}")
                putString("email", "$username@example.com")
                putString("avatar_url", "https://picsum.photos/200")
                putBoolean("notify_enabled", true)
                putBoolean("dark_mode", false)
                apply()
            }

            dialog.dismiss()
            updateUI()
            Toast.makeText(requireContext(), "登录成功", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}
