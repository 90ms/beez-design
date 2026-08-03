package beez.design.samples

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import beez.design.components.BeezCheckbox
import beez.design.foundation.BeezTheme

/**
 * Compiles only when the published Components artifact and its transitive
 * Foundation and Tokens artifacts can be resolved without project dependencies.
 */
@Composable
public fun PublishedConsumerSmoke() {
    var checked by remember { mutableStateOf(false) }

    BeezTheme {
        BeezCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            label = "Use BEEZ",
        )
    }
}
