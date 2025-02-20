package com.ulpgc.uniMatch.ui.components.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun EventSurveyCard(
    title: String = "",
    initialOptions: Map<String, Int> = mapOf("Option 1" to 0, "Option 2" to 0),
    isEditing: Boolean = false,
    onDeleteSurvey: (() -> Unit)? = null,
    onConfirmSurvey: ((String, List<String>) -> Unit)? = null
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var options by remember { mutableStateOf(initialOptions) }
    var editTitle by remember { mutableStateOf(title) }
    var editOptions by remember { mutableStateOf(initialOptions.keys.toList()) }
    var isConfirmed by remember { mutableStateOf(!isEditing) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (!isConfirmed) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (editTitle.isEmpty()) {
                                Text("Enter title", style = TextStyle(fontSize = 18.sp, color = Color.Gray))
                            }
                            innerTextField()
                        }
                    )

                    IconButton(
                        onClick = { onDeleteSurvey?.invoke() },
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete survey")
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                editOptions.forEachIndexed { index, option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BasicTextField(
                            value = option,
                            onValueChange = { newValue ->
                                editOptions = editOptions.toMutableList().also { it[index] = newValue }
                            },
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            editOptions = editOptions.toMutableList().also { it.removeAt(index) }
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete option")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (editOptions.size < 10) {
                        Button(onClick = {
                            editOptions = editOptions + "New Option"
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add option")
                            Text("Add option")
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        isConfirmed = true
                        onConfirmSurvey?.invoke(editTitle, editOptions)
                    }) {
                        Text("Confirm Survey")
                    }
                }
            }
        } else {
            val maxVotes = options.values.maxOrNull() ?: 1
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = editTitle,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    if (isEditing){
                        IconButton(
                            onClick = { onDeleteSurvey?.invoke() },
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete survey")
                        }
                    }
                }
                options.keys.toList().forEachIndexed { index, option ->
                    val votes = options[option] ?: 0
                    val isSelected = option == selectedOption
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                options = options.mapValues { (key, value) ->
                                    when {
                                        key == selectedOption -> value - 1
                                        key == option -> value + 1
                                        else -> value
                                    }
                                }
                                selectedOption = if (isSelected) null else option
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text(option, modifier = Modifier.weight(1f))
                        Text(votes.toString())
                    }
                    val safeMaxVotes = if (maxVotes == 0) 1 else maxVotes

                    LinearProgressIndicator(
                        progress = votes.toFloat() / safeMaxVotes,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50)),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                        trackColor = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}




