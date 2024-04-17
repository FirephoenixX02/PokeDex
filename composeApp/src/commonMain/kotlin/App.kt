import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.json.JSONObject
import pokedex.composeapp.generated.resources.Res
import pokedex.composeapp.generated.resources.bug
import pokedex.composeapp.generated.resources.dark
import pokedex.composeapp.generated.resources.dragon
import pokedex.composeapp.generated.resources.electric
import pokedex.composeapp.generated.resources.fairy
import pokedex.composeapp.generated.resources.fighting
import pokedex.composeapp.generated.resources.fire
import pokedex.composeapp.generated.resources.flying
import pokedex.composeapp.generated.resources.ghost
import pokedex.composeapp.generated.resources.grass
import pokedex.composeapp.generated.resources.ground
import pokedex.composeapp.generated.resources.ice
import pokedex.composeapp.generated.resources.normal
import pokedex.composeapp.generated.resources.poison
import pokedex.composeapp.generated.resources.psychic
import pokedex.composeapp.generated.resources.rock
import pokedex.composeapp.generated.resources.steel
import pokedex.composeapp.generated.resources.water
import java.net.URL
import java.util.Locale

var pokemap by mutableStateOf<ArrayList<Pokemon>>(arrayListOf())
val apiString = "https://pokeapi.co/api/v2/pokemon/"

@OptIn(ExperimentalResourceApi::class)
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

@Composable
@Preview
fun App() {
    var dataLoaded by remember { mutableStateOf(false) }

    MaterialTheme {
        if (!dataLoaded) {
            Navigator(screen = LoadingScreen()) { navigator ->
                FadeTransition(navigator)
            }

            // Load data asynchronously
            LaunchedEffect(Unit) {
                pokemap = withContext(Dispatchers.IO) {
                    loadPokemonData(1, 10)
                } as ArrayList<Pokemon>

                dataLoaded = true
            }

        } else {
            println("Pokemon should show up any second!")
            //Display Pokemon Grid
            Navigator(screen = HomeScreen()) { navigator ->
                SlideTransition(navigator)
            }
            print(PokemonNames.names)
        }
    }
}

// Function to load Pokemon data asynchronously, leave suspend even if IDE complains
suspend fun loadPokemonData(startId: Int, endId: Int): List<Pokemon> {
    val pokemap = ArrayList<Pokemon>()
    for (i in startId..endId) {
        val json = JSONObject(URL(apiString + i).readText());
        val sprites = json.optJSONObject("sprites")
        val type: String =
            json.getJSONArray("types").optJSONObject(0).optJSONObject("type")?.optString("name") ?: "null"
        val name: String = json.optString("name")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        val pokemon = Pokemon(
            name, URL(sprites?.optString("front_default") ?: "null"), type, json.optInt("id")
        )

        pokemap.add(pokemon)
    }
    return pokemap
}

suspend fun loadPokemonDataFromName(name: String): Pokemon? {
    val trimmedName = name.trim()
    println("fetching api, name: $name")
    if (trimmedName == "") return null
    var json: JSONObject
    try {
         json = JSONObject(URL(apiString + trimmedName).readText());
    } catch (e: Exception) {
        println(e)
        return null
    }
    val sprites = json.optJSONObject("sprites")
    val type: String =
        json.getJSONArray("types").optJSONObject(0).optJSONObject("type")?.optString("name") ?: "null"
    val name: String = json.optString("name")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    val pokemon = Pokemon(
        name, URL(sprites?.optString("front_default") ?: "null"), type, json.optInt("id")
    )

    val pokemap = ArrayList<Pokemon>()

    pokemap.add(pokemon)

    return pokemon
}