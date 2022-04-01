package el.ka.tictactoe.screens.gameScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import el.ka.tictactoe.general.APP
import el.ka.tictactoe.general.GameType

class GameScreenViewModel: ViewModel() {
    private val _gameType = MutableLiveData(GameType.Robot)
    val gameType: LiveData<GameType>
        get() = _gameType


    fun goBack() {
        APP.navController.navigateUp()
    }

    fun setGameType(gameType: GameType) {
        _gameType.value = gameType
    }
}