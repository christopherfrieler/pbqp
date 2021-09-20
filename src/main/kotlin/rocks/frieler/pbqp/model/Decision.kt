package rocks.frieler.pbqp.model

class Decision<O> {
    private val options: MutableList<Pair<O, Effect>> = ArrayList()

    internal fun addOption(option: O, effect: Effect = Effect.NONE) {
        options.add(Pair(option, effect))
    }

    fun hasOption(option: O): Boolean = options.find { pair -> pair.first == option } != null

    fun getOptions(): List<O> = options.map(Pair<O, Effect>::first)

    fun getOptionsWithEffects(): List<Pair<O, Effect>> = listOf(*options.toTypedArray())
}
