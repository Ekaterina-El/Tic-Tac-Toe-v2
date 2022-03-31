package el.ka.tictactoe.screens.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import el.ka.tictactoe.databinding.MainScreenBinding

class MainScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        val binding = MainScreenBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragmentViewModel = viewModel

        return binding.root
    }
}