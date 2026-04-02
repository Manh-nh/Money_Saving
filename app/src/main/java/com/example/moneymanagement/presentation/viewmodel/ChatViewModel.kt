package com.example.moneymanagement.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.presentation.database.roomdb.ChatMessageDao
import com.example.moneymanagement.presentation.database.roomdb.ChatMessageEntity
import com.example.moneymanagement.presentation.view.adapter.ChatMessageUI
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessageUI>>(emptyList())
    val messages: LiveData<List<ChatMessageUI>> = _messages

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var generativeModel: GenerativeModel? = null
    private var chatSession: Chat? = null
    private var systemPrompt: String = ""
    private var chatDao: ChatMessageDao? = null

    fun initAssistant(userDataContext: String, dao: ChatMessageDao) {
        this.systemPrompt = """
            You are a professional Financial Advisor and CFO for the "Money Saving" app. 
            Your goal is to help the user save money, manage their budget, and provide intelligent financial insights.
            
            RULES:
            1. Analyze the provided USER DATA carefully (Income, Expenses, Loans). 
            2. Give specific, data-driven advice based on the transactions provided.
            3. Be professional, concise, and encouraging.
            4. If the user asks non-financial questions, respond briefly but try to steer the conversation back to their financial goals.
            5. Always respond in the same language as the user (Vietnamese or English).
            
            USER DATA CONTEXT:
            $userDataContext
        """.trimIndent()
        this.chatDao = dao
        
        if (_messages.value!!.isEmpty()) {
            viewModelScope.launch {
                val history = dao.getAllMessages()
                if (history.isEmpty()) {
                    val welcomeMsg = "Hello! I am your AI Financial Assistant (Powered by Gemini SDK). I've analyzed your spending and I'm ready to help you save money. How can I assist you today?"
                    _messages.value = _messages.value!! + ChatMessageUI(welcomeMsg, false)
                } else {
                    // Map DB history to UI, including the message ID
                    val uiMessages = history.map { 
                        ChatMessageUI(it.content, it.role == "user", it.id) 
                    }
                    _messages.value = uiMessages
                }
            }
        }
    }

    fun sendMessage(apiKey: String, text: String) {
        if (text.isBlank()) return

        _isLoading.value = true

        viewModelScope.launch {
            if (generativeModel == null) {
                generativeModel = GenerativeModel(
                    modelName = "models/gemini-2.5-flash",
                    apiKey = apiKey,
                    systemInstruction = content { text(systemPrompt) }
                )
                
                val dbHistory = chatDao?.getAllMessages() ?: emptyList()
                val geminiHistory = dbHistory.map { msg ->
                    content(role = msg.role) { text(msg.content) }
                }
                
                chatSession = generativeModel!!.startChat(history = geminiHistory)
            }

            // In version 0.9.0, startChat works this way.
            // When we insert user message, we immediately save it and update UI WITH THE ID
            val userMsgId = chatDao?.insertMessage(ChatMessageEntity(role = "user", content = text))?.toInt()
            _messages.value = _messages.value!! + ChatMessageUI(text, true, userMsgId)

            try {
                // Add an empty AI message to the list first as a placeholder
                val initialList = _messages.value?.toMutableList() ?: mutableListOf()
                val aiMsgIndex = initialList.size
                initialList.add(ChatMessageUI("", false))
                _messages.value = initialList

                var fullResponse = ""
                
                // Use sendMessageStream for "streaming" effect
                chatSession?.sendMessageStream(text)?.collect { chunk ->
                    val chunkText = chunk.text ?: ""
                    
                    // To make it "type like real", we iterate through each character
                    for (char in chunkText) {
                        fullResponse += char
                        
                        // Update the message in the list
                        val updatedList = _messages.value?.toMutableList() ?: mutableListOf()
                        if (aiMsgIndex < updatedList.size) {
                            updatedList[aiMsgIndex] = ChatMessageUI(fullResponse, false)
                            _messages.value = updatedList
                        }
                        
                        // Delay per character for "realism" (Increased for slower effect)
                        delay(40)
                    }
                }
                
                // AI done typing, save full message to DB and get the final ID
                val aiMsgId = chatDao?.insertMessage(ChatMessageEntity(role = "model", content = fullResponse))?.toInt()
                
                // Update the placeholder in the list with the final ID
                val finalList = _messages.value?.toMutableList() ?: mutableListOf()
                if (aiMsgIndex < finalList.size) {
                    finalList[aiMsgIndex] = finalList[aiMsgIndex].copy(id = aiMsgId)
                    _messages.value = finalList
                }

            } catch (e: Exception) {
                val errorMsg = "Lỗi kết nối (SDK): ${e.localizedMessage ?: "Không xác định"}"
                // If an error occurs, we might want to update the latest blank message with error
                val currentList = _messages.value?.toMutableList() ?: mutableListOf()
                if (currentList.isNotEmpty() && currentList.last().text.isEmpty()) {
                    currentList[currentList.size - 1] = ChatMessageUI(errorMsg, false)
                    _messages.value = currentList
                } else {
                    _messages.value = currentList + ChatMessageUI(errorMsg, false)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMessage(message: ChatMessageUI) {
        viewModelScope.launch {
            message.id?.let { id ->
                chatDao?.deleteById(id)
                // Update UI list
                val currentList = _messages.value?.toMutableList() ?: mutableListOf()
                currentList.remove(message)
                _messages.value = currentList
            }
        }
    }
}
