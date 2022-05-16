package kr.sul.miscellaneousthings2.customitem.itemlistgui

class Dummy<T: Number> {
    fun acceptGeneric(t: @UnsafeVariance T) {
        println("${t}")
    }
}

fun main() {
    val dummy: Dummy<Number> = Dummy<Int>() as Dummy<Number>



    dummy.acceptGeneric(1)
}

fun acceptt(t: Dummy<Int>) {
    accept(t as Dummy<Number>)
}
fun accept(t: Dummy<Number>) {

}