package ruotas.pokedex.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class PokemonListViewModel @Inject constructor() : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList.asStateFlow()

    fun getPokemonList() {
        viewModelScope.launch {
            val pokemonList = withContext(Dispatchers.IO) {
                PokeApiClient().getPokemonList(offset = 0, limit = 20).results
//                PokeApiClient().getPokemonSpecies(20)
            }
//            _pokemonList.value = pokemonList.map { it.toPokemon() }
//            val test = pokemonList.names.find { it.language.name == "ja" }
//            Log.d("test", test.toString())
            _pokemonList.value = pokemonList.map {
                async { it.toPokemon() }
            }.awaitAll() // awaitAll で全ての結果を取得
        }
    }

    private suspend fun NamedApiResource.toPokemon(): Pokemon = withContext(Dispatchers.IO) {
        val pokemon = PokeApiClient().getPokemon(id)
        Pokemon(
            name = pokemon.name,
            imageUrl = pokemon.sprites.frontDefault
        )
    }
}

data class Pokemon(
    val name: String,
    val imageUrl: String?
)