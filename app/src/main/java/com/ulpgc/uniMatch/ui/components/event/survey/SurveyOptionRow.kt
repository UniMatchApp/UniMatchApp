package com.ulpgc.uniMatch.ui.components.event.survey

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R

@Composable
fun SurveyOptionRow(
    option: String,
    votes: Int = 0,
    isSelected: Boolean = false,
    maxVotes: Int = 0,
    onVote: (String) -> Unit = {},
    isEditing: Boolean = false,
    onOptionChange: (String) -> Unit = {},
    onOptionRemove: () -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if(isEditing == false) {
            RadioButton(
                selected = isSelected,
                onClick = { onVote(option) },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
            )
            Text(option, modifier = Modifier.weight(1f))
            Text(votes.toString())
        } else {
            BasicTextField(
                value = option,
                onValueChange = { newValue -> onOptionChange(newValue) },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onOptionRemove() }) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = stringResource(R.string.delete_option))
            }
        }

    }
    val safeMaxVotes = if (maxVotes == 0) 1 else maxVotes

    if(isEditing == false) {
        LinearProgressIndicator(
            progress = votes.toFloat() / safeMaxVotes,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
            trackColor = Color.LightGray
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
