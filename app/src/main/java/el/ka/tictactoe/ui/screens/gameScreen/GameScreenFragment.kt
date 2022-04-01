package el.ka.tictactoe.ui.screens.gameScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import el.ka.tictactoe.databinding.GameScreenBinding
import el.ka.tictactoe.general.GameType
import el.ka.tictactoe.general.GameTypeKey

class GameScreenFragment : Fragment() {
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

        val gameType = requireArguments().getSerializable(GameTypeKey) as GameType
        viewModel.setGameType(gameType)
    }
}