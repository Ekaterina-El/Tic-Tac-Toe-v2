package el.ka.tictactoe.screens.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import el.ka.tictactoe.general.MAX_BOARD_SIZE
import el.ka.tictactoe.general.MIN_BOARD_SIZE

class MainScreenViewModel: ViewModel() {
    private val _currentBoardSize = MutableLiveData<Int>()
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

    init {
        _currentBoardSize.value = 3
    }
}