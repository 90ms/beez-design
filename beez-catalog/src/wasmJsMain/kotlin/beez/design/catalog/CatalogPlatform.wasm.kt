package beez.design.catalog

import kotlinx.browser.window

internal actual fun defaultCatalogLocale(): CatalogLocale =
    if (window.navigator.language.lowercase().startsWith("ko")) {
        CatalogLocale.Korean
    } else {
        CatalogLocale.English
    }
