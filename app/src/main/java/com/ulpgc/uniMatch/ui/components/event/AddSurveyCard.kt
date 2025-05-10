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
    onDeleteSurvey: (() -> Unit)? = null,
    onConfirmSurvey: ((String, List<String>) -> Unit)? = null,
) {
    val defaultOptionList = listOf("", "")

    val finalTitle = survey.title
    var title by remember { mutableStateOf(finalTitle) }

    var optionList by remember {
        mutableStateOf(survey.options.keys.toList().ifEmpty { defaultOptionList })
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SurveyTitle(
            title = title,
            placeholder = stringResource(R.string.event_title),
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
                optionList = optionList + ""
            },
            onConfirmSurvey = {
                Log.i("Survey", "AddSurveyCard survey card onConfirm $title, $optionList")
                onConfirmSurvey?.invoke(title, optionList)
            },
            optionsSize = optionList.size
        )
    }
}
