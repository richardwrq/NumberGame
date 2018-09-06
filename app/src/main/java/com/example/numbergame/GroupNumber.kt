package com.example.numbergame


data class GroupNumber(private var n1: Int = 0, private var n2: Int = 0, private var n3: Int = 0) {

    var backgroundColor = ""

    fun contain(n: Int): Boolean {
        if (!isValid()) {
            return false
        }
        return n1 == n || n2 == n || n3 == n
    }

    fun isValid() = n1 != n2 && n1 != n3 && n2 != n3

    fun sum() = n1 + n2 + n3

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        if (other !is GroupNumber) {
            return false
        }
        return listOf(n1, n2, n3).sortedDescending() == listOf(other.n1, other.n2, other.n3).sortedDescending()
    }

    override fun toString(): String {
        return n1.toString() + n2 + n3
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    fun clear() {
        n1 = 0
        n2 = 0
        n3 = 0
    }
}