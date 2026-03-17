package life.league.challenge.app.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import life.league.challenge.app.di.UseCaseModule
import life.league.challenge.app.main.MainActivity
import life.league.challenge.data.di.RepositoryModule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(RepositoryModule::class, UseCaseModule::class)
class LoginInstrumentedTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun login_happyPath_navigatesToWelcome() {
        composeTestRule.waitUntilAtLeastOneExists(
            hasTestTag("login_username"), timeoutMillis = 5_000
        )

        composeTestRule.onNodeWithTag("login_username").performTextInput("john_doe")
        composeTestRule.onNodeWithTag("login_password").performTextInput("password")
        composeTestRule.onNodeWithTag("login_button").performClick()

        composeTestRule.waitUntilAtLeastOneExists(
            hasTestTag("welcome_message"), timeoutMillis = 10_000
        )
        composeTestRule.onNodeWithTag("welcome_message").assertIsDisplayed()
    }
}
