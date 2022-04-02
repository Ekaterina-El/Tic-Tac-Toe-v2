package el.ka.tictactoe.ui.screens.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import el.ka.tictactoe.databinding.MainScreenBinding

class MainScreenFragment : Fragment() {
    private lateinit var binding: MainScreenBinding
    private lateinit var viewModel: MainScreenViewModel

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
        binding = MainScreenBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragmentViewModel = viewModel
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
    }

}
