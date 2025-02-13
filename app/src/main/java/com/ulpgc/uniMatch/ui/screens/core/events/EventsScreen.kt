package com.ulpgc.uniMatch.ui.screens.core.events
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.ui.components.ButtonComponent
import com.ulpgc.uniMatch.ui.components.chats.SearchBar
import com.ulpgc.uniMatch.ui.screens.CoreRoutes


@Composable
fun EventsScreen(
    eventViewModel: EventViewModel
) {

    val isSearchActive = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SearchBar(
                    searchText = searchText.value,
                    onSearchTextChange = {
                        searchText.value = it
                    },
                    onArrowBackCallback = {
                        isSearchActive.value = false
                        searchText.value = ""
                    },
                    backgroundColor = MaterialTheme.colorScheme.tertiary
                )
            }

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(text = stringResource(R.string.add_event), color = Color.White)
            }
        }

        EventsList()
    }
}

@Composable
fun EventsList() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        repeat(2) {
            EventCard()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EventCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
//            Image(painter = painterResource(id = R.drawable.event_placeholder), contentDescription = "Event Image")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Asadero EII ULPGC", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("San José del Álamo", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text("VI CAMPUS", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Magenta)
            Text("Friday 25 October 2024", fontSize = 14.sp)
            Text("12:00 - 22:00", fontSize = 14.sp)
        }
    }
}

