import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

class HomeScreen: Screen {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val orange = Color(0xFFffa500)
        val navigator = LocalNavigator.currentOrThrow
        Box(modifier = Modifier.background(color = orange).fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 256.dp),
            ) {
                items(pokemap.size) { index ->
                    Box(modifier = Modifier.size(256.dp).padding(5.dp)) {
                        Canvas(modifier = Modifier.matchParentSize().clickable { navigator.push(DetailScreen(pokemap[index])) }) {
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(0f, 0f),
                                size = Size(size.width, size.height),
                                cornerRadius = CornerRadius(20f, 20f),
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            KamelImage(
                                resource = asyncPainterResource(pokemap[index].imageUrl),
                                modifier = Modifier.size(128.dp),
                                contentDescription = "",
                                alignment = Alignment.Center
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxSize().padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val type: String = pokemap[index].type
                            Box(modifier = Modifier.size(32.dp)) {
                                Image(
                                    painterResource(
                                        pokemonTypeDrawableMap[type]!!
                                    ), contentDescription = null
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = pokemap[index].name,
                                textAlign = TextAlign.Center,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

}