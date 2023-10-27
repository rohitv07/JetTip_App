package com.project.jettip.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val IconbuttonSizeModifier = Modifier.size(40.dp)

@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onCLick: () -> Unit,
    tint:Color = Color.Black.copy(alpha = 0.8f),
    backgroundColor:Color = MaterialTheme.colorScheme.background,
    elevation:Dp = 4.dp
){

    Card(
        modifier = modifier
            .background(backgroundColor)
            .padding(all = 4.dp)
            .clickable/*(onClick = onClick)*/{
                onCLick.invoke()
            }
            .then(IconbuttonSizeModifier),
                //Using then() we are displaying Modifier after previous Modifier to create an Animation
        shape = CircleShape,
       // colors = CardDefaults.cardColors(backgroundColor),
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Icon(imageVector = imageVector,
            contentDescription = "Plus or Minus Icon",
            modifier = modifier
                .padding(9.dp)
                .align(alignment = Alignment.CenterHorizontally),
            tint = tint)
    }
}