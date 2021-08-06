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
package com.sencha.gxt.core.shared;

/*
 * This class is taken directly from com.google.gwt.safehtml.shared.SimpleHtmlSanitizer and modified
 * to include an expanded list of acceptable formatting tags that make this sanitizer more useful.
 *
 * Originally Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An expanded HTML sanitizer based on <code>SimpleHtmlSanitizer</code>, but with a larger set
 * of formatting tags that make this sanitizer more useful. These tags were determined to be safe by conducting
 * a manual review of formatting tags that don't require attributes to be useful from the full list of tags at:
 *
 * http://www.w3schools.com/tags/
 *
 * <p>
 * This sanitizer accepts only the following subset of HTML tags and only when used with no attributes:
 *
 * <ul>
 * <li>{@code <b>}, {@code <big>}, {@code <del>}, {@code <em>}, {@code <i>}, {@code <ins>}, {@code <mark>}, {@code <s>}, {@code <small>},
 *     {@code <strike>}, {@code <strong>}, {@code <sub>}, {@code <sup>}, {@code <u>}</li>
 * <li>{@code <br>}, {@code <div>}, {@code <center>}, {@code <hr>}, {@code <p>}, {@code <span>}</li>
 * <li>{@code <h1>}, {@code <h2>}, {@code <h3>}, {@code <h4>}, {@code <h5>}, {@code <h6>}</li>
 * <li>{@code <dd>}, {@code <dir>}, {@code <dl>}, {@code <dt>}, {@code <li>}, {@code <ol>}, {@code <ul>}</li>
 * <li>{@code <address>}, {@code <article>}, {@code <aside>}, {@code <blockquote>}, {@code <cite>}, {@code <code>}, {@code <details>},
 *     {@code <dfn>}, {@code <footer>}, {@code <header>}, {@code <kbd>}, {@code <main>}, {@code <pre>}, {@code <q>}, {@code <samp>},
 *     {@code <section>}, {@code <summary>}, {@code <tt>}</li>
 * <li>{@code <table>}, {@code <tbody>}, {@code <td>}, {@code <tfoot>}, {@code <th>}, {@code <thead>}, {@code <tr>}</li>
 * <li>{@code <bdi>}, {@code <rp>}, {@code <rt>}, {@code <ruby>}, {@code <wbr>}</li>
 * </ul>
 *
 * as well as numeric HTML entities and HTML entity references. Any HTML metacharacters that do not appear as part of
 * markup in this subset will be escaped.
 */
public final class ExpandedHtmlSanitizer implements HtmlSanitizer {

  private static final ExpandedHtmlSanitizer INSTANCE = new ExpandedHtmlSanitizer();

  private static final Set<String> TAG_WHITELIST = new HashSet<String>(
      Arrays.asList(
          "b", "big", "del", "em", "i", "ins", "mark", "s", "small", "strike", "strong", "sub", "sup", "u",
          "br", "div", "center", "hr", "p", "span",
          "h1", "h2", "h3", "h4", "h5", "h6",
          "dd", "dir", "dl", "dt", "li", "ol", "ul",
          "address", "article", "aside", "blockquote", "cite", "code", "details", "dfn", "footer", "header", "kbd",
              "main", "pre", "q", "samp", "section", "summary", "tt",
          "table", "tbody", "td", "tfoot", "th", "thead", "tr",
          "bdi", "rp", "rt", "ruby", "wbr"
      )
  );

  /**
   * Return a singleton ExpandedHtmlSanitizer instance.
   *
   * @return the instance
   */
  public static ExpandedHtmlSanitizer getInstance() {
    return INSTANCE;
  }

  /**
   * HTML-sanitizes a string.
   *
   * <p>
   * The input string is processed as described above. The result of sanitizing
   * the string is guaranteed to be safe to use (with respect to XSS
   * vulnerabilities) in HTML contexts, and is returned as an instance of the
   * {@link SafeHtml} type.
   *
   * @param html the input String
   * @return a sanitized SafeHtml instance
   */
  public static SafeHtml sanitizeHtml(String html) {
    if (html == null) {
      throw new NullPointerException("html is null");
    }
    return SafeHtmlUtils.fromTrustedString(simpleSanitize(html));
  }

  /*
   * Sanitize a string containing simple HTML markup as defined above. The
   * approach is as follows: We split the string at each occurence of '<'. Each
   * segment thus obtained is inspected to determine if the leading '<' was
   * indeed the start of a whitelisted tag or not. If so, the tag is emitted
   * unescaped, and the remainder of the segment (which cannot contain any
   * additional tags) is emitted in escaped form. Otherwise, the entire segment
   * is emitted in escaped form.
   *
   * In either case, EscapeUtils.htmlEscapeAllowEntities is used to escape,
   * which escapes HTML but does not double escape existing syntactially valid
   * HTML entities.
   */
  private static String simpleSanitize(String text) {
    StringBuilder sanitized = new StringBuilder();

    boolean firstSegment = true;
    for (String segment : text.split("<", -1)) {
      if (firstSegment) {
        /*
         *  the first segment is never part of a valid tag; note that if the
         *  input string starts with a tag, we will get an empty segment at the
         *  beginning.
         */
        firstSegment = false;
        sanitized.append(SafeHtmlUtils.htmlEscapeAllowEntities(segment));
        continue;
      }

      /*
       *  determine if the current segment is the start of an attribute-free tag
       *  or end-tag in our whitelist
       */
      int tagStart = 0; // will be 1 if this turns out to be an end tag.
      int tagEnd = segment.indexOf('>');
      String tag = null;
      boolean isValidTag = false;
      if (tagEnd > 0) {
        if (segment.charAt(0) == '/') {
          tagStart = 1;
        }
        tag = segment.substring(tagStart, tagEnd);
        if (TAG_WHITELIST.contains(tag)) {
          isValidTag = true;
        }
      }

      if (isValidTag) {
        // append the tag, not escaping it
        if (tagStart == 0) {
          sanitized.append('<');
        } else {
          // we had seen an end-tag
          sanitized.append("</");
        }
        sanitized.append(tag).append('>');

        // append the rest of the segment, escaping it
        sanitized.append(SafeHtmlUtils.htmlEscapeAllowEntities(
            segment.substring(tagEnd + 1)));
      } else {
        // just escape the whole segment
        sanitized.append("&lt;").append(
            SafeHtmlUtils.htmlEscapeAllowEntities(segment));
      }
    }
    return sanitized.toString();
  }

  /*
   * Note: We purposely do not provide a method to create a SafeHtml from
   * another (arbitrary) SafeHtml via sanitization, as this would permit the
   * construction of SafeHtml objects that are not stable in the sense that for
   * a {@code SafeHtml s} it may not be true that {@code s.asString()} equals
   * {@code SimpleHtmlSanitizer.sanitizeHtml(s.asString()).asString()}. While
   * this is not currently an issue, it might become one and result in
   * unexpected behavior if this class were to become serializable and enforce
   * its class invariant upon deserialization.
   */

  // prevent external instantiation
  private ExpandedHtmlSanitizer() {
  }

  public SafeHtml sanitize(String html) {
    return sanitizeHtml(html);
  }
}
