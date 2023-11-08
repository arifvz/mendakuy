package com.arif.mendakuy.ui.chat

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arif.mendakuy.data.model.Chat
import com.arif.mendakuy.databinding.ActivityChatBinding
import com.arif.mendakuy.databinding.ActivityListChatBinding
import com.arif.mendakuy.ui.adapter.ChatAdapter


class ChatActivity : AppCompatActivity() {
    private lateinit var chatViewModel: ChatViewModel

    private var _binding: ActivityListChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar: ProgressBar

    private val chatAdapter = ChatAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.chatViewModel = ChatViewModel()

        _binding = ActivityListChatBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        onViewCreated()

    }

    private fun onViewCreated() {

        binding.apply {

            recyclerViewChat.adapter = chatAdapter
            recyclerViewChat.layoutManager = LinearLayoutManager(this@ChatActivity)

            chatViewModel.getListChat()

            chatViewModel.state.observe(this@ChatActivity) { state ->
                when (state) {
                    is ChatViewState.ShowListChat -> onShowList(state.listchat)
                    else -> {}
                }
            }

            }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        }


    private fun onShowList(listchat: List<Chat>) {
        chatAdapter.setData(listchat)
    }

}