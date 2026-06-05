package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.JokeResponse
import com.example.repository.JokeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JokeViewModel : ViewModel() {
    private val jokeRepository = JokeRepository()

    private val _jokeState = MutableStateFlow<JokeUiState>(JokeUiState.Initial)
    val jokeState: StateFlow<JokeUiState> = _jokeState.asStateFlow()

    private val _jokesHistory = MutableStateFlow<List<JokeResponse>>(emptyList())
    val jokesHistory: StateFlow<List<JokeResponse>> = _jokesHistory.asStateFlow()

    fun getRandomJoke() {
        viewModelScope.launch {
            _jokeState.value = JokeUiState.Loading
            try {
                val joke = jokeRepository.getRandomJoke()
                _jokeState.value = JokeUiState.Success(joke)
                // Add to history
                _jokesHistory.value = _jokesHistory.value + joke
            } catch (e: Exception) {
                _jokeState.value = JokeUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun clearHistory() {
        _jokesHistory.value = emptyList()
    }

    fun resetState() {
        _jokeState.value = JokeUiState.Initial
    }
}

sealed class JokeUiState {
    object Initial : JokeUiState()
    object Loading : JokeUiState()
    data class Success(val joke: JokeResponse) : JokeUiState()
    data class Error(val message: String) : JokeUiState()
}
