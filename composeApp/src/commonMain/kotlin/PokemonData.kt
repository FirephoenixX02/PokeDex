import org.json.JSONObject
import java.util.Locale

data class PokemonData(
    val name: String,
    val type: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
) {
    companion object {
        fun fromJson(json: JSONObject): PokemonData {
            val name = json.optString("name")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val type =
                json.getJSONArray("types").optJSONObject(0).optJSONObject("type").optString("name")
            val baseExperience = json.optInt("base_experience")
            val height = json.optInt("height")
            val weight = json.optInt("weight")
            val hp = json.getJSONArray("stats").optJSONObject(0).optInt("base_stat")
            val attack = json.getJSONArray("stats").optJSONObject(1).optInt("base_stat")
            val defense = json.getJSONArray("stats").optJSONObject(2).optInt("base_stat")
            val specialAttack = json.getJSONArray("stats").optJSONObject(3).optInt("base_stat")
            val specialDefense = json.getJSONArray("stats").optJSONObject(4).optInt("base_stat")
            val speed = json.getJSONArray("stats").optJSONObject(5).optInt("base_stat")

            return PokemonData(
                name = name,
                type = type,
                baseExperience = baseExperience,
                height = height,
                weight = weight,
                hp = hp,
                attack = attack,
                defense = defense,
                specialAttack = specialAttack,
                specialDefense = specialDefense,
                speed = speed
            )
        }
    }
}
