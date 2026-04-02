package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemChatAiBinding
import com.example.moneymanagement.databinding.ItemChatUserBinding

data class ChatMessageUI(val text: String, val isUser: Boolean, val id: Int? = null)

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<ChatMessageUI>()
    var onLongClick: ((ChatMessageUI) -> Unit)? = null

    companion object {
        private const val TYPE_USER = 1
        private const val TYPE_AI = 2
    }

    fun addMessage(message: ChatMessageUI) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun updateLastMessage(newText: String) {
        if (messages.isNotEmpty()) {
            val lastIndex = messages.size - 1
            val lastMsg = messages[lastIndex]
            messages[lastIndex] = lastMsg.copy(text = newText)
            notifyItemChanged(lastIndex)
        }
    }

    fun replaceMessages(newMessages: List<ChatMessageUI>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    fun removeMessage(message: ChatMessageUI) {
        val index = messages.indexOf(message)
        if (index != -1) {
            messages.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) TYPE_USER else TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_USER) {
            UserViewHolder(ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            AIViewHolder(ItemChatAiBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserViewHolder) holder.bind(message, onLongClick)
        else if (holder is AIViewHolder) holder.bind(message, onLongClick)
    }

    override fun getItemCount(): Int = messages.size

    class UserViewHolder(private val binding: ItemChatUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessageUI, onLongClick: ((ChatMessageUI) -> Unit)?) {
            binding.txtMessage.text = message.text
            binding.root.setOnLongClickListener {
                onLongClick?.invoke(message)
                true
            }
        }
    }

    class AIViewHolder(private val binding: ItemChatAiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessageUI, onLongClick: ((ChatMessageUI) -> Unit)?) {
            binding.txtMessage.text = message.text
            binding.root.setOnLongClickListener {
                onLongClick?.invoke(message)
                true
            }
        }
    }
}
