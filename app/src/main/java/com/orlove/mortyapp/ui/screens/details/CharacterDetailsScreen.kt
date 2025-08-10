package com.orlove.mortyapp.ui.screens.details

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.orlove.mortyapp.R
import com.orlove.mortyapp.ui.components.ErrorState
import com.orlove.mortyapp.ui.components.LoadingState
import com.orlove.mortyapp.ui.model.RickAndMortyCharacterUi
import com.orlove.mortyapp.ui.screens.details.components.InfoCard
import com.orlove.mortyapp.ui.screens.details.components.InfoRow
import com.orlove.mortyapp.util.collectInLaunchedEffect
import com.orlove.mortyapp.util.use

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CharacterDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: CharacterDetailViewModel
) {
    val (state, dispatch, effectFlow) = use(viewModel)
    val context = LocalContext.current

    effectFlow.collectInLaunchedEffect { effect ->
        when (effect) {
            CharacterDetailContract.Effect.NavigateBack -> onNavigateBack()
            is CharacterDetailContract.Effect.ShowError -> {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { dispatch(CharacterDetailContract.Event.NavigateBack) }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(width = 16.dp))
            Text(
                text = stringResource(R.string.character_details),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        when {
            state.isLoading -> {
                LoadingState()
            }

            state.error  -> {
                ErrorState(
                    error = stringResource(R.string.oops_something_went_wrong),
                    onRetry = { dispatch(CharacterDetailContract.Event.RetryLoading) },
                )
            }

            state.character != null -> {
                CharacterDetailContent(
                    character = state.character!!,
                )
            }
        }
    }
}

@Composable
private fun CharacterDetailContent(
    character: RickAndMortyCharacterUi,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            InfoCard(title = stringResource(R.string.basic_information)) {
                InfoRow(label = stringResource(R.string.species), value = character.species)
                if (character.type.isNotBlank()) {
                    InfoRow(label = stringResource(R.string.type), value = character.type)
                }
                InfoRow(
                    label = stringResource(R.string.gender),
                    value = character.gender.name.lowercase().uppercase()
                )
            }
        }

        item {
            InfoCard(title = stringResource(R.string.location_details)) {
                InfoRow(label = stringResource(R.string.origin), value = character.origin.name)
                InfoRow(
                    label = stringResource(R.string.last_known_location),
                    value = character.location.name
                )
            }
        }

        item {
            InfoCard(title = stringResource(R.string.episodes)) {
                Text(
                    text = stringResource(R.string.appeared_in_episodes, character.episodes.size),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        item {
            InfoCard(title = stringResource(R.string.additional_information)) {
                InfoRow(label = stringResource(R.string.created), value = character.created)
                InfoRow(
                    label = stringResource(R.string.character_id),
                    value = character.id.toString()
                )
            }
        }
    }
}