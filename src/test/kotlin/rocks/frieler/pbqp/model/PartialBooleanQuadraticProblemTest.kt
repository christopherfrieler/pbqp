package rocks.frieler.pbqp.model

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class PartialBooleanQuadraticProblemTest {

    @Test
    internal fun `can create PBQP instance`() {
        val pbqp = PartialBooleanQuadraticProblem()

        assertNotNull(pbqp)
    }
}
