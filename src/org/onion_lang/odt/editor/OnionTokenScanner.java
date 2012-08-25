/* ************************************************************** *
 *                                                                *
 * Copyright (c) 2005, Kota Mizushima, All rights reserved.       *
 *                                                                *
 *                                                                *
 * This software is distributed under the modified BSD License.   *
 * ************************************************************** */
package org.onion_lang.odt.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IJavaColorConstants;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Onionのトークンを解析するクラス
 * 
 * @author Kouta
 * 
 */
public class OnionTokenScanner extends RuleBasedScanner {
  public static final String[] KEYWORDS = {
    "abstract", "is", "assert", "break", "select", "case", "rec", "class",
    "continue", "def", "do", "else", "extends", "final", "fin",
    "for", "goto", "if","import", "is", "cond",
    "interface", "native", "new", "module", "private",
    "protected", "public", "return", "static", "super",
    "synchronized", "self", "throw", "try",
    "while", "true", "false", "null", "void", "boolean", "byte", "int",
    "short", "long", "float", "double", "char"
  };

  public static final char[] SYMBOLS = {
    ';', '(', ')', '{', '}', '.', '=', '/', '\\', '+', '-', '*', '[', ']', '<',
    '>', ':', '?', '!', ',', '|', '&', '^', '%', '~'
  };

  private class OnionWordDetector implements IWordDetector {
    public boolean isWordStart(char c) {
      return Character.isJavaIdentifierStart(c);
    }

    public boolean isWordPart(char c) {
      return Character.isJavaIdentifierPart(c);
    }
  }

  private class GroovyWSDetector implements IWhitespaceDetector {
    public boolean isWhitespace(char c) {
      return Character.isWhitespace(c);
    }
  }

  /**
   * Onionの演算子用のルール
   */
  private class OnionSymbolRule implements IRule {
    /** このルールで返すトークン */
    private final IToken token;

    /**
     * 新しい記号用のルールを生成する
     * 
     * @param token
     *          このルール用のトークン
     */
    public OnionSymbolRule(IToken token) {
      this.token = token;
    }

    public boolean isSymbol(char character) {
      for (int index = 0; index < SYMBOLS.length; index++) {
        if (SYMBOLS[index] == character)
          return true;
      }
      return false;
    }

    public IToken evaluate(ICharacterScanner scanner) {
      int ch = scanner.read();
      if (isSymbol((char) ch)) {
        do {
          ch = scanner.read();
        } while (isSymbol((char) ch));
        scanner.unread();
        return token;
      } else {
        scanner.unread();
        return Token.UNDEFINED;
      }
    }
  }

  private class OnionKeywordRule extends WordRule {
    public OnionKeywordRule() {
      super(new OnionWordDetector(), Token.UNDEFINED);
    }

    public OnionKeywordRule(String[] words, IToken[] tokens) {
      this();
      for (int i = 0; i < words.length; i++) {
        addWord(words[i], tokens[i]);
      }
    }
  }

  private class OnionQ2StringRule extends SingleLineRule {
    public OnionQ2StringRule(IToken token) {
      super("\"", "\"", token, '\\');
    }
  }

  private class OnionL1CommentRule extends EndOfLineRule {
    public OnionL1CommentRule(IToken token) {
      super("//", token);
    }
  }

  private class OnionMLCommentRule extends MultiLineRule {
    public OnionMLCommentRule(IToken token) {
      super("/*", "*/", token, (char) 0, true);
    }
  }

  private class OnionWSRule extends WhitespaceRule {
    public OnionWSRule() {
      super(new GroovyWSDetector());
    }
  }

  private IColorManager manager;

  public OnionTokenScanner(IColorManager manager) {
    this.manager = manager;
    setRules(makeRules());
  }

  private IRule[] makeRules() {
    List rules = new ArrayList();

    rules.add(new OnionMLCommentRule(
      getColoredToken(IJavaColorConstants.JAVA_MULTI_LINE_COMMENT)));

    rules.add(new OnionL1CommentRule(
      getColoredToken(IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT)));

    /*
     * Groovyの予約語用のルールを作成
     */
    IToken[] tokens = new IToken[KEYWORDS.length];
    for (int i = 0; i < KEYWORDS.length; i++) {
      tokens[i] = getColoredToken(IJavaColorConstants.JAVA_KEYWORD);
    }
    rules.add(new OnionKeywordRule(KEYWORDS, tokens));

    rules.add(new OnionSymbolRule(
      getColoredToken(IJavaColorConstants.JAVA_OPERATOR)));

    rules.add(new OnionQ2StringRule(
      getColoredToken(IJavaColorConstants.JAVA_STRING)));
    
    rules.add(new OnionWSRule());
    
    return (IRule[]) rules.toArray(new IRule[0]);
  }

  private IToken getColoredToken(String colorKey) {
    return new Token(new TextAttribute(manager.getColor(colorKey)));
  }
}
