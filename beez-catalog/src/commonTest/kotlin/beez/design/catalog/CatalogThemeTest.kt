package beez.design.catalog

import beez.design.tokens.BeezTokenSchemes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CatalogThemeTest {
    @Test
    fun testBrandOverridesOnlyDeclaredSemanticColors() {
        val lightBase = BeezTokenSchemes.light
        val darkBase = BeezTokenSchemes.dark
        val lightBrand = lightBase.withCatalogTestBrand()
        val darkBrand = darkBase.withCatalogTestBrand()

        assertNotEquals(lightBase.colors.backgroundBrand, lightBrand.colors.backgroundBrand)
        assertEquals(lightBrand.colors.backgroundBrand, darkBrand.colors.backgroundBrand)
        assertEquals(lightBrand.colors.foregroundOnBrand, darkBrand.colors.foregroundOnBrand)
        assertEquals(lightBase.colors.foregroundPrimary, lightBrand.colors.foregroundPrimary)
        assertEquals(darkBase.colors.foregroundPrimary, darkBrand.colors.foregroundPrimary)
        assertEquals(lightBase.typography, lightBrand.typography)
        assertEquals(darkBase.shapes, darkBrand.shapes)
    }
}
