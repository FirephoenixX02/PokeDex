import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen

class LoadingScreen : Screen {

    @Composable
    override fun Content() {
        val orange = Color(0xFFffa500)
        Box(modifier = Modifier.background(color = orange).fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(128.dp), color = Color.White
                )
                Text(
                    "Fetching data from API...",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.height(200.dp)
                )
            }
        }
    }
}