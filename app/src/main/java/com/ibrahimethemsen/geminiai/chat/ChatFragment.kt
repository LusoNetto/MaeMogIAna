package com.ibrahimethemsen.geminiai.chat

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.ibrahimethemsen.geminiai.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding : FragmentChatBinding? = null
    private val binding : FragmentChatBinding get() = _binding!!
    private val viewModel by viewModels<ChatViewModel>()
    private val chatAdapter = ChatAdapter()
    private val messageList = mutableListOf<Pair<String, Int>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(messageList.size == 0){
            messageList.add(Pair("Olá, voce tem uma consulta para daqui uma semana " +
                    "no posto da UBS de Jundiapeba, será no dia 23 de Abril de 2024 as 17:00 horas com o pediatra" +
                    " Marcelo, voce tem que levar o documento de identidade. Posso ajudar em alguma coisa?", ChatAdapter.VIEW_TYPE_GEMINI))
            chatAdapter.setMessages(messageList)
        }
        //createQuestion("Responda", "Você está bem?", null)
        _binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        sendMessage()
        observe()
    }
    private fun setAdapter(){
        binding.chatRv.adapter = chatAdapter
    }

    private fun sendMessage(){
        binding.chatSend.setOnClickListener {
            val userMessage = binding.chatPromptTextEt.text.toString()
            viewModel.geminiChat(userMessage, messageList.size == 1)
            messageList.add(Pair(userMessage,ChatAdapter.VIEW_TYPE_USER))
            chatAdapter.setMessages(messageList)
            scrollPosition()
            binding.chatPromptTextEt.setText("")
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun observe(){
        viewModel.messageResponse.observe(viewLifecycleOwner){content ->
            content.text?.let {
                if (messageList[messageList.size - 1].second == ChatAdapter.VIEW_TYPE_USER) {
                    messageList.add(Pair(it,ChatAdapter.VIEW_TYPE_GEMINI))
                    chatAdapter.setMessages(messageList)
                    scrollPosition()
                }

            }
        }
    }

    private fun scrollPosition(){
        binding.chatRv.smoothScrollToPosition(chatAdapter.itemCount - 1)

    }
    private fun createQuestion(title: String, description: String, next: (() -> Void)?) {
        fun after() {
            next?.invoke()
        }
        AlertDialog
            .Builder(this as Context)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton("Sim", DialogInterface.OnClickListener {
                dialog, id -> after()
            })
            .setNegativeButton("Não", DialogInterface.OnClickListener {
                dialog, id -> after()
            })
            .create()
            .show()
    }

}