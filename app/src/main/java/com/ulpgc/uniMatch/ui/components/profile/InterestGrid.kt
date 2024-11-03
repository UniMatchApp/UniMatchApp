package com.ulpgc.uniMatch.ui.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.theme.MainColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestGrid(
    initialInterestList: List<String>,
    onInterestChange: (String, Boolean) -> Unit) {

    val selectedInterests = remember { mutableStateListOf<String>().apply { addAll(initialInterestList) } }
    val context = LocalContext.current
    val interests = context.resources.getStringArray(R.array.interests).toList()

    FlowRow (
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        interests.forEach { interest ->
            val isSelected = selectedInterests.contains(interest)

            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) MainColor else Color.Gray.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .clickable {
                        if (isSelected) {
                            selectedInterests.remove(interest)
                            onInterestChange(interest, false)
                        } else {
                            selectedInterests.add(interest)
                            onInterestChange(interest, true)
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = interest,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) Color.Black else Color.White
                )
            }
        }
    }
}
