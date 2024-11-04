import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.ui.components.InputField
import com.ulpgc.uniMatch.ui.components.profile.ProfileSection
import com.ulpgc.uniMatch.ui.screens.core.home.ReportModal

@Composable
fun ProfileInfoModal(
    profile: Profile?,
    onClose: () -> Unit,
    onReport: (String, String, String) -> Unit,
    onBlock: () -> Unit
) {
    var showReportModal by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("") }
    var selectedDetail by remember { mutableStateOf("") }
    var extraDetails by remember { mutableStateOf("") }

    if (profile != null && !showReportModal) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                                .clickable { onClose() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = stringResource(R.string.close),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.about_me),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputField(
                            value = profile.aboutMe,
                            onValueChange = {},
                            label = stringResource(R.string.about_me_label),
                            textColor = MaterialTheme.colorScheme.onBackground,
                            isEditable = false,
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            isRound = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.searching),
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = stringResource(R.string.looking_for),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputField(
                            value = profile.relationshipType.toString(),
                            onValueChange = {},
                            label = stringResource(R.string.relationship),
                            textColor = MaterialTheme.colorScheme.onBackground,
                            isEditable = false,
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            isRound = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    ProfileSection(
                        title = stringResource(R.string.more_about_me),
                        rowTitles = listOf(
                            "horoscope" to profile.horoscope.toString(),
                            "education" to profile.education,
                            "personality_type" to profile.personalityType
                        ),
                        isSelectable = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    ProfileSection(
                        title = stringResource(R.string.lifestyle),
                        rowTitles = listOf(
                            "pets" to profile.pets,
                            "drinks" to profile.drinks,
                            "smokes" to profile.smokes,
                            "sports" to profile.doesSports,
                            "religion" to profile.valuesAndBeliefs
                        ),
                        isSelectable = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = { showReportModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(stringResource(R.string.report), color = MaterialTheme.colorScheme.onError)
                        }

                        Button(
                            onClick = { onBlock() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(stringResource(R.string.block), color = MaterialTheme.colorScheme.onSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    if (showReportModal) {
        ReportModal(
            onDismiss = { showReportModal = false; onClose() },
            onReasonChange = { reason -> selectedReason = reason },
            onDetailChange = { detail -> selectedDetail = detail },
            onExtraDetailsChange = { extra -> extraDetails = extra },
            onReport = {
                onReport(
                    selectedReason,
                    selectedDetail,
                    extraDetails
                )
            }
        )
    }
}
