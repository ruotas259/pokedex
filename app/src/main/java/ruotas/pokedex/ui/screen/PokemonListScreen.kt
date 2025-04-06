package ruotas.pokedex.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import ruotas.pokedex.ui.viewmodel.Pokemon
import ruotas.pokedex.ui.viewmodel.PokemonListViewModel

class PokemonListScreen {
    @Composable
    fun PokemonList(viewModel: PokemonListViewModel = viewModel()) {
        val pokemonList by viewModel.pokemonList.collectAsStateWithLifecycle()

        PokemonGrid(pokemonList)

        DisposableEffect(Unit) {
            viewModel.getPokemonList()
            onDispose {
                // Cleanup if needed
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PokemonGrid(pokemonList: List<Pokemon>) {
        var selectedPokemon by remember { mutableStateOf<Pokemon?>(null) }
        var showBottomSheet by remember { mutableStateOf(false) }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = Color.White // or any other color
            ) {
                selectedPokemon?.let { pokemon ->
                    PokemonDetailScreen(pokemon)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .background(Color.Transparent)
            ) {
                items(pokemonList) { pokemon ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                selectedPokemon = pokemon
                                showBottomSheet = true
                            }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(pokemon.imageUrl),
                            contentDescription = pokemon.name,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(text = pokemon.name)
                    }
                }
            }
        }
    }

    // 詳細画面用のコンポーザブル関数
    @Composable
    fun PokemonDetailScreen(pokemon: Pokemon) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = pokemon.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberAsyncImagePainter(pokemon.imageUrl),
                contentDescription = pokemon.name,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}