package com.ulpgc.uniMatch.ui.screens.core.policies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.components.policies.TextSection
import com.ulpgc.uniMatch.ui.screens.core.topBars.PoliciesTopBar

@Composable
fun CookiesPolicyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.cookies_policy_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextSection(
            title = stringResource(id = R.string.what_are_cookies_title),
            content = stringResource(id = R.string.what_are_cookies_content)
        )

        TextSection(
            title = stringResource(id = R.string.types_of_cookies_title),
            content = stringResource(id = R.string.types_of_cookies_content)
        )

        TextSection(
            title = stringResource(id = R.string.why_we_use_cookies_title),
            content = stringResource(id = R.string.why_we_use_cookies_content)
        )

        TextSection(
            title = stringResource(id = R.string.managing_cookies_title),
            content = stringResource(id = R.string.managing_cookies_content)
        )

        TextSection(
            title = stringResource(id = R.string.changes_to_cookies_policy_title),
            content = stringResource(id = R.string.changes_to_cookies_policy_content)
        )
    }
}
