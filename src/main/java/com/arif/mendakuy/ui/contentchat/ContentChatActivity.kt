package com.arif.mendakuy.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arif.mendakuy.data.model.Messages
import com.arif.mendakuy.databinding.ActivityChatBinding
import com.arif.mendakuy.ui.adapter.MessageAdapter
import com.arif.mendakuy.ui.contentchat.ContentChatViewState


class ContentChatActivity : AppCompatActivity() {
    private lateinit var contentChatViewModel: ContentChatViewModel

    private var _binding: ActivityChatBinding? = null
    private val binding get() = _binding!!

    private val messageAdapter = MessageAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.contentChatViewModel = ContentChatViewModel()

        _binding = ActivityChatBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        onViewCreated()
    }

    private fun onViewCreated() {

        binding.recycleView.adapter = messageAdapter
        binding.recycleView.layoutManager = LinearLayoutManager(this@ContentChatActivity)

        contentChatViewModel.groupId = intent.getStringExtra("groupId")

        contentChatViewModel.getChat()
        contentChatViewModel.state.observe(this@ContentChatActivity) { state ->
            when (state) {
                is ContentChatViewState.ShowChat -> onShowChat(state.showChatContent)
                else -> {}
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSend.setOnClickListener {
            contentChatViewModel.sendMessage(message = binding.etMessage.text.toString())
            binding.etMessage.setText("")
        }
    }

    private fun onShowChat(chatContent: List<Messages>) {
        messageAdapter.setData(chatContent)
    }

}