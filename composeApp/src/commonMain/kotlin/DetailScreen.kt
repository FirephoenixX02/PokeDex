import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            pokemonData = withContext(Dispatchers.IO) {
                fetchPokemonDetails()
            }
            dataLoaded = true
        }

        if (!dataLoaded) {
            Box(
                modifier = Modifier.background(color = Color.White).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Fetching Data from API...")
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
            }
        } else {
            Column(
                modifier = Modifier.background(color = Color.White).fillMaxSize().padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.align(Alignment.Start)) {
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
                }

                // Image
                KamelImage(
                    resource = asyncPainterResource(pokemon.imageUrl),
                    modifier = Modifier.size(256.dp),
                    contentDescription = "",
                    alignment = Alignment.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Details card
                Card(
                    modifier = Modifier.background(color = Color.White).fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        detailRow("Name:", pokemonData?.name ?: "")
                        detailRow("Type:", pokemonData?.type?.capitalize() ?: "")
                        detailRow("Base XP:", pokemonData?.baseExperience.toString())
                        detailRow("Height:", pokemonData?.height.toString())
                        detailRow("Weight:", pokemonData?.weight.toString() + "kg")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stat card
                PokemonStatCard(
                    hp = pokemonData?.hp ?: 0,
                    attack = pokemonData?.attack ?: 0,
                    defense = pokemonData?.defense ?: 0,
                    specialAttack = pokemonData?.specialAttack ?: 0,
                    specialDefense = pokemonData?.specialDefense ?: 0,
                    speed = pokemonData?.speed ?: 0
                )
            }
        }
    }

    private suspend fun fetchPokemonDetails(): PokemonData {
        val apiString = "https://pokeapi.co/api/v2/pokemon/${pokemon.name.lowercase()}"
        val json = JSONObject(URL(apiString).readText())
        return PokemonData.fromJson(json)
    }
}

@Composable
private fun detailRow(statName: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = statName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1 // Adjust text style as needed
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1 // Adjust text style as needed
        )
    }
}
