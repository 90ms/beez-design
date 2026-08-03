package beez.design.components

import beez.design.tokens.BeezTokenSchemes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BeezSurfaceTest {
    @Test
    fun elevationsUseSemanticRoles() {
        val elevation = BeezTokenSchemes.light.elevation

        assertNull(resolveSurfaceElevation(BeezSurfaceElevation.Flat, elevation))
        assertEquals(
            elevation.raised,
            resolveSurfaceElevation(BeezSurfaceElevation.Raised, elevation),
        )
        assertEquals(
            elevation.floating,
            resolveSurfaceElevation(BeezSurfaceElevation.Floating, elevation),
        )
    }
}
