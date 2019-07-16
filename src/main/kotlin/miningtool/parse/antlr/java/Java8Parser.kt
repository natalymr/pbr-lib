package miningtool.parse.antlr.java

import me.vovak.antlr.parser.JavaLexer
import me.vovak.antlr.parser.JavaParser
import miningtool.common.Parser
import miningtool.parse.antlr.SimpleNode
import miningtool.parse.antlr.convertAntlrTree
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token
import java.io.InputStream

class Java8Parser : Parser<SimpleNode> {
    override fun parse(content: InputStream): SimpleNode? {
        val lexer = JavaLexer(ANTLRInputStream(content))
        lexer.removeErrorListeners()
        val tokens = CommonTokenStream(lexer)
        val parser = JavaParser(tokens)
        parser.removeErrorListeners()
        val context = parser.compilationUnit()
        return convertAntlrTree(context, JavaParser.ruleNames)
    }

    fun tokenizer(content: InputStream): List<Token> {
//        return JavaLexer(ANTLRInputStream(content)).allTokens
        val lexer = JavaLexer(ANTLRInputStream(content))
        val tokenList = lexer.allTokens
//        for (token in tokenList) {
//            println("Next token :"  + token.text)
//        }
        return tokenList
    }

    fun tokenizerForBlob(content: InputStream): List<String> {
//        return JavaLexer(ANTLRInputStream(content)).allTokens
        val lexer = JavaLexer(ANTLRInputStream(content))
        lexer.removeErrorListeners()

        val tokenList = lexer.allTokens
        val result = mutableListOf<String>()
        for (token in tokenList) {
            result.add(token.text)
        }
        return result
    }
}