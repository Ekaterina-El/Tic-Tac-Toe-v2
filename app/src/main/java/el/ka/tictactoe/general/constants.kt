package el.ka.tictactoe.general

import el.ka.tictactoe.MainActivity

const val MAX_BOARD_SIZE = 10
const val MIN_BOARD_SIZE = 3

lateinit var APP: MainActivity

fun initApp(mainApp: MainActivity) {
    APP = mainApp
}
const val GAME_TYPE_KEY = "GameType"
const val GAME_BOARD_SIZE = "GameBoardSize"