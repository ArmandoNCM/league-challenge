package life.league.challenge.app.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MainComposition()
        }
    }


    override fun onResume() {
        super.onResume()

        // TODO Move this out
        // example api call to login, delete this and implement the call to login
        // somewhere else differently depending on your chosen architecture
        /*lifecycleScope.launch(Dispatchers.IO) {
            try {
                val account = Service.api.login("hello", "world")
                Log.v(TAG, account.apiKey ?: "")
            } catch (t : Throwable) {
                Log.e(TAG, t.message, t)
            }
        }*/
    }

}
