package life.league.challenge.app.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import life.league.challenge.app.login.LoginComposition
import life.league.challenge.app.navigation.Login

@Composable
fun MainComposition() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginComposition()
        }
    }
}