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
fun PrivacyPolicyScreen(
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
                text = "Privacy Policy",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextSection(
                title = "1. Information Collection",
                content = "We collect personal information that you provide to us directly, such as name, email address, and other relevant information when you create an account or use our services."
            )

            TextSection(
                title = "2. Use of Information",
                content = "We use the information collected to provide, improve, and personalize our services, as well as to communicate with you about updates and promotional offers."
            )

            TextSection(
                title = "3. Sharing of Information",
                content = "We may share your information with third parties only as necessary to provide our services or as required by law."
            )

            TextSection(
                title = "4. Data Security",
                content = "We implement security measures to protect your data. However, we cannot guarantee complete security of your information."
            )

            TextSection(
                title = "5. User Rights",
                content = "You have the right to access, modify, or delete your personal information by contacting us at support@example.com."
            )

            TextSection(
                title = "6. Changes to This Policy",
                content = "We may update our privacy policy from time to time. We will notify you of any significant changes."
            )
        }

    }

}


