package life.league.challenge.app.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import life.league.challenge.app.R
import life.league.challenge.domain.model.APIKey
import life.league.challenge.domain.usecase.login.LoginError
import life.league.challenge.domain.usecase.login.LoginResult
import life.league.challenge.domain.usecase.login.LoginUseCase
import javax.inject.Inject


sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val apiKey: APIKey) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context, private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    var username by mutableStateOf("")
        private set

    fun onUsernameChange(value: String) {
        username = value
    }

    fun login(password: String) {
        viewModelScope.launch {
            clearLoginError()
            val charArray = password.toCharArray()
            try {
                _loginState.value = LoginUiState.Loading
                when (val result = loginUseCase(username, charArray)) {
                    is LoginResult.Success -> _loginState.value =
                        LoginUiState.Success(result.apiKey)

                    is LoginResult.Failure -> _loginState.value =
                        LoginUiState.Error(loginErrorMessage(result.error))
                }
            } finally {
                charArray.fill('\u0000')
            }
        }
    }

    fun clearLoginError() {
        if (_loginState.value is LoginUiState.Error) {
            _loginState.value = LoginUiState.Idle
        }
    }

    fun clearLoginSuccess() {
        if (_loginState.value is LoginUiState.Success) {
            _loginState.value = LoginUiState.Idle
        }
    }

    private fun loginErrorMessage(error: LoginError): String = when (error) {
        LoginError.Unauthorized -> context.getString(R.string.login_error_unauthorized)
        LoginError.NetworkError -> context.getString(R.string.login_error_network)
        LoginError.ServerError -> context.getString(R.string.login_error_server)
        LoginError.InvalidResponse -> context.getString(R.string.login_error_invalid_response)
        LoginError.Unknown -> context.getString(R.string.login_error_unknown)
    }
}
