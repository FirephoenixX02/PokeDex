import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.json.JSONObject
import java.net.URL

data class DetailScreen(
    val pokemon: Pokemon
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val orange = Color(0xFFffa500)
        var dataLoaded by remember { mutableStateOf(false) }

        var pokemonData by remember { mutableStateOf<PokemonData?>(null) }

        LaunchedEffect(Unit) {
            pokemonData = fetchPokemonDetails();
            dataLoaded = true;
        }

        LaunchedEffect(dataLoaded, this) {
            if (dataLoaded) {
                navigator.push(this@DetailScreen)
            }
        }

        if (!dataLoaded) {
            navigator.push(LoadingScreen())
        } else {
            Box(
                modifier = Modifier.background(color = Color.White).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                //Back Button
                Button(
                    onClick = { navigator.popAll(); navigator.push(HomeScreen()) },
                    modifier = Modifier.align(Alignment.TopStart),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = orange, contentColor = Color.White
                    )
                ) {
                    Text("Home")
                }
                //Details
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KamelImage(
                        resource = asyncPainterResource(pokemon.imageUrl),
                        modifier = Modifier.size(256.dp),
                        contentDescription = "",
                        alignment = Alignment.Center
                    )
                }
                //Details
                Box(
                    modifier = Modifier.size(256.dp).padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawRoundRect(
                            color = Color.White,
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(20f, 20f),
                        )
                        drawRoundRect(
                            color = Color.Black,
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(20f, 20f),
                            style = Stroke(1.dp.toPx())
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Name: ${pokemonData?.name}",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Type: ${pokemonData?.type}",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Base XP: ${pokemonData?.baseExperience}",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Height: ${pokemonData?.height}",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Weight: ${pokemonData?.weight}kg",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                //Stats + Progress Bars
                Box(
                    modifier = Modifier.size(512.dp).padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {

                }
            }
        }
    }

    //Leave suspend
    suspend fun fetchPokemonDetails(): PokemonData {
        val apiString = "https://pokeapi.co/api/v2/pokemon/${pokemon.name.lowercase()}"
        val json = JSONObject(URL(apiString).readText())
       return PokemonData.fromJson(json)
    }
}