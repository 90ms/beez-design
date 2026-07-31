package beez.design.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import beez.design.components.BeezActionButton
import beez.design.components.BeezTextField
import beez.design.foundation.BeezTheme
import beez.design.tokens.BeezTokenSchemes

/**
 * Initial Compose Catalog entry point.
 *
 * The full Showcase sections will be migrated incrementally. This first
 * vertical slice deliberately renders the real BEEZ components instead of
 * duplicating them in HTML or CSS.
 */
@Composable
public fun CatalogApp() {
    BeezTheme(scheme = BeezTokenSchemes.light) {
        var value by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BeezTheme.colors.backgroundNeutral)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            BasicText(text = "BEEZ Catalog")
            BasicText(text = "Compose Multiplatform Showcase")

            BeezActionButton(
                label = "Action Button",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            BeezTextField(
                value = value,
                onValueChange = { value = it },
                label = "Text Field",
                placeholder = "Type something",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
