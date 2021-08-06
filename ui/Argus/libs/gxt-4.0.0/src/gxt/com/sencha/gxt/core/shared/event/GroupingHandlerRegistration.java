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
package com.sencha.gxt.core.shared.event;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Groups multiple handler registrations allowing them to be removed as a group.
 */
public class GroupingHandlerRegistration implements HandlerRegistration {

  private Set<HandlerRegistration> registrations;

  /**
   * Creates a new grouping handler registration.
   */
  public GroupingHandlerRegistration() {
    registrations = new HashSet<HandlerRegistration>();
  }

  /**
   * Adds a hander registration.
   * 
   * @param registration the handler registration
   */
  public void add(HandlerRegistration registration) {
    registrations.add(registration);
  }

  /**
   * Returns the list of handler registrations.
   * 
   * @return the list of registrations
   */
  public Set<HandlerRegistration> getRegistrations() {
    return registrations;
  }

  public void removeHandler() {
    for (HandlerRegistration r : registrations) {
      r.removeHandler();
    }
    registrations.clear();
  }

}
