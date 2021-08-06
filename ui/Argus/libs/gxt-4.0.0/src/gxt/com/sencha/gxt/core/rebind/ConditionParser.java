/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.core.rebind;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;

public class ConditionParser {
  private static final Pattern BIN_OPS = Pattern.compile("\\|\\||&&|\\<=|\\>=|\\<|\\>|==|!=|\\+|\\-|\\*|\\/");
  private static final Pattern METHOD_PATTERN = Pattern.compile("([a-zA-Z0-9\\._]+\\:[a-zA-Z0-9_]+\\([^\\)]*\\))");
  private static final Pattern UNARY_OPS = Pattern.compile("!|" + METHOD_PATTERN.pattern());
  private static final Pattern WHITESPACE = Pattern.compile("\\s");

  private static final Pattern POSSIBLE_STRING_LITERAL = Pattern.compile("\\\'([^\\\']*)\\\'");

  private static final Pattern NON_REF = Pattern.compile(WHITESPACE.pattern() + "|" + BIN_OPS.pattern() + "|" + UNARY_OPS.pattern());

  private static final Pattern LITERAL = Pattern.compile("^(?:[0-9]+|\\\"[^\\\"]*\\\"|\\\'[^\\\']*\\\'|true|false|null)$");
  private TreeLogger log;

  public ConditionParser(TreeLogger log) {
    this.log = log;
  }

  public List<Token> parse(String condition) {
    TreeLogger l = log.branch(Type.DEBUG, "Parsing condition: " + condition);
    // expect lots of whitespace for now... if it doesn't match an expression or
    // literal, it must be a ref
    List<Token> tokens = new ArrayList<Token>();

    Matcher m = NON_REF.matcher(condition);
    int lastMatchEnd = 0;
    while (m.find()) {
      int begin = m.start(), end = m.end();
      @SuppressWarnings("unused")
      String currentMatch = condition.substring(begin, end);

      // look for unmatched sections, indicating either ref or literal
      if (lastMatchEnd < begin) {
        Token ref = buildRefOrLitToken(l, condition.substring(lastMatchEnd, begin));
        tokens.add(ref);
      }
      lastMatchEnd = end;

      // deal with methods differently than bin/unary ops
      Token t = new Token();

      Matcher method = METHOD_PATTERN.matcher(m.group());
      if (method.matches()) {
        t.type = Token.Type.MethodInvocation;
        t.contents = method.group(1);
      } else {
        // TODO consider leaving out whitespace...
        t.type = Token.Type.ExpressionLiteral;
        t.contents = m.group();
      }

      tokens.add(t);
    }
    // look for ending trailing refs
    if (lastMatchEnd < condition.length()) {
      Token ref = buildRefOrLitToken(l, condition.substring(lastMatchEnd));
      tokens.add(ref);
    }

    return tokens;
  }

  private Token buildRefOrLitToken(TreeLogger l, String contents) {
    Token ref = new Token();
    ref.contents = contents;
    // This is either a literal or a reference
    Matcher lit = LITERAL.matcher(ref.contents);
    if (lit.matches()) {
      // Matches the literal pattern
      ref.type = Token.Type.ExpressionLiteral;
      // look for accidental char/string mismatch
      Matcher str = POSSIBLE_STRING_LITERAL.matcher(ref.contents);
      if (str.matches()) {
        if (str.group(1).length() > 1) {
          ref.contents = "\"" + str.group(1) + "\"";
        } else {
          // TODO mechanism to disable this warn
          l.log(
              Type.WARN,
              "Possible char was turned into a string, please be aware that both ' and \" marks are used for String objects in XTemplates");
        }
      }
    } else {
      // Must be a reference, mark it as such
      ref.type = Token.Type.Reference;
    }
    return ref;
  }

  public static class Token {
    public enum Type {
      Reference, ExpressionLiteral, MethodInvocation
    }

    public Type type;
    public String contents;
  }
}
