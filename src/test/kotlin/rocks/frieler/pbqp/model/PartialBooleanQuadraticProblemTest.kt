package rocks.frieler.pbqp.model

import assertk.assertThat
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test

internal class PartialBooleanQuadraticProblemTest {

    @Test
    internal fun `can create PBQP instance`() {
        val pbqp = PartialBooleanQuadraticProblem()

        assertThat(pbqp).isNotNull()
    }
}
