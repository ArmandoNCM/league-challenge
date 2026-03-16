package life.league.challenge.app.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import life.league.challenge.domain.usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var username by mutableStateOf("")
        private set

    fun onUsernameChange(value: String) {
        username = value
    }

    fun login(password: String) {
        viewModelScope.launch {
            val charArray = password.toCharArray()
            try {
                loginUseCase(username, charArray)
            } finally {
                charArray.fill('\u0000')
            }
        }
    }
}
