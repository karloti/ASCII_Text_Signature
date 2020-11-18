package signature

import java.io.File

class Badge(name: String, tag: String) {
    private val pName = Font("roman.txt", name)
    private val pTag = Font("medium.txt", tag.toUpperCase())

    class Font(name: String) {  // Universal class with methods and properties for read fonts and print phrases
        class TypeCharacter(val sizeWidth: Int, val data: List<String>)

        private val fontSize: Int
        private val lettersCount: Int
        private val letters: MutableMap<Char, TypeCharacter>
        private val sizeOfWhiteSpace: Int
        private val phrase: MutableList<String>

        init {
            val file = File("C:/fonts/$name").readLines()
            val fileIterator = file.iterator()

            val (fSize, lCount) = fileIterator.next().split(" ").map(String::toInt)
            fontSize = fSize
            lettersCount = lCount
            letters = mutableMapOf()
            phrase = MutableList(fontSize) { "" }

            repeat(lettersCount) {
                val (s1, s2) = fileIterator.next().split(" ")
                letters[s1[0]] = TypeCharacter(s2.toInt(), List(fontSize) { fileIterator.next() })
            }

            sizeOfWhiteSpace = letters['a']?.sizeWidth ?: letters['A']!!.sizeWidth
            letters[' '] = TypeCharacter(sizeOfWhiteSpace, List(fontSize) { " ".repeat(sizeOfWhiteSpace) })
        }

        fun getLength() = phrase[0].length
        fun getPhrase(): MutableList<String> = phrase
        fun setPhrase(words: String) {
            for (i in 0 until fontSize) words.forEach { phrase[i] += letters[it]!!.data[i] }
        }

        fun addSymbols(symbol: String, left: Int, right: Int) {
            phrase.forEachIndexed { index, s -> phrase[index] = symbol.repeat(left) + s + symbol.repeat(right) }
        }

        constructor(name: String, words: String) : this(name) {
            setPhrase(words)
        }
    }

    fun printBadge() { // Print design of badge with boundaries and phrases
        when {  // Add two spaces on each side of the largest phrase
            pName.getLength() > pTag.getLength() -> pName.addSymbols(" ", 2, 2)
            pName.getLength() < pTag.getLength() -> pTag.addSymbols(" ", 2, 2)
            else -> {
                pName.addSymbols(" ", 2, 2)
                pTag.addSymbols(" ", 2, 2)
            }
        }
        val length = maxOf(pName.getLength(), pTag.getLength())  // Get size of largest phrase

        // Centering of each of them
        pName.addSymbols(" ", (length - pName.getLength()) / 2, length - pName.getLength() - (length - pName.getLength()) / 2)
        pTag.addSymbols(" ", (length - pTag.getLength()) / 2, length - pTag.getLength() - (length - pTag.getLength()) / 2)

        // Put boundaries of each phrases
        pName.addSymbols("88", 1, 1)
        pTag.addSymbols("88", 1, 1)

        // Top border
        println("8".repeat(pName.getLength()))

        // Print phrases
        pName.getPhrase().forEach(::println)
        pTag.getPhrase().forEach(::println)

        // Bottom border
        println("8".repeat(pName.getLength()))
    }
}

fun main() {
    print("Enter name and surname: ")
    val name = readLine()!!

    print("Enter person's status: ")
    val tag = readLine()!!

    Badge(name, tag).printBadge()
}

