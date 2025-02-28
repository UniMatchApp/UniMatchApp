package com.ulpgc.uniMatch.ui.components.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R


@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onArrowBackCallback: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back),
            tint = Color.Black,
            modifier = Modifier
                .clickable { onArrowBackCallback() }
                .padding(end = 8.dp)
                .size(24.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (searchText.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.search),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }

            BasicTextField(
                value = searchText,
                onValueChange = {
                    if (it.length <= 25) onSearchTextChange(it)
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

        }

    }
}
