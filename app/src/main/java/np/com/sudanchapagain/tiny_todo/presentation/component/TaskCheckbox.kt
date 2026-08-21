package np.com.sudanchapagain.tiny_todo.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import np.com.sudanchapagain.tiny_todo.core.ui.theme.successColor

@Composable
fun TaskCheckbox(
    checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(6.dp)
    val accent = successColor()

    val backgroundColor by animateColorAsState(
        targetValue = if (checked) accent else MaterialTheme.colorScheme.surface,
        label = "taskCheckboxBackground"
    )
    val borderColor by animateColorAsState(
        targetValue = if (checked) accent else MaterialTheme.colorScheme.outline,
        label = "taskCheckboxBorder"
    )

    Box(
        modifier = modifier.size(22.dp).clip(shape).background(backgroundColor)
            .border(width = 1.5.dp, color = borderColor, shape = shape)
            .clickable { onCheckedChange(!checked) }, contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = checked,
            enter = fadeIn() + scaleIn(initialScale = 0.5f),
            exit = fadeOut() + scaleOut(targetScale = 0.5f)
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
