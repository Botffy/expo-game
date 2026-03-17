package hu.sarmin.expo.simulation

data class Clock(private var time: Long = 0L) {
    fun tick(delta: Long = 1L) {
        require(delta > 0) { "Delta must be positive, got $delta" }
        time += delta
    }
    fun time() = time
}

data class Context(val clock: Clock)