package life.league.challenge.app.login

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import life.league.challenge.app.R
import life.league.challenge.app.ui.theme.LeagueCodeChallengeTheme

@Composable
fun LoginComposition(
    onLoginSuccess: () -> Unit = {}, viewModel: MainViewModel = hiltViewModel()
) {
    var password by remember { mutableStateOf("") }
    var validationErrorMessage by remember { mutableStateOf<String?>(null) }
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is LoginUiState.Success) {
            onLoginSuccess()
            viewModel.clearLoginSuccess()
        }
    }

    val screenPadding = dimensionResource(R.dimen.login_screen_padding)
    val validationEmptyFields = stringResource(R.string.login_validation_empty_fields)
    val validationEmptyUsername = stringResource(R.string.login_validation_empty_username)
    val validationEmptyPassword = stringResource(R.string.login_validation_empty_password)
    val formWidthFraction = integerResource(R.integer.login_form_width_percent) / 100f
    val gradientPrimaryAlpha = integerResource(R.integer.login_gradient_primary_alpha) / 100f
    val gradientTertiaryAlpha = integerResource(R.integer.login_gradient_tertiary_alpha) / 100f
    val overlayAlpha = integerResource(R.integer.login_loading_overlay_alpha) / 100f
    val progressSize = dimensionResource(R.dimen.login_progress_size)
    val progressStrokeWidth = dimensionResource(R.dimen.login_progress_stroke_width)

    Box(modifier = Modifier.fillMaxSize()) {
        // Gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = gradientPrimaryAlpha),
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = gradientTertiaryAlpha)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding),
            contentAlignment = Alignment.Center
        ) {

            LoginForm(
                username = viewModel.username,
                onUsernameChange = {
                    viewModel.onUsernameChange(it)
                    validationErrorMessage = null
                },
                password = password,
                onPasswordChange = {
                    password = it
                    validationErrorMessage = null
                },
                onLoginClick = {
                    val usernameBlank = viewModel.username.isBlank()
                    val passwordBlank = password.isBlank()
                    if (usernameBlank || passwordBlank) {
                        validationErrorMessage = when {
                            usernameBlank && passwordBlank -> validationEmptyFields
                            usernameBlank -> validationEmptyUsername
                            else -> validationEmptyPassword
                        }
                    } else {
                        validationErrorMessage = null
                        viewModel.login(password)
                        password = ""
                    }
                },
                loginState = loginState,
                validationErrorMessage = validationErrorMessage,
                modifier = Modifier.fillMaxWidth(formWidthFraction)
            )
        }

        if (loginState is LoginUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = overlayAlpha)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(progressSize),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = progressStrokeWidth
                )
            }
        }
    }
}

@Composable
fun LoginForm(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    loginState: LoginUiState,
    modifier: Modifier = Modifier,
    validationErrorMessage: String? = null
) {
    val fieldShape = RoundedCornerShape(dimensionResource(R.dimen.login_field_corner_radius))
    val fieldFocusedAlpha = integerResource(R.integer.login_field_focused_container_alpha) / 100f
    val fieldUnfocusedAlpha =
        integerResource(R.integer.login_field_unfocused_container_alpha) / 100f
    val displayError = validationErrorMessage ?: (loginState as? LoginUiState.Error)?.message
    val errorAlpha by animateFloatAsState(
        targetValue = if (displayError != null) 1f else 0f,
        animationSpec = tween(integerResource(R.integer.login_error_animation_duration_ms)),
        label = "errorAlpha"
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.login_card_corner_radius)),
        color = MaterialTheme.colorScheme.surface.copy(alpha = integerResource(R.integer.login_card_surface_alpha) / 100f),
        shadowElevation = dimensionResource(R.dimen.login_card_elevation),
        tonalElevation = dimensionResource(R.dimen.login_card_tonal_elevation)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.login_card_inner_padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.login_title_subtitle_spacer)))
            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.login_section_spacer)))

            // Error message (validation or login failure)
            if (displayError != null) {
                Surface(
                    shape = RoundedCornerShape(dimensionResource(R.dimen.login_error_chip_corner_radius)),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = errorAlpha)
                ) {
                    Text(
                        text = displayError,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.login_error_chip_padding)),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.login_error_bottom_spacer)))
            }

            // Username field
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text(stringResource(R.string.login_username_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = fieldShape,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = fieldFocusedAlpha),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = fieldUnfocusedAlpha)
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.login_field_spacer)))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(R.string.login_password_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = fieldShape,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = fieldFocusedAlpha),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = fieldUnfocusedAlpha)
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.login_button_top_spacer)))

            // Login button
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.login_button_height)),
                enabled = loginState !is LoginUiState.Loading,
                shape = RoundedCornerShape(dimensionResource(R.dimen.login_button_corner_radius)),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = dimensionResource(R.dimen.login_button_elevation_default),
                    pressedElevation = dimensionResource(R.dimen.login_button_elevation_pressed),
                    disabledElevation = dimensionResource(R.dimen.login_button_elevation_disabled)
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = if (loginState is LoginUiState.Loading) {
                        stringResource(R.string.login_button_loading)
                    } else {
                        stringResource(R.string.login_button)
                    }, style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginFormIdlePreview() {
    LeagueCodeChallengeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp), contentAlignment = Alignment.Center
        ) {
            LoginForm(
                username = "",
                onUsernameChange = {},
                password = "",
                onPasswordChange = {},
                onLoginClick = {},
                loginState = LoginUiState.Idle,
                validationErrorMessage = null,
                modifier = Modifier.fillMaxWidth(0.85f)
            )
        }
    }
}