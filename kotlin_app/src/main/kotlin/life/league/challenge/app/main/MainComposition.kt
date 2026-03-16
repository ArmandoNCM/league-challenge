package life.league.challenge.app.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import life.league.challenge.app.R

@Composable
fun MainComposition() {

}

@Composable
fun MainContent() {

    Box(Modifier.fillMaxSize()) {
        Text(
            "Hello World!",
            Modifier
                .align(Alignment.Center)
                .background(colorResource(R.color.colorPrimary))
                .padding(24.dp)
                .background(colorResource(R.color.colorAccent))
        )
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent()
}