package com.ulpgc.uniMatch.ui.components.event

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyBottomButtons
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyOptions
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyTitle

@Composable
fun AddSurveyCard(
    survey: Survey,
    isEditing: Boolean = false,
    onDeleteSurvey: (() -> Unit)? = null,
    onConfirmSurvey: ((String, List<String>) -> Unit)? = null,
) {
    Log.i("Survey", "IsEditing survey card $isEditing")

    val defaultTitle = stringResource(R.string.event_title)
    val defaultOptions = mapOf(
        stringResource(R.string.option_one) to setOf<String>(),
        stringResource(R.string.option_two) to setOf<String>()
    )

    val finalTitle = survey.title.ifBlank { defaultTitle }
    var options by remember {
        mutableStateOf(if (survey.options.isEmpty()) defaultOptions else survey.options)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var title by remember { mutableStateOf(finalTitle) }
        var optionList by remember { mutableStateOf(options.keys.toList()) }

        SurveyTitle(
            title = title,
            onDeleteSurvey = onDeleteSurvey,
            onTitleChange = { title = it },
            isEditing = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        SurveyOptions(
            options = optionList,
            isEditing = true,
            onOptionChange = { index, newValue ->
                optionList = optionList.toMutableList().apply { set(index, newValue) }
            },
            onOptionRemove = { index ->
                optionList = optionList.toMutableList().apply { removeAt(index) }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        SurveyBottomButtons(
            onAddOption = {
                optionList = optionList + "New Option ${optionList.size + 1}"
            },
            onConfirmSurvey = {
                Log.i("Survey", "AddSurveyCard survey card onConfirme $title, $optionList")
                onConfirmSurvey?.invoke(title, optionList)

                options = optionList.associateWith { setOf<String>() }
            },
            optionsSize = optionList.size
        )
    }
}
