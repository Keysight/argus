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
package com.sencha.gxt.core.client.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.sencha.gxt.core.client.util.Util;

/**
 * Wraps an list of elements, allowing code to be applied to each element using
 * a {@link CompositeFunction}.
 */
public class CompositeElement {

  /**
   * The list of elements.
   */
  protected List<Element> items;

  /**
   * Creates a new composite element.
   */
  public CompositeElement() {
    items = new ArrayList<Element>();
  }

  /**
   * Creates a new composite element.
   * 
   * @param elements the initial elements
   */
  public CompositeElement(Element[] elements) {
    items = Util.createList(elements);
  }

  /**
   * Creates a new composite element.
   * 
   * @param elements the initial elements
   */
  public CompositeElement(List<Element> elements) {
    items = elements;
  }

  /**
   * Adds an element.
   * 
   * @param elem the element to add
   */
  public void add(Element elem) {
    insert(elem, getCount());
  }

  /**
   * Adds the elements.
   * 
   * @param elements the elements to add
   */
  public void add(Elements elements) {
    if (elements.isElement()) {
      add(elements.getElement());
    } else if (elements.isArray()) {
      items.addAll(Arrays.asList(elements.getElements()));
    } else if (elements.isId()) {
      add(DOM.getElementById(elements.getId()));
    }
  }

  /**
   * Removes the class name.
   * 
   * @param cls the class name
   */
  public void addClassName(String cls) {
    for (int i = 0, len = items.size(); i < len; i++) {
      items.get(i).addClassName(cls);
    }
  }

  /**
   * Returns true if this composite contains the passed element.
   * 
   * @param elem the element
   * @return the contains state
   */
  public boolean contains(Element elem) {
    return items != null && items.contains(elem);
  }

  /**
   * Calls the passed function passing (el, this, index) for each element in
   * this composite.
   * 
   * @param f the function
   */
  public void each(CompositeFunction f) {
    int count = items.size();
    for (int i = 0; i < count; i++) {
      f.doFunction(item(i), this, i);
    }
  }

  /**
   * Returns the first element.
   * 
   * @return the element
   */
  public Element first() {
    return item(0);
  }

  /**
   * Returns the number of elements in this composite.
   * 
   * @return the count
   */
  public int getCount() {
    return items.size();
  }

  /**
   * Returns the element at the given index.
   * 
   * @param index the element index
   * @return the element
   */
  public Element getElement(int index) {
    return index >= 0 && index < items.size() ? items.get(index) : null;
  }

  /**
   * The list of elements.
   */
  public List<Element> getElements() {
    return Collections.unmodifiableList(items);
  }

  /**
   * Returns the index of the given element.
   * 
   * @param elem the element
   * @return the index
   */
  public int indexOf(Element elem) {
    if (items != null) {
      return items.indexOf(elem);
    }
    return -1;
  }

  /**
   * Inserts an element at the given index.
   * 
   * @param elem the element to add
   * @param index the insert location
   */
  public void insert(Element elem, int index) {
    items.add(index, elem);
  }

  /**
   * Inserts the elements at the given index.
   * 
   * @param elems the elements to insert
   * @param index the insert location
   */
  public void insert(Element[] elems, int index) {
    items.addAll(index, Arrays.asList(elems));
  }

  /**
   * Returns true if the given element is or is a child of any contained
   * element.
   * 
   * @param elem the element to test
   * @return the is state
   */
  public boolean is(Element elem) {
    int count = items.size();
    for (int i = 0; i < count; i++) {
      Element test = getElement(i);
      if (test.isOrHasChild(elem)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the Element object at the specified index.
   * 
   * @param index the index
   * @return the element
   */
  public Element item(int index) {
    return items != null ? (Element) items.get(index) : null;
  }

  /**
   * Returns the last element.
   * 
   * @return the last element
   */
  public Element last() {
    return item(items.size() == 0 ? 0 : getCount() - 1);
  }

  /**
   * Removes an element.
   * 
   * @param element the element to remove
   */
  public void remove(Element element) {
    if (items != null) {
      items.remove(element);
    }
  }

  /**
   * Removes an element.
   * 
   * @param index the index of the element to remove
   */
  public void remove(int index) {
    if (items != null) {
      items.remove(index);
    }
  }

  /**
   * Removes all elements.
   */
  public void removeAll() {
    items.clear();
  }

  /**
   * Removes the class name.
   * 
   * @param cls the class name
   */
  public void removeClassName(String cls) {
    for (int i = 0, len = items.size(); i < len; i++) {
      items.get(i).removeClassName(cls);
    }
  }

  /**
   * Replaces an element.
   * 
   * @param elem the element to remove
   * @param replace the element to replace
   * @return true if the item was replaced
   */
  public boolean replaceElement(Element elem, Element replace) {
    int i = items.indexOf(elem);
    if (i != -1) {
      remove(elem);
      items.add(i, replace);
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Sets the element's height.
   * 
   * @param height the height
   */
  public void setHeight(int height) {
    for (Element elem : items) {
      elem.getStyle().setHeight(height, Unit.PX);
    }
  }

  /**
   * Sets the element's inner HTML.
   *
   * @param html the html
   */
  public void setInnerSafeHtml(SafeHtml html) {
    for (Element elem : items) {
      elem.setInnerSafeHtml(html);
    }
  }

  /**
   * Sets the element's width.
   * 
   * @param width the width
   */
  public void setWidth(int width) {
    for (Element elem : items) {
      elem.getStyle().setWidth(width, Unit.PX);
    }
  }

  /**
   * Sets the element's width.
   * 
   * @param width the width
   */
  public void setWidth(String width) {
    for (Element elem : items) {
      elem.getStyle().setProperty("width", width);
    }
  }

}
