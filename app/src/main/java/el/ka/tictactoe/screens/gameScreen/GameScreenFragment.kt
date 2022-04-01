package el.ka.tictactoe.screens.gameScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import el.ka.tictactoe.R
import el.ka.tictactoe.general.GameTypeKey

class GameScreenFragment: Fragment(R.layout.game_screen) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameType = requireArguments().getSerializable(GameTypeKey)
        val a = 1
    }
}