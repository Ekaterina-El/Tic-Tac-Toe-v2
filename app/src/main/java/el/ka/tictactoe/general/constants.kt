package el.ka.tictactoe.general

import el.ka.tictactoe.MainActivity

const val MAX_BOARD_SIZE = 10
const val MIN_BOARD_SIZE = 3

lateinit var APP: MainActivity

fun initApp(mainApp: MainActivity) {
    APP = mainApp
}

enum class GameType(val index: Int) {
    Robot(1),
    Friend(2)
}

enum class Player {
    X,
    O;

    operator fun not(): Player {
        return when(this) {
            X -> O
            O -> X
            else -> X
        }
    }
}
const val GAME_TYPE_KEY = "GameType"
const val GAME_BOARD_SIZE = "GameBoardSize"