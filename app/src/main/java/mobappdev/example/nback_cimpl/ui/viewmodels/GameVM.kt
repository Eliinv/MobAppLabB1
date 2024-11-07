package mobappdev.example.nback_cimpl.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository

/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */
interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBack: Int

    fun setGameType(gameType: GameType)
    fun startGame()
    fun checkMatch()
}

class GameVM(
    private val userPreferencesRepository: UserPreferencesRepository
): GameViewModel, ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int>
        get() = _highscore

    // nBack is currently hardcoded
    override val nBack: Int = 2

    private var job: Job? = null
    private val eventInterval: Long = 2000L

    private val nBackHelper = NBackHelper()
    private var events = emptyArray<Int>()

    override fun setGameType(gameType: GameType) {
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }

    override fun startGame() {
        job?.cancel()
        events = nBackHelper.generateNBackString(10, 9, 30, nBack).toList().toTypedArray()
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}")

        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame()
                GameType.AudioVisual -> runAudioVisualGame()
                GameType.Visual -> runVisualGame(events)
            }
            updateHighscoreIfNeeded()
        }
    }

    private fun updateHighscoreIfNeeded() {
        if (_score.value > _highscore.value) {
            _highscore.value = _score.value
            viewModelScope.launch {
                userPreferencesRepository.saveHighscore(_highscore.value)
            }
        }
    }

    override fun checkMatch() {
        Log.d("GameVM", "checkMatch called for gameType: ${_gameState.value.gameType}")
        when (_gameState.value.gameType) {
            GameType.Visual -> {
                Log.d("GameVM", "Calling checkVisualMatch")
                checkVisualMatch()
            }
            GameType.Audio -> {
                Log.d("GameVM", "Calling checkAudioMatch")
                checkAudioMatch()
            }
            else -> Log.d("GameVM", "Unsupported game type for match checking")
        }
    }

    private fun checkVisualMatch() {
        val currentIndex = _gameState.value.eventValue
        Log.d("GameVM", "checkVisualMatch called with currentIndex: $currentIndex")
        if (currentIndex >= nBack && events[currentIndex] == events[currentIndex - nBack]) {
            _score.value += 1
            updateHighscoreIfNeeded()
            Log.d("GameVM", "Visual match found! Score: ${_score.value}")
        } else {
            Log.d("GameVM", "No visual match found.")
        }
    }

    private fun checkAudioMatch() {
        val currentIndex = _gameState.value.eventValue
        if (currentIndex >= nBack && events[currentIndex] == events[currentIndex - nBack]) {
            _score.value += 1
            updateHighscoreIfNeeded()
        } else {
            Log.d("GameVM", "No audio match found.")
        }
    }

    private fun runAudioGame() {
        viewModelScope.launch {
            for (value in events) {
                _gameState.value = _gameState.value.copy(eventValue = value)
                delay(eventInterval)
            }
            updateHighscoreIfNeeded()
        }
    }

    private suspend fun runVisualGame(events: Array<Int>) {
        for (value in events) {
            _gameState.value = _gameState.value.copy(eventValue = value)
            Log.d("GameVM", "New event value: $value") // Log för att se att eventValue uppdateras
            delay(eventInterval)  // Vänta i 2 sekunder innan nästa händelse visas
        }
        updateHighscoreIfNeeded()
    }

    private fun runAudioVisualGame() {
        // Todo: Implement logic for combined audio-visual game
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application.userPreferencesRespository)
            }
        }
    }

    init {
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect {
                _highscore.value = it
            }
        }
    }
}

enum class GameType {
    Audio,
    Visual,
    AudioVisual
}

data class GameState(
    val gameType: GameType = GameType.Visual,
    val eventValue: Int = -1
)


class FakeVM: GameViewModel{
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()
    override val nBack: Int
        get() = 2

    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun checkMatch() {
    }
}

