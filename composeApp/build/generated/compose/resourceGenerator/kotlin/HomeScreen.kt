
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.util.Locale

class HomeScreen: Screen {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val orange = Color(0xFFffa500)
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        var searchQuery by remember { mutableStateOf("") }
        val yellow = Color(0xFFFFD700)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = orange),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newName -> searchQuery = newName },
                modifier = Modifier.fillMaxWidth(0.8f)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .background(color = Color.White),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (searchQuery.length >= 3 && PokemonNames.names.any { name -> name.contains(searchQuery.lowercase(
                                    Locale.getDefault())) }) {
                                coroutineScope.launch {
                                    withContext(Dispatchers.IO) {
                                        println("Searching for pokemon, query: $searchQuery")
                                        val names: List<String> = PokemonNames.names.filter { name -> name.contains(searchQuery.lowercase(
                                            Locale.getDefault())) }
                                        val newMap = arrayListOf<Pokemon>()
                                        names.forEach { name ->
                                            loadPokemonDataFromName(name)?.let {
                                                newMap.add(it)
                                            }
                                        }
                                        println(names)
                                        println(newMap)
                                        if (newMap.isNotEmpty()) {
                                            pokemap = newMap
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = yellow,
                    unfocusedBorderColor = yellow,
                    textColor = Color.Black,
                    cursorColor = orange,
                    focusedLabelColor = orange,
                    unfocusedLabelColor = orange,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            //Grid
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