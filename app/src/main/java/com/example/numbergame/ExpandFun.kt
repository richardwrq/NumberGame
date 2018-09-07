package com.example.numbergame


fun ArrayList<ArrayList<GroupNumber>>.check(): GroupNumber {
    val it = iterator()

    var area1Count = 0
    var area2Count = 0
    var area3Count = 0

    while (it.hasNext()) {
        val arrayList = it.next()
        if (arrayList.isEmpty()) {
            it.remove()
        }
        var size = 0
        when (arrayList[0].n1) {
            0, 5 -> {
                size = arrayList.filter { it.isValid() }.size
                area1Count += size
            }
            1, 4, 6, 7 -> {
                size = arrayList.filter { it.isValid() }.size
                area2Count += size
            }
            2, 3 -> {
                size = arrayList.filter { it.isValid() }.size
                area3Count += size
            }
        }
        if (size == 0) {
            it.remove()
        }
    }
    return GroupNumber(area1Count, area2Count, area3Count)
}