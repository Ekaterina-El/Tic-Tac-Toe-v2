package el.ka.tictactoe

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import el.ka.tictactoe.general.initApp

class MainActivity : AppCompatActivity() {
    private lateinit var music: MediaPlayer
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        initApp(this)

        supportActionBar?.hide()

        music = MediaPlayer.create(this, R.raw.sky_puzzle)

    }

    override fun onResume() {
        music.isLooping = true
        music.setVolume(40.0f, 40.0f)
        music.start()
        super.onResume()
    }

    override fun onStop() {
        music.stop()
        music.release()
        super.onStop()
    }
}