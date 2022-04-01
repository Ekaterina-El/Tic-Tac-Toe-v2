package el.ka.tictactoe.ui.screens.mainScreen

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import el.ka.tictactoe.R
import el.ka.tictactoe.general.*

class MainScreenViewModel: ViewModel() {
    private val _currentBoardSize = MutableLiveData(3)
    val currentBoardSize: LiveData<Int>
        get() = _currentBoardSize

    fun incCurrentBoardSize() {
        if (_currentBoardSize.value!! < MAX_BOARD_SIZE) {
            _currentBoardSize.value = _currentBoardSize.value!!.plus(1)
        }
    }

    fun decCurrentBoardSize() {
        if (_currentBoardSize.value!! > MIN_BOARD_SIZE) {
            _currentBoardSize.value = _currentBoardSize.value!!.minus(1)
        }
    }

    private fun startGame(gameType: GameType) {
        val bundle = Bundle()
        bundle.putSerializable(GameTypeKey, gameType)
        APP.navController.navigate(R.id.action_mainScreenFragment_to_gameScreenFragment, bundle)
    }

    fun startGameWithRobot() {
        startGame(GameType.Robot)
    }

    fun startGameWithFriend() {
        startGame(GameType.Friend)
    }
}