package aleksey.vasiliev.helpers

import aleksey.vasiliev.helpers.SharedLogic.type2Value
import kotlin.math.pow
import java.io.Serializable

class Coder {
    /** Data-класс, передаваемый через TCP
     */

    data class TLVInstance(val identifier: Byte, val length: List<Byte>, val value: Serializable?)

    /** Функция для получения размера сериализуемого объекта в байтах.
     * К сожалению, в JVM нет прямого аналога sizeOf из C,
     * поэтому объект сериализуется, загружается в ObjectOutputStream,
     * который базируется на ByteArrayOutputStream (BAOS). Далее BAOS кастуется к массиву байт,
     * размер полученного массива - искомое число байт при передачи по сети.
     */

    /** Функция для декодирования длины передаваемых данных.
     */

    private fun decodeDataLength(length: List<Byte>): Int {
        if (length.size == 1) {
            return length[0].toInt()
        }
        var size = 0.0
        for (i in 1 until length.size) {
            var current = length[i].toInt()
            if (current < 0) {
                current = 128 - current
            }
            size += 256.0.pow(length.size - i - 1) * current
        }
        return size.toInt()
    }

    /** Функция для декодирования типа передаваемых данных.
     */

    private fun decodeDataType(identifier: Byte): String? {
        return type2Value.firstOrNull { it.second == identifier }?.first
    }

    /** Функция для декодирования передаваемых данных.
     */

    fun decode(tlvInstance: TLVInstance): Triple<Serializable?, Int, String?> {
        return Triple(tlvInstance.value, decodeDataLength(tlvInstance.length), decodeDataType(tlvInstance.identifier))
    }
}