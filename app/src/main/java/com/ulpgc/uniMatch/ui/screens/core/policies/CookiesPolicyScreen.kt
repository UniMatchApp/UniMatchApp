package com.ulpgc.uniMatch.ui.screens.core.policies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.ui.components.policies.TextSection
import com.ulpgc.uniMatch.ui.screens.core.topBars.PoliciesTopBar

@Composable
fun CookiesPolicyScreen(
    onBackPressed : () -> Unit
) {

    Column {
        PoliciesTopBar(onBackPressed = { onBackPressed() })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {



            Text(
                text = "Cookies Policy",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextSection(
                title = "1. What Are Cookies?",
                content = "Cookies are small files that are stored on your device when you visit a website. They help us remember your preferences and enhance your experience on our site."
            )

            TextSection(
                title = "2. Types of Cookies We Use",
                content = "We use both session and persistent cookies. Session cookies are deleted when you close the browser, while persistent cookies remain until you delete them or they expire."
            )

            TextSection(
                title = "3. Why We Use Cookies",
                content = "Cookies help us improve our services by tracking your preferences, allowing us to provide personalized content, and understanding how you interact with our website."
            )

            TextSection(
                title = "4. Managing Cookies",
                content = "You can control or delete cookies through your browser settings. However, disabling cookies may affect your experience on our site."
            )

            TextSection(
                title = "5. Changes to This Policy",
                content = "We may update our cookies policy periodically. Any changes will be posted on this page."
            )
        }
    }

}

