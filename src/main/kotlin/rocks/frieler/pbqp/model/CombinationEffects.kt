package rocks.frieler.pbqp.model

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
