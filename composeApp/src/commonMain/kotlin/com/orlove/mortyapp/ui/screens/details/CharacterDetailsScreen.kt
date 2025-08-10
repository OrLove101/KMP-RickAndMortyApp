package com.orlove.mortyapp.ui.screens.details

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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.orlove.mortyapp.ui.components.ErrorState
import com.orlove.mortyapp.ui.components.LoadingState
import com.orlove.mortyapp.ui.model.RickAndMortyCharacterUi
import com.orlove.mortyapp.ui.screens.details.components.InfoCard
import com.orlove.mortyapp.ui.screens.details.components.InfoRow
import com.orlove.mortyapp.utils.collectInLaunchedEffect
import com.orlove.mortyapp.utils.use
import mortyapp.composeapp.generated.resources.Res
import mortyapp.composeapp.generated.resources.additional_information
import mortyapp.composeapp.generated.resources.appeared_in_episodes
import mortyapp.composeapp.generated.resources.basic_information
import mortyapp.composeapp.generated.resources.character_details
import mortyapp.composeapp.generated.resources.character_id
import mortyapp.composeapp.generated.resources.created
import mortyapp.composeapp.generated.resources.episodes
import mortyapp.composeapp.generated.resources.gender
import mortyapp.composeapp.generated.resources.last_known_location
import mortyapp.composeapp.generated.resources.location_details
import mortyapp.composeapp.generated.resources.oops_something_went_wrong
import mortyapp.composeapp.generated.resources.origin
import mortyapp.composeapp.generated.resources.species
import mortyapp.composeapp.generated.resources.type
import org.jetbrains.compose.resources.stringResource

@Composable
fun CharacterDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: CharacterDetailViewModel
) {
    val (state, dispatch, effectFlow) = use(viewModel)

    effectFlow.collectInLaunchedEffect { effect ->
        when (effect) {
            CharacterDetailContract.Effect.NavigateBack -> onNavigateBack()
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
                text = stringResource(Res.string.character_details),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        when {
            state.isLoading -> {
                LoadingState()
            }

            state.error  -> {
                ErrorState(
                    error = stringResource(Res.string.oops_something_went_wrong),
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
            InfoCard(title = stringResource(Res.string.basic_information)) {
                InfoRow(label = stringResource(Res.string.species), value = character.species)
                if (character.type.isNotBlank()) {
                    InfoRow(label = stringResource(Res.string.type), value = character.type)
                }
                InfoRow(
                    label = stringResource(Res.string.gender),
                    value = character.gender.name.lowercase().uppercase()
                )
            }
        }

        item {
            InfoCard(title = stringResource(Res.string.location_details)) {
                InfoRow(label = stringResource(Res.string.origin), value = character.origin.name)
                InfoRow(
                    label = stringResource(Res.string.last_known_location),
                    value = character.location.name
                )
            }
        }

        item {
            InfoCard(title = stringResource(Res.string.episodes)) {
                Text(
                    text = stringResource(Res.string.appeared_in_episodes, character.episodes.size),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        item {
            InfoCard(title = stringResource(Res.string.additional_information)) {
                InfoRow(label = stringResource(Res.string.created), value = character.created)
                InfoRow(
                    label = stringResource(Res.string.character_id),
                    value = character.id.toString()
                )
            }
        }
    }
}