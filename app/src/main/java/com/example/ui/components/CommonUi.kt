package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A highly polished, responsive container that wraps standard screen content.
 * Pins content width to a maximum of 600.dp for tablets/foldables, centering it nicely.
 */
@Composable
fun SleekPageContainer(
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .widthIn(max = 600.dp)
                .fillMaxWidth()
        ) {
            content()
        }
    }
}

/**
 * Premium unified section or page header with dynamic typography,
 * modern negative space, and elegant M3 branding accents.
 */
@Composable
fun SleekHeader(
    badgeText: String,
    titleText: String,
    subtitleText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.08f),
                        Color.Transparent
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // Sophisticated Pill Badge
        Surface(
            shape = RoundedCornerShape(100.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = badgeText.uppercase(),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        // Title
        Text(
            text = titleText,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Subtitle
        Text(
            text = subtitleText,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
            lineHeight = 18.sp
        )
    }
}

/**
 * Custom modern Card component adhering to the "Sleek Interface" guidelines.
 * Uses soft outline, refined round corners (24.dp), and high-contrast styling.
 */
@Composable
fun SleekCard(
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val defaultBorder = border ?: BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
    )

    if (onClick != null) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = defaultBorder,
            onClick = onClick,
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = defaultBorder,
            content = content
        )
    }
}
