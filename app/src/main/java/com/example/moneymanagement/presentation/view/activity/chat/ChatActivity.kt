package com.example.moneymanagement.presentation.view.activity.chat

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import android.view.LayoutInflater
import android.widget.Toast
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.ActivityChatBinding
import com.example.moneymanagement.databinding.DialogNoInternetBinding
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.utils.FinancialContextHelper
import com.example.moneymanagement.presentation.utils.NetworkUtils
import com.example.moneymanagement.presentation.view.adapter.ChatAdapter
import com.example.moneymanagement.presentation.viewmodel.ChatViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val adapter = ChatAdapter()
    
    private val viewModel: ChatViewModel by viewModels()
    private var noInternetDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setupUI()
        observeViewModel()
        loadContextAndInit()
        setupWindowInsets()
        checkNetworkAndShowDialog()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            
            // Apply padding to avoid covering UI with system bars or keyboard
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                if (ime.bottom > 0) ime.bottom else systemBars.bottom
            )
            insets
        }
    }

    private fun setupUI() {
        binding.rvChat.adapter = adapter
        
        binding.btnBack.setOnClickListener { finish() }
        
        binding.btnSend.setOnClickListener {
            if (!NetworkUtils.isNetworkAvailable(this)) {
                showNoInternetDialog()
                return@setOnClickListener
            }
            val text = binding.edtMessage.text.toString()
            if (text.isNotBlank()) {
                val apiKey = getString(R.string.apiKeyChatGPT).trim()
                viewModel.sendMessage(apiKey, text)
                binding.edtMessage.setText("")
            }
        }

        adapter.onLongClick = { message ->
            if (message.id != null) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Xóa tin nhắn")
                    .setMessage("Bạn có chắc chắn muốn xóa tin nhắn này không? Hành động này không thể hoàn tác.")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa") { _, _ ->
                        viewModel.deleteMessage(message)
                    }
                    .show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.messages.observe(this) { messages ->
            if (messages.isEmpty()) {
                adapter.replaceMessages(emptyList())
            } else if (adapter.itemCount == 0) {
                // First load (history)
                messages.forEach { adapter.addMessage(it) }
                binding.rvChat.scrollToPosition(adapter.itemCount - 1)
            } else if (messages.size < adapter.itemCount) {
                // A message was deleted
                adapter.replaceMessages(messages)
            } else if (messages.size > adapter.itemCount) {
                // New message added
                adapter.addMessage(messages.last())
                binding.rvChat.scrollToPosition(adapter.itemCount - 1)
            } else {
                // Same size: Possible content update (Streaming)
                val lastMsg = messages.last()
                adapter.updateLastMessage(lastMsg.text)
                binding.rvChat.scrollToPosition(adapter.itemCount - 1)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnSend.isEnabled = !isLoading
            binding.btnSend.alpha = if (isLoading) 0.5f else 1.0f
        }
    }

    private fun loadContextAndInit() {
        lifecycleScope.launch {
            val db = DataManager.getDataBase(this@ChatActivity)
            val transactions = db.addNewDao().getAllSync()
            val summary = FinancialContextHelper.generateFinancialSummary(transactions)
            val chatDao = db.chatMessageDao()
            viewModel.initAssistant(summary, chatDao)
        }
    }

    private fun checkNetworkAndShowDialog() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoInternetDialog()
        }
    }

    private fun showNoInternetDialog() {
        if (noInternetDialog?.isShowing == true) return

        val dialogBinding = DialogNoInternetBinding.inflate(LayoutInflater.from(this))
        val builder = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)

        noInternetDialog = builder.create()
        noInternetDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnRetry.setOnClickListener {
            if (NetworkUtils.isNetworkAvailable(this)) {
                noInternetDialog?.dismiss()
                noInternetDialog = null
                Toast.makeText(this, "Kết nối đã được khôi phục", Toast.LENGTH_SHORT).show()
                // Automatically scroll to bottom if needed or refresh
            } else {
                Toast.makeText(this, "Vẫn chưa có kết nối mạng", Toast.LENGTH_SHORT).show()
            }
        }

        noInternetDialog?.show()
    }
}
