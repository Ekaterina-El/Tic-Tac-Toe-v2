package el.ka.tictactoe.ui.screens.gameScreen

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import el.ka.tictactoe.R
import el.ka.tictactoe.databinding.GameScreenBinding
import el.ka.tictactoe.general.APP
import el.ka.tictactoe.general.GAME_BOARD_SIZE
import el.ka.tictactoe.general.GAME_TYPE_KEY
import el.ka.tictactoe.ui.customView.GameBoardEventListener
import el.ka.tictactoe.ui.customView.GameBoardView.Companion.GameType
import el.ka.tictactoe.ui.customView.GameBoardView.Companion.Player

class GameScreenFragment : Fragment(), GameBoardEventListener {
    private lateinit var gameBoardObserver: Observer<Int>

    private lateinit var viewModel: GameScreenViewModel
    private lateinit var binding: GameScreenBinding

    private lateinit var endGameDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createBinding(inflater)
        return binding.root
    }

    private fun createBinding(inflater: LayoutInflater) {
        createViewModel()
        initBinding(inflater)
    }

    private fun initBinding(inflater: LayoutInflater) {
        binding = GameScreenBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragmentViewModel = viewModel
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(this).get(GameScreenViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameType = requireArguments().getSerializable(GAME_TYPE_KEY) as GameType
        val gameSize = requireArguments().getInt(GAME_BOARD_SIZE)

        viewModel.setGameBoardSize(gameSize)
        viewModel.setGameType(gameType)

        gameBoardObserver = Observer<Int> { value ->
            binding.gameBoard.setCountOfCells(value)
        }
        binding.gameBoard.setEventListener(this)
        binding.gameBoard.setGameType(gameType)
        viewModel.gameBoardSize.observe(viewLifecycleOwner, gameBoardObserver)

    }

    override fun onDestroy() {
        viewModel.gameBoardSize.removeObserver(gameBoardObserver)
        super.onDestroy()
    }

    override fun onChangePlayer(player: Player) {
        viewModel.setCurrentState(player.toString())
    }

    override fun onWin(player: Player) {
        viewModel.setCurrentState(
            "$player ${this.getString(R.string.won_this_game)}"
        )
        viewModel.updateScoreFor(player)

        if (viewModel.scoreO.value!! == 3) {
            showEndGameDialog(Player.O)
        } else if (viewModel.scoreX.value!! == 3) {
            showEndGameDialog(Player.X)
        }
    }

    override fun onDraw() {
        viewModel.setCurrentState(this.getString(R.string.isDraw))
    }


    private fun showEndGameDialog(player: Player) {
        endGameDialog = Dialog(APP)
        endGameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        endGameDialog.setContentView(R.layout.game_end_dialog)

        endGameDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        endGameDialog.setCancelable(false)
        endGameDialog.show()

        endGameDialog.findViewById<TextView>(R.id.whoWin).text =
            getString(R.string.win, player.toString())
        endGameDialog.findViewById<LinearLayout>(R.id.dialog_button_quit).setOnClickListener {
            endGameDialog.dismiss()
            viewModel.goBack()
        }
    }
}