package el.ka.tictactoe.ui.screens.gameScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import el.ka.tictactoe.databinding.GameScreenBinding
import el.ka.tictactoe.general.GAME_BOARD_SIZE
import el.ka.tictactoe.general.GameType
import el.ka.tictactoe.general.GAME_TYPE_KEY
import el.ka.tictactoe.ui.customView.GameBoardEventListener
import el.ka.tictactoe.ui.customView.GameBoardView

class GameScreenFragment : Fragment(), GameBoardEventListener {
    private lateinit var gameBoardObserver: Observer<Int>
    private lateinit var viewModel: GameScreenViewModel
    private lateinit var binding: GameScreenBinding

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
        viewModel.gameBoardSize.observe(viewLifecycleOwner, gameBoardObserver)

    }

    override fun onDestroy() {
        viewModel.gameBoardSize.removeObserver(gameBoardObserver)
        super.onDestroy()
    }

    override fun onChangePlayer(player: GameBoardView.Companion.Player) {
        viewModel.setCurrentState(player.toString())
    }
}