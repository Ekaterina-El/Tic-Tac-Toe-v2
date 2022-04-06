package el.ka.tictactoe.ui.customView

public interface GameBoardEventListener {
    public fun onChangePlayer(player: GameBoardView.Companion.Player)
    public fun onWin(player: GameBoardView.Companion.Player)
}