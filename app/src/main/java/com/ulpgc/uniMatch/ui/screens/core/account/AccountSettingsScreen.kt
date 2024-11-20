package com.ulpgc.uniMatch.ui.screens.core.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel

@Composable
fun AccountSettingsScreen(userViewModel: UserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 16.dp)
    ) {
        AccountOptionItem(iconId = R.drawable.ic_email, title = stringResource(R.string.change_email), onClick = {})
        AccountOptionItem(iconId = R.drawable.ic_password, title = stringResource(R.string.change_password), onClick = {})
        AccountOptionItem(iconId = R.drawable.ic_arrow_back, title = stringResource(R.string.log_out), onClick = {userViewModel.logout()})
        AccountOptionItem(iconId = R.drawable.ic_delete_account, title = stringResource(R.string.delete_account), onClick = {userViewModel.deleteAccount()})
    }
}

@Composable
fun AccountOptionItem(iconId: Int, title: String, onClick : () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal
        )
    }
}
