
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PokemonStatCard(
    hp: Int,
    attack: Int,
    defense: Int,
    specialAttack: Int,
    specialDefense: Int,
    speed: Int
) {
    Column(
        modifier = Modifier
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        StatRow("HP", hp, Color.Green)
        StatRow("Attack", attack, Color.Red)
        StatRow("Defense", defense, Color.Gray)
        StatRow("Special Attack", specialAttack, Color.Yellow)
        StatRow("Special Defense", specialDefense, Color.LightGray)
        StatRow("Speed", speed, Color.Cyan)
    }
}

@Composable
private fun StatRow(statName: String, progress: Int, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$statName: $progress",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.width(16.dp))
        LinearProgressIndicator(
            progress = progress / 200f,
            modifier = Modifier.weight(2f),
            color = color,
        )
    }
}
