package com.example.myapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.myapplication.view.common.CardList
import com.example.myapplication.view.common.ImageByUrl
import com.example.myapplication.view.common.TextMedium
import com.example.myapplication.view.common.TextSmall
import com.example.myapplication.viewModel.CharacterViewModel


@Composable
fun CharacterPage(modifier: Modifier = Modifier, viewModel: CharacterViewModel) {
    val characterData = viewModel.characterData.observeAsState(emptyList())
    val isLoading = viewModel.isLoading.observeAsState(false)

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.getCharacterData()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItem ->
                val totalItems = characterData.value?.size ?: 0
                if (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 4 && !isLoading.value) {
                    viewModel.getCharacterData()
                }
            }
    }

    LaunchedEffect(characterData.value) {
        listState.layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastVisibleItem ->
            listState.scrollToItem(lastVisibleItem.index)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            characterData.value?.let { listCharacter ->
                items(listCharacter) {
                    CardList(modifier) {
                        ImageByUrl(it.image, it.name)
                        Spacer(modifier = Modifier.height(16.dp))
                        TextMedium(it.name)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextSmall("Species: ${it.species}")
                        TextSmall("Gender: ${it.gender}")
                    }
                }
                if (isLoading.value) {
                    item { CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally)) }
                }
            }
        }
    }
}
