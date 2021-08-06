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
package com.sencha.gxt.data.shared;

import java.util.Comparator;

/**
 * Sort direction enumeration.
 */

public enum SortDir {
  /**
   * Ascending sort order.
   */
  ASC {
    @Override
    public <X> Comparator<X> comparator(final Comparator<X> c) {
      return new Comparator<X>() {
        public int compare(X o1, X o2) {
          return c.compare(o1, o2);
        }
      };
    }
  },

  /**
   * Descending sort order.
   */
  DESC {
    @Override
    public <X> Comparator<X> comparator(final Comparator<X> c) {
      return new Comparator<X>() {
        public int compare(X o1, X o2) {
          return c.compare(o2, o1);
        }
      };
    }
  };

  /**
   * Toggles the given sort direction, that is given one sort direction, it
   * returns the other.
   * 
   * @param sortDir the sort direction to toggle
   * @return the toggled sort direction
   */
  public static SortDir toggle(SortDir sortDir) {
    return (sortDir == ASC) ? DESC : ASC;
  }

  /**
   * An example of how to use this :
   * 
   * List<Something> list = ...
   * 
   * Collections.sort(list, SortDir.ASC.comparator(new Comparator() { public int
   * compare(Object o1, Object o2) { return ... } });
   * 
   * 
   * @return a Comparator that wraps the specific comparator that orders the
   *         results according to the sort direction
   */
  public abstract <X> Comparator<X> comparator(Comparator<X> c);
}