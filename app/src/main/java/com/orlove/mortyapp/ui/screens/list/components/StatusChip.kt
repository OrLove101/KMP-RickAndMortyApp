package com.orlove.mortyapp.ui.screens.list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orlove.mortyapp.R
import com.orlove.mortyapp.ui.theme.Spacing
import com.orlove.mortyapp.utils.constants.CharacterStatus

@Composable
fun StatusChip(status: CharacterStatus) {
    val (color, text) = when (status) {
        CharacterStatus.ALIVE -> Color.Green to stringResource(R.string.alive)
        CharacterStatus.DEAD -> Color.Red to stringResource(R.string.dead)
        CharacterStatus.UNKNOWN -> Color.Gray to stringResource(R.string.unknown)
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = Spacing.medium, vertical = Spacing.small),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}