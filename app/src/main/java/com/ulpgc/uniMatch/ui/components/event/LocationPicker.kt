import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Location
import com.ulpgc.uniMatch.data.infrastructure.viewModels.RideViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPicker(
    viewModel: RideViewModel = viewModel(),
    onChangeLocation: (Location) -> Unit
) {
    val pickupLocationPlaces by viewModel.pickupLocationPlaces.collectAsStateWithLifecycle()
    val selectedLocation = viewModel.location


    var isLazyColumnVisible by remember { mutableStateOf(true) }

    TextField(
        value = viewModel.pickUp,
        onValueChange = { newValue ->
            viewModel.onPickUpValueChanged(newValue)
            isLazyColumnVisible = true
        },
        placeholder = {
            Text(text = stringResource(R.string.event_location))
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
    )

    if (isLazyColumnVisible) {
        LazyColumn(
            modifier = Modifier
                .wrapContentHeight()
        ) {
            items(pickupLocationPlaces) { place ->
                Text(
                    text = place.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Log.i("PlacesAutoCompleteTextField", "Place clicked: ${place.toString()}")
                            viewModel.onPlaceClick(place.id)
                            viewModel.onPickUpValueChanged(TextFieldValue(text = place.name))
                            isLazyColumnVisible = false
                        }
                        .padding(16.dp)
                )

            }
        }
    }


    // Si hay una ubicación seleccionada, la pasamos a la función onChangeLocation
    selectedLocation?.let {
        onChangeLocation(it)
    }
}
