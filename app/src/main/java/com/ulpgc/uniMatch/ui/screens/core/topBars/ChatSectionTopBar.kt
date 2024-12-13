package com.ulpgc.uniMatch.ui.screens.core.topBars

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.ui.components.chats.SearchBar
import com.ulpgc.uniMatch.ui.theme.AppPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSectionTopBar(
    chatViewModel: ChatViewModel
) {
    val isSearchActive = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!isSearchActive.value) {
                    TopBarTitle()
                } else {
                    SearchBar(
                        searchText = searchText.value,
                        onSearchTextChange = {
                            searchText.value = it
                            chatViewModel.filterChats(it)
                        },
                        onArrowBackCallback = {
                            isSearchActive.value = false
                            searchText.value = ""
                            chatViewModel.filterChats("")
                        }
                    )
                }
            }
        },
        actions = {
            // Icono de búsqueda a la derecha, visible solo cuando la búsqueda no está activa
            if (!isSearchActive.value) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search),
                    tint = Color.Black,
                    modifier = Modifier
                        .clickable {
                            isSearchActive.value = true
                        }
                        .padding(end = 8.dp)
                        .size(24.dp)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TopBarTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.unimatch_logo),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = stringResource(id = R.string.chat),
            color = Color.Black,
            modifier = Modifier.padding(start = AppPadding.Small)
        )
    }
}
