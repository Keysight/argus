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
package com.sencha.gxt.core.client.gestures;

import com.sencha.gxt.core.client.GXT;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum PointerEvents {

  POINTERDOWN("MSPointerDown", "pointerdown"),
  POINTERUP("MSPointerUp", "pointerup"),
  POINTERCANCEL("MSPointerCancel", "pointercancel"),
  POINTERMOVE("MSPointerMove", "pointermove"),
  POINTEROVER("MSPointerOver", "pointerover"),
  POINTERENTER("MSPointerEnter", "pointerenter"),
  POINTERLEAVE("MSPointerLeave", "pointerleave");

  private static final Set<String> LOOKUP;
  static {
    Set<String> lookup = new HashSet<String>(values().length);
    for (PointerEvents pointerEvent : values()) {
      lookup.add(pointerEvent.eventName);
    }
    LOOKUP = Collections.unmodifiableSet(lookup);
  }

  private final String eventName;

  private PointerEvents(String msPrefixedEventName, String eventName) {
    this.eventName = GXT.isIE11() || GXT.isMSEdge() ? eventName : msPrefixedEventName;
  }

  public String getEventName() {
    return eventName;
  }

  public static boolean isPointerEvent(String eventType) {
    return LOOKUP.contains(eventType);
  }
}
