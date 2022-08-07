package com.unatxe.quicklist.helpers

fun <T>MutableList<T>.evenArray(listToEven: List<T>) {
    listToEven.forEach {
        if (!contains(it)) {
            add(it)
        }
    }

    val toRemove = filter {
        !listToEven.contains(it)
    }

    removeAll(toRemove)
}
