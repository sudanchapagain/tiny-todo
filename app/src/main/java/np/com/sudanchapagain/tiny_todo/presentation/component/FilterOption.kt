package np.com.sudanchapagain.tiny_todo.presentation.component

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import np.com.sudanchapagain.tiny_todo.domain.model.Filter

@Composable
fun FilterOption(
    label: String, option: Filter, selectedFilter: Filter, onFilterSelected: (Filter) -> Unit
) {
    TextButton(onClick = { onFilterSelected(option) }) {
        Text(
            text = label,
            fontWeight = if (option == selectedFilter) FontWeight.Bold else FontWeight.Normal
        )
    }
}