package el.ka.tictactoe.ui.screens.gameScreen

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import el.ka.tictactoe.general.APP
import el.ka.tictactoe.general.GameType
import el.ka.tictactoe.general.Player

class GameScreenViewModel: ViewModel() {


    private val _gameType = MutableLiveData(GameType.Robot)
    val gameType: LiveData<GameType>
        get() = _gameType

    private val _scoreX = MutableLiveData<Int>(0)
    val scoreX: LiveData<Int>
        get() = _scoreX

    private val _scoreO = MutableLiveData<Int>(0)
    val scoreO: LiveData<Int>
        get() = _scoreO

    fun makeMove() {
//        when (_currentPlayer.value) {
//            Player.X -> _scoreX.value = _scoreX.value!!.plus(1)
//            Player.O -> _scoreO.value = _scoreO.value!!.plus(1)
//            else -> return
//        }
        _currentPlayer.value = !_currentPlayer.value!!
    }


    private val _currentPlayer = MutableLiveData(Player.X)
    val currentPlayer: MutableLiveData<Player>
        get() = _currentPlayer

    fun goBack() {
        APP.navController.navigateUp()
    }

    fun setGameType(gameType: GameType) {
        _gameType.value = gameType
    }


    private val _gameBoardSize = MutableLiveData(0)
    val gameBoardSize: LiveData<Int>
        get() = _gameBoardSize

    fun setGameBoardSize(gameSize: Int) {
        _gameBoardSize.value = gameSize
    }
}