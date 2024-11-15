package com.example.myapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.myapplication.view.common.TextLarge
import com.example.myapplication.view.common.TextMedium
import com.example.myapplication.view.common.TextSmall
import com.example.myapplication.viewModel.EpisodeViewModel


@Composable
fun EpisodePage(modifier: Modifier = Modifier, viewModel: EpisodeViewModel) {
    val characterData = viewModel.episodeData.observeAsState(emptyList())
    val isLoading = viewModel.isLoading.observeAsState(false)

    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        viewModel.getEpisodeData()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItem ->
                val totalItems = characterData.value?.size ?: 0
                if (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 4 && !isLoading.value) {
                    viewModel.getEpisodeData()
                }
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
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            characterData.value?.let { listCharacter ->
                items(listCharacter) {
                    CardList(modifier) {
                        TextLarge(it.name)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextMedium("Episode: ${it.episode}")
                        TextMedium("Air Date: ${it.airDate}")
                        Spacer(modifier = Modifier.height(8.dp))
                        TextMedium("Characters:")
                        it.characters.forEach { item ->
                            TextSmall(item)
                        }
                    }
                }
                if (isLoading.value) {
                    item { CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally)) }
                }
            }
        }
    }
}