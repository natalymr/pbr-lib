package miningtool.parse.antlr.cpp

import me.vovak.antlr.parser.*
import miningtool.common.Parser
import miningtool.parse.antlr.SimpleNode
import miningtool.parse.antlr.convertAntlrTree
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token
import java.io.InputStream

class CppParser : Parser<SimpleNode> {

    override fun parse(content: InputStream): SimpleNode? {
        val lexer = CPP14Lexer(ANTLRInputStream(content))
        lexer.removeErrorListeners()
        val tokens = CommonTokenStream(lexer)
        val parser = CPP14Parser(tokens)
        parser.removeErrorListeners()
        val context = parser.translationunit()
        return convertAntlrTree(context, CPP14Parser.ruleNames)
    }

    fun tokenizer(content: InputStream): List<Token> {
        val lexer = CPP14Lexer(ANTLRInputStream(content))
        val tokenList = lexer.getAllTokens()
        for (token in tokenList) {
            System.out.println("Next token :"  + token.text)
        }
        return tokenList
    }
}