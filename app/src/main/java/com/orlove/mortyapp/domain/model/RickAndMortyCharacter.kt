package com.orlove.mortyapp.domain.model

import com.orlove.mortyapp.util.constants.CharacterGender
import com.orlove.mortyapp.util.constants.CharacterStatus

data class RickAndMortyCharacter(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val origin: Location,
    val location: Location,
    val image: String,
    val episodes: List<String>,
    val url: String,
    val created: String
)

data class Location(
    val name: String,
    val url: String
)