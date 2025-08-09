package com.orlove.mortyapp.ui.model.mapper

import com.orlove.mortyapp.data.local.db.entity.CharacterEntity
import com.orlove.mortyapp.data.remote.dto.CharacterDto
import com.orlove.mortyapp.data.remote.dto.LocationDto
import com.orlove.mortyapp.domain.model.Location
import com.orlove.mortyapp.domain.model.RickAndMortyCharacter
import com.orlove.mortyapp.ui.model.LocationUi
import com.orlove.mortyapp.ui.model.RickAndMortyCharacterUi
import com.orlove.mortyapp.util.constants.CharacterGender
import com.orlove.mortyapp.util.constants.CharacterStatus
import com.orlove.mortyapp.util.formatDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun CharacterDto.toDomain(): RickAndMortyCharacter {
    return RickAndMortyCharacter(
        id = id,
        name = name,
        status = CharacterStatus.valueOf(status.uppercase()),
        species = species,
        type = type,
        gender = CharacterGender.valueOf(gender.uppercase()),
        origin = origin.toDomain(),
        location = location.toDomain(),
        image = image,
        episodes = episode,
        url = url,
        created = created
    )
}

fun CharacterDto.toEntity(page: Int): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originName = origin.name,
        originUrl = origin.url,
        locationName = location.name,
        locationUrl = location.url,
        image = image,
        episodeUrls = Json.encodeToString(episode),
        url = url,
        created = created,
        page = page
    )
}

fun CharacterEntity.toDomain(): RickAndMortyCharacter {
    return RickAndMortyCharacter(
        id = id,
        name = name,
        status = CharacterStatus.valueOf(status.uppercase()),
        species = species,
        type = type,
        gender = CharacterGender.valueOf(gender.uppercase()),
        origin = Location(originName, originUrl),
        location = Location(locationName, locationUrl),
        image = image,
        episodes = Json.decodeFromString(episodeUrls),
        url = url,
        created = created
    )
}

private fun LocationDto.toDomain(): Location {
    return Location(name = name, url = url)
}

fun RickAndMortyCharacter.toUi(): RickAndMortyCharacterUi {
    return RickAndMortyCharacterUi(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = LocationUi(origin.name, origin.url),
        location = LocationUi(location.name, location.url),
        image = image,
        episodes = episodes,
        url = url,
        created = formatDate(created)
    )
}