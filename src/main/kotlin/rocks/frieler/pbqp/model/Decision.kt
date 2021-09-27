package rocks.frieler.pbqp.model

/**
 * A single [Decision] where to choose between multiple options.
 *
 * @param O the type of options
 */
class Decision<O> {
    private val options: MutableList<Pair<O, Effect>> = ArrayList()

    internal fun addOption(option: O, effect: Effect = Effect.NONE) {
        options.add(Pair(option, effect))
    }

    /**
     * Checks, if this [Decision] has the given option.
     *
     * @param option the option to search
     */
    fun hasOption(option: O): Boolean = options.find { pair -> pair.first == option } != null

    /**
     * Returns the options to choose from in this [Decision].
     *
     * Note: The returned list is an immutable copy. Changes to the [Decision] must be made through the enclosing
     * [PBQP][PartialBooleanQuadraticProblem] to ensure consistency.
     *
     * @return the options for this [Decision]
     */
    fun getOptions(): List<O> = options.map(Pair<O, Effect>::first)

    /**
     * Returns the options to choose from in this [Decision] with their [Effect]s when chosen.
     *
     * Note: The returned list is an immutable copy. Changes to the [Decision] must be made through the enclosing
     * [PBQP][PartialBooleanQuadraticProblem] to ensure consistency.
     *
     * @return the options for this [Decision] and their [Effect]s
     */
    fun getOptionsWithEffects(): List<Pair<O, Effect>> = listOf(*options.toTypedArray())
}
