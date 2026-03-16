package life.league.challenge.app.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import life.league.challenge.domain.usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel()
