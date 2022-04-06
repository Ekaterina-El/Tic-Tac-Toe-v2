package el.ka.tictactoe.ui.screens.gameScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import el.ka.tictactoe.R
import el.ka.tictactoe.general.APP
import el.ka.tictactoe.general.GameType
import el.ka.tictactoe.ui.customView.GameBoardView
import el.ka.tictactoe.ui.customView.GameBoardView.Companion.Player.O
import el.ka.tictactoe.ui.customView.GameBoardView.Companion.Player.X

class GameScreenViewModel(application: Application) : AndroidViewModel(application) {


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
//        _currentPlayer.value = !_currentPlayer.value!!
    }


    private val _currentState = MutableLiveData("Player $X")
    val currentState: MutableLiveData<String>
        get() = _currentState

    fun setCurrentState(state: String) {
        if (state == O.toString() || state == X.toString()) {
            val res = getApplication<Application>().resources
            _currentState.value = "${res.getString(R.string.player)} $state"
            return
        }
        _currentState.value = state
    }

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

    fun updateScoreFor(player: GameBoardView.Companion.Player) {
        when (player) {
            X -> _scoreX.value = _scoreX.value!!.plus(1)
            O -> _scoreO.value = _scoreO.value!!.plus(1)
        }

    }


}