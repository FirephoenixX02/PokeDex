import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.json.JSONObject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import pokedex.composeapp.generated.resources.Res
import pokedex.composeapp.generated.resources.*;

import java.net.URL

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    val pokemap = ArrayList<Pokemon>()
    val apiString = "https://pokeapi.co/api/v2/pokemon/"
    var dataLoaded by mutableStateOf(false)
    val orange = Color(0xFFffa500)

    val pokemonTypeDrawableMap = hashMapOf(
        "normal" to Res.drawable.normal,
        "fire" to Res.drawable.fire,
        "water" to Res.drawable.water,
        "electric" to Res.drawable.electric,
        "grass" to Res.drawable.grass,
        "ice" to Res.drawable.ice,
        "fighting" to Res.drawable.fighting,
        "poison" to Res.drawable.poison,
        "ground" to Res.drawable.ground,
        "flying" to Res.drawable.flying,
        "psychic" to Res.drawable.psychic,
        "bug" to Res.drawable.bug,
        "rock" to Res.drawable.rock,
        "ghost" to Res.drawable.ghost,
        "dragon" to Res.drawable.dragon,
        "dark" to Res.drawable.dark,
        "steel" to Res.drawable.steel,
        "fairy" to Res.drawable.fairy
    )

    MaterialTheme {
        Box(modifier = Modifier.background(color = orange).fillMaxSize()) {
            if (!dataLoaded) {
                println("Should show indicator")
                //For some reason the application calls this code but does not diplay the ui if the launchedeffect is running
                //This should not happen as launched-effect is an async co routine and non blocking
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(128.dp), color = Color.White)
                    Text(
                        "Fetching data from API...",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.height(200.dp)
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 256.dp),
                ) {
                    items(pokemap.size) { index ->
                        Box(modifier = Modifier.size(256.dp).padding(5.dp)) {
                            Canvas(modifier = Modifier.matchParentSize()) {
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
                                        org.jetbrains.compose.resources.painterResource(
                                            pokemonTypeDrawableMap[type]!!
                                        ),
                                        contentDescription = null
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
    //Blocks ui rendering thread?
    LaunchedEffect(Unit) {
        //PokeDex API: https://pokeapi.co/api/v2/pokemon/
        for (i in 1..10) {
            val json = JSONObject(URL(apiString + i).readText());
            val sprites = json.optJSONObject("sprites")
            val types = json.getJSONArray("types")
            val firstType = types.optJSONObject(0)
            val type: String = firstType.optJSONObject("type").optString("name")

            val pokemon = Pokemon(
                json.optString("name"),
                URL(sprites.optString("front_default")),
                type,
                json.optInt("id")
            )

            println(pokemon)

            //println(type)
            pokemap.add(
                pokemon
            )
        }
        dataLoaded = true;
    }

}
