package com.ulpgc.uniMatch.ui.screens.core.policies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.components.policies.TextSection

@Composable
fun PrivacyPolicyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        TextSection(
            title = stringResource(id = R.string.information_collection_title),
            content = stringResource(id = R.string.information_collection_content)
        )

        TextSection(
            title = stringResource(id = R.string.use_of_information_title),
            content = stringResource(id = R.string.use_of_information_content)
        )

        TextSection(
            title = stringResource(id = R.string.sharing_of_information_title),
            content = stringResource(id = R.string.sharing_of_information_content)
        )

        TextSection(
            title = stringResource(id = R.string.data_security_title),
            content = stringResource(id = R.string.data_security_content)
        )

        TextSection(
            title = stringResource(id = R.string.user_rights_title),
            content = stringResource(id = R.string.user_rights_content)
        )

        TextSection(
            title = stringResource(id = R.string.changes_to_policy_title),
            content = stringResource(id = R.string.changes_to_policy_content)
        )
    }
}
