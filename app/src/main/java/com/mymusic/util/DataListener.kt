package com.mymusic.util

class DataListener<T>(var t: T?) {
    val list = mutableListOf<(T) -> Unit>()

    fun add(f: (T) -> Unit) {
        list.add(f)
    }

    fun addAndListen(f: (T) -> Unit) {
        list.add(f)
        t?.let { t1 -> f(t1) }
    }

    fun remove(f: (T) -> Unit) {
        list.remove(f)
    }

    fun removeAll() {
        list.clear()
    }

    fun set(value: T) {
        t = value
        t?.let { t1 ->
            list.forEach { f ->
                f(t1)
            }
        }
    }

}