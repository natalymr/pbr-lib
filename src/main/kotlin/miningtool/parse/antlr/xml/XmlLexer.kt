package miningtool.parse.antlr.java

import me.vovak.antlr.parser.XMLLexer
import org.antlr.v4.runtime.ANTLRInputStream
import java.io.InputStream

class XmlParser {

    fun tokenizerForBlob(content: InputStream): List<String> {
        val lexer = XMLLexer(ANTLRInputStream(content))
        val tokenList = lexer.allTokens
        val result = mutableListOf<String>()
        for (token in tokenList) {
            result.add(token.text)
        }
        return result
    }
}