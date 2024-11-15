package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.model.TabItem
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.view.CharacterPage
import com.example.myapplication.view.EpisodePage
import com.example.myapplication.viewModel.CharacterViewModel
import com.example.myapplication.viewModel.EpisodeViewModel

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val characterViewModel = ViewModelProvider(this)[CharacterViewModel::class]
        val episodeViewModel = ViewModelProvider(this)[EpisodeViewModel::class]

        val tabitems = listOf(
            TabItem("Personajes", Icons.Outlined.Person, Icons.Filled.Person),
            TabItem("Episodios", Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle)
        )

        setContent {
            MyApplicationTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing) // Respeta las áreas seguras
                ) {
                    var selectedTabIndex by remember {
                        mutableIntStateOf(0)
                    }
                    val pagerState = rememberPagerState { tabitems.size }
                    LaunchedEffect (selectedTabIndex) { pagerState.animateScrollToPage(selectedTabIndex) }
                    LaunchedEffect (pagerState.currentPage) { selectedTabIndex = pagerState.currentPage }

                    Column (modifier = Modifier.fillMaxSize()) {
                        TabRow(selectedTabIndex = selectedTabIndex) {
                            tabitems.forEachIndexed { index, tabItem ->
                                Tab(
                                    selected = index == selectedTabIndex,
                                    onClick = { selectedTabIndex = index},
                                    text = { Text(text = tabItem.title) },
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedTabIndex) { tabItem.selectedIcon } else { tabItem.unselectedIcon },
                                            contentDescription = tabItem.title
                                        )
                                    }
                                )
                            }
                        }
                        when (selectedTabIndex) {
                            0 -> Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                CharacterPage(modifier = Modifier.padding(innerPadding), characterViewModel)
                            }
                            1 -> Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                EpisodePage(modifier = Modifier.padding(innerPadding), episodeViewModel)
                            }
                        }
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize().weight(1f)
                        ) { index ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = tabitems[index].title)
                            }
                        }
                    }
                }
            }
        }
    }
}