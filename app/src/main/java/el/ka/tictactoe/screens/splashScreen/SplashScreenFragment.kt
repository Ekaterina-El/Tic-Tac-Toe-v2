package el.ka.tictactoe.screens.splashScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import el.ka.tictactoe.R

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment: Fragment(R.layout.splash_screen) {
    private lateinit var viewModel: SplashScreenViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SplashScreenViewModel::class.java)
        viewModel.loadApp()
    }

}