package rocks.frieler.pbqp.model

/**
 * The [Effect]s for combinations of two options of two [Decision]s, when chosen together.
 *
 * There should be one instance of [CombinationEffects] for each pair of [Decision]s, managing their interdependencies.
 *
 * @param O the type of options for the [Decision]s
 */
class CombinationEffects<O>(
    val firstDecision: Decision<O>,
    val secondDecision: Decision<O>,
) {
    private val effects: MutableMap<Pair<O, O>, Effect> = HashMap()

    internal fun addEffect(optionOfOneDecision: O, optionOfOtherDecision: O, additionalEffect: Effect) {
        if (firstDecision.hasOption(optionOfOneDecision)) {
            require(secondDecision.hasOption(optionOfOtherDecision))
            effects[Pair(optionOfOneDecision, optionOfOtherDecision)] += additionalEffect
        } else {
            require(firstDecision.hasOption(optionOfOtherDecision))
            require(secondDecision.hasOption(optionOfOneDecision))
            effects[Pair(optionOfOtherDecision, optionOfOneDecision)] += additionalEffect
        }
    }

    /**
     * Returns the [Effect] for the combination of two options, each from one of the two [Decision]s.
     *
     * @param optionOfOneDecision an option for one [Decision]
     * @param optionOfOtherDecision an option for the other [Decision]
     */
    fun getEffect(optionOfOneDecision: O, optionOfOtherDecision: O): Effect {
        return if (firstDecision.hasOption(optionOfOneDecision)) {
            require(secondDecision.hasOption(optionOfOtherDecision))
            effects[Pair(optionOfOneDecision, optionOfOtherDecision)] ?: Effect.NONE
        } else {
            require(firstDecision.hasOption(optionOfOtherDecision))
            require(secondDecision.hasOption(optionOfOneDecision))
            effects[Pair(optionOfOtherDecision, optionOfOneDecision)] ?: Effect.NONE
        }
    }

    private operator fun Effect?.plus(augend: Effect) = (this ?: Effect.NONE) + augend
}
