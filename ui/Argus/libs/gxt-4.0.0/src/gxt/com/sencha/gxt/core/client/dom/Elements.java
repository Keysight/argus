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

import java.util.List;

import com.google.gwt.dom.client.Element;

/**
 * Utility class for identifying elements either as a single element, array of
 * elements, a id, and index.
 */
public class Elements {

  public enum Type {
    /**
     * Representing a <b>Array</b>
     */
    ARRAY, 
    /**
     * Representing a <b>Element</b>
     */
    ELEMENT, 
    /**
     * Representing a <b>Id</b>
     */
    ID,
    /**
     * Representing a <b>Index</b>
     */
    INDEX
  }

  private Element element;
  private Element[] elements;
  private String id;
  private int index;

  private boolean isArray;
  private boolean isElement;
  private boolean isId;
  private boolean isIndex;
  private Type type;

  public Elements(Element element) {
    this.element = element;
    type = Type.ELEMENT;
    isElement = true;
  }

  public Elements(Element[] elements) {
    this.elements = elements;
    type = Type.ARRAY;
    isArray = true;
  }

  /**
   * Creates a new index element info.
   * 
   * @param index the index
   */
  public Elements(int index) {
    this.index = index;
    type = Type.INDEX;
    isIndex = true;
  }

  /**
   * Creates a new instance.
   * 
   * @param elements the list of elements
   */
  public Elements(List<Element> elements) {
    this(elements.toArray(new Element[0]));
  }

  /**
   * Creates a new id element info.
   * 
   * @param id the id
   */
  public Elements(String id) {
    this.id = id;
    type = Type.ID;
    isId = true;
  }

  /**
   * Returns the element when {@link Type#ELEMENT}.
   * 
   * @return the element
   */
  public Element getElement() {
    return element;
  }

  /**
   * Returns the array of elements when {@link Type#ARRAY}.
   * 
   * @return the element array
   */
  public Element[] getElements() {
    return elements;
  }

  /**
   * Returns the element id when {@link Type#ID}.
   * 
   * @return the element id
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the index when {@link Type#ARRAY}.
   * 
   * @return the index
   */
  public int getIndex() {
    return index;
  }

  /**
   * Returns the element info type.
   * 
   * @return the type
   */
  public Type getType() {
    return type;
  }

  /**
   * Returns true if the value is an array.
   * 
   * @return true if an array
   */
  public boolean isArray() {
    return isArray;
  }

  /**
   * Returns true if the value is an element.
   * 
   * @return true if an element
   */
  public boolean isElement() {
    return isElement;
  }

  /**
   * Returns true if the value is an id.
   * 
   * @return true if an id
   */
  public boolean isId() {
    return isId;
  }

  /**
   * Returns true if the value is an index.
   * 
   * @return true if an index
   */
  public boolean isIndex() {
    return isIndex;
  }

}
