package el.ka.tictactoe.ui.customView

public interface GameBoardEventListener {
    fun onChangePlayer(player: GameBoardView.Companion.Player)
    fun onWin(player: GameBoardView.Companion.Player)
    fun onDraw()
}