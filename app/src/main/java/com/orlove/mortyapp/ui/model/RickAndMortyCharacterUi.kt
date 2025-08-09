package com.orlove.mortyapp.ui.model

import com.orlove.mortyapp.util.constants.CharacterGender
import com.orlove.mortyapp.util.constants.CharacterStatus

data class RickAndMortyCharacterUi(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val origin: LocationUi,
    val location: LocationUi,
    val image: String,
    val episodes: List<String>,
    val url: String,
    val created: String
)

data class LocationUi(
    val name: String,
    val url: String
)
