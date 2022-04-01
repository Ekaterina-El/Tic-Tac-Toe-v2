package el.ka.tictactoe.ui.screens.splashScreen

import androidx.lifecycle.ViewModel
import el.ka.tictactoe.R
import el.ka.tictactoe.general.APP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SplashScreenViewModel: ViewModel() {

    fun loadApp() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            // Todo (Добавить прогресс-бар загрузки)
            APP.navController.navigate(R.id.action_splashScreenFragment_to_mainScreenFragment)
        }
    }
}