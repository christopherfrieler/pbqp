package rocks.frieler.pbqp.model

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class DecisionTest {
    @Test
    internal fun `a decision can have an option`() {
        val option = Any()

        val decision = Decision<Any>()
        decision.addOption(option)

        assertThat(decision.hasOption(option)).isTrue()
        assertThat(decision.getOptions()).contains(option)
    }

    @Test
    internal fun `a decision does not have an option that was not added`() {
        val option = Any()

        val decision = Decision<Any>()

        assertThat(decision.hasOption(option)).isFalse()
    }

    @Test
    internal fun `a decision can have an option with an effect`() {
        val option = Any()
        val effect = Effect.of(BigDecimal.ONE)

        val decision = Decision<Any>()
        decision.addOption(option, effect)

        assertThat(decision.hasOption(option)).isTrue()
        assertThat(decision.getOptionsWithEffects()).contains(Pair(option, effect))
    }

    @Test
    internal fun `a decision's option has no effect by default`() {
        val option = Any()

        val decision = Decision<Any>()
        decision.addOption(option)

        assertThat(decision.getOptionsWithEffects()).contains(Pair(option, Effect.NONE))
    }
}
