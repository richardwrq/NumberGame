package com.example.numbergame


fun ArrayList<ArrayList<GroupNumber>>.check() {
    val it = iterator()
    while (it.hasNext()) {
        val arrayList = it.next()
        if (arrayList.isEmpty() || !arrayList[0].isValid()) {
            it.remove()
        }
    }
}