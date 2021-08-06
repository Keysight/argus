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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Groups gestures together, in order of which should have first crack an an incoming event. Does not support
 * {@link #start(java.util.List)} or {@link #getTouches()}.
 */
public class GestureChain implements GestureRecognizer {
  private final List<GestureRecognizer> gestures;

  public GestureChain(GestureRecognizer... gestures) {
    this(Arrays.asList(gestures));
  }

  public GestureChain(List<GestureRecognizer> gestures) {
    this.gestures = gestures;
  }

  @Override
  public void cancel() {
    for (int i = 0; i < gestures.size(); i++) {
      gestures.get(i).cancel();
    }
  }

  @Override
  public List<TouchData> getTouches() {
    throw new UnsupportedOperationException("Can't return specific touches from a chain of gestures");
  }

  @Override
  public boolean handle(NativeEvent event) {
    Iterator<GestureRecognizer> iter = gestures.iterator();
    while (iter.hasNext()) {
      if (!iter.next().handle(event)) {
        //that handler took the event, don't give it to a later handler, and return false overall
        return false;
      }
    }
    //no one asked that the event be stopped from propagating, so return true
    return true;
  }

  @Override
  public void start(List<TouchData> touches) {
    throw new UnsupportedOperationException("Can't start a chain of gestures");
  }

  @Override
  public void setDelegate(HasHandlers eventDelegate) {
    for (int i = 0; i < gestures.size(); i++) {
      gestures.get(i).setDelegate(eventDelegate);
    }
  }
}
