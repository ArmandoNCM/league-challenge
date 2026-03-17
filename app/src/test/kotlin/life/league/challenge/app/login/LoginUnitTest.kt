package life.league.challenge.app.login

import android.content.Context
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import life.league.challenge.app.testing.MockAccountRepositoryImpl
import life.league.challenge.domain.model.APIKey
import life.league.challenge.domain.usecase.login.LoginError
import life.league.challenge.domain.usecase.login.LoginResult
import life.league.challenge.domain.usecase.login.LoginUseCase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUnitTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockContext = mockk<Context>(relaxed = true)
    private val mockRepo = MockAccountRepositoryImpl()
    private val loginUseCase = LoginUseCase(mockRepo)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun login_success_emitsSuccessState() = runTest {
        mockRepo.result = LoginResult.Success(APIKey("test-key"))
        val viewModel = MainViewModel(mockContext, loginUseCase)

        viewModel.onUsernameChange("user")
        viewModel.login("pass")
        advanceUntilIdle()

        val state = viewModel.loginState.first()
        Assert.assertTrue(state is LoginUiState.Success)
        Assert.assertEquals(APIKey("test-key"), (state as LoginUiState.Success).apiKey)
    }

    @Test
    fun login_unauthorized_emitsErrorState() = runTest {
        mockRepo.result = LoginResult.Failure(LoginError.Unauthorized)
        val viewModel = MainViewModel(mockContext, loginUseCase)

        viewModel.onUsernameChange("user")
        viewModel.login("pass")
        advanceUntilIdle()

        val state = viewModel.loginState.first()
        Assert.assertTrue(state is LoginUiState.Error)
        Assert.assertEquals("Invalid username or password", (state as LoginUiState.Error).message)
    }

    @Test
    fun login_networkError_emitsErrorState() = runTest {
        mockRepo.result = LoginResult.Failure(LoginError.NetworkError)
        val viewModel = MainViewModel(mockContext, loginUseCase)

        viewModel.onUsernameChange("user")
        viewModel.login("pass")
        advanceUntilIdle()

        val state = viewModel.loginState.first()
        Assert.assertTrue(state is LoginUiState.Error)
        Assert.assertEquals(
            "Network error. Check your connection.",
            (state as LoginUiState.Error).message
        )
    }
}