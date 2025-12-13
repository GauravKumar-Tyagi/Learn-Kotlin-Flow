package com.gaurav.learn.kotlin.flow.ui.flowInternals

// Create your own flow
class Flow<T>(private var value: T) {

    // mapper function accept T and return R
    fun <R> map(mapper: Function<T, R>): Flow<R> {
        val res : R = mapper.apply(this.value)
        return Flow(res)
    }

    fun filter(predicate: Predicate<T>): Flow<T?> {
        return if (predicate.test(this.value)) {
            Flow(this.value)
        } else {
            Flow(null)
        }
    }

    fun collect(collector: Collector<T>) {
        collector.collect(this.value)
    }

}

fun interface Function<T,R> {

    fun apply(value: T): R

}

fun interface Collector<T> {

    fun collect(value: T)

}

fun interface Predicate<T> {
    fun test(value: T): Boolean
}

