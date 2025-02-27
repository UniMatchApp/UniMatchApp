package com.ulpgc.uniMatch.ui.components.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.ui.screens.utils.DateParser
import com.ulpgc.uniMatch.ui.screens.utils.LocationHelper

@Composable
fun EventCard(
    event: Event,
    onEventClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEventClick() },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(event.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(LocationHelper.getAddressFromCoordinates(event.location?.latitude, event.location?.longitude),fontSize = 14.sp, color = Color.Gray)
            Text(DateParser.formatDateToString(event.date), fontSize = 14.sp)
        }
    }
}