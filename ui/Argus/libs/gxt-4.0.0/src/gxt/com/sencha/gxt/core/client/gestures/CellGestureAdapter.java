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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Helper class to build and track gesture instances for use in cells. Subclasses
 * should declare how to actually create the specific gesture they are interested in,
 * and call the #release methods to indicate that the gesture has ended and can be garbage collected.
 * @param <G> the type of gesture to create and manage
 * @param <T> the type of data in the cell, so that it can be managed correctly
 */
public abstract class CellGestureAdapter<G extends GestureRecognizer, T> {

  private static class Details<T> {
    private Context context;
    private Element parent;
    private T value;
    private ValueUpdater<T> updater;

    private Details(Context context, Element parent, T value, ValueUpdater<T> updater) {
      this.context = context;
      this.parent = parent;
      this.value = value;
      this.updater = updater;
    }
  }
  private final Map<Object, G> activeRecognizers = new HashMap<Object, G>();
  private final Map<GestureRecognizer, Details<T>> details = new HashMap<GestureRecognizer, Details<T>>();

  // an "event bus" must be passed through to the adapted GestureRecognizer in order for
  // firing logical events like LongPressEvent to work correctly from a cell
  // TODO consider making final, adding to constructor, and removing setDelegate() method
  private HasHandlers delegate;

  /**
   * Creates a new recognizer instance. This will be called when a rendered cell instance is being interacted
   * with and need to have its events handled correctly.
   *
   * @return a new instance of the recognizer
   */
  protected abstract G createRecognizer();

  /**
   * Indicates that a new event is occurring in the given rendered cell instance that should potentially should
   * be handled by a gesture recognizer.
   *
   * @param context the context of the cell
   * @param parent the parent element of the rendered cell
   * @param value the current value of the cell
   * @param event the event that occurred
   * @param updater a valueupdater to change the cell value
   */
  public void handle(Context context, Element parent, T value, NativeEvent event, ValueUpdater<T> updater) {
    G recognizer = getOrCreateGestureRecognizer(context, parent, value, updater);
    recognizer.setDelegate(this.delegate);
    recognizer.handle(event);
  }

  /**
   * Gets a gesture recognizer for the given context, or creates it using the other provided arguments. Any object
   * returned from this must be correctly released to avoid a memory leak.
   *
   * @param context the context of the cell
   * @param parent the parent element of the rendered cell
   * @param value the current value of the cell
   * @param updater a valueupdater to change the cell value
   * @return the gesture recognizer for the given rendered cell instance
   */
  public G getOrCreateGestureRecognizer(Context context, Element parent, T value, ValueUpdater<T> updater) {
    G recognizer = activeRecognizers.get(context.getKey());
    if (recognizer == null) {
      recognizer = createRecognizer();
      activeRecognizers.put(context.getKey(), recognizer);
      details.put(recognizer, new Details<T>(context, parent, value, updater));
    }
    return recognizer;
  }

  /**
   * Gets the gesture recognizer for the given context, if any exists
   * @param context the context of the rendered cell instance
   * @return the gesture currently associated with that context, or {@code null} if non exists
   */
  public G getGestureRecognizer(Context context) {
    return activeRecognizers.get(context.getKey());
  }

  /**
   * Indicates that the gesture recognizer for the given cell context is no longer required, and can be
   * garbage collected. This usually means that the gesture has ended, the gesture was canceled, or the
   * cell was re-rendered and any old gesture is no longer required.
   * @param context the context of the cell that no longer needs this gesture
   */
  public void release(Context context) {
    G recognizer = activeRecognizers.remove(context.getKey());
    if (recognizer != null) {
//      recognizer.cancel();//TODO is this a good idea? insist on it being done by the caller?
      details.remove(recognizer);
    }
  }

  /**
   * Indicates that the gesture recognizer is no longer required, and can be garbage collected. This
   * usually means that the gesture has ended, the gesture was canceled, or the cell was re-rendered and
   * the old gesture is no longer required.
   * @param gestureRecognizer the gesture that is no longer needed
   */
  public void release(GestureRecognizer gestureRecognizer) {
//    gestureRecognizer.cancel();//TODO is this a good idea? insist on it being done by the caller?
    Details<T> details = this.details.remove(gestureRecognizer);
    if (details != null) {
      activeRecognizers.remove(details.context.getKey());
    }
  }

  public Element getParent(GestureRecognizer gestureRecognizer) {
    return details.get(gestureRecognizer).parent;
  }


  public Context getContext(GestureRecognizer gestureRecognizer) {
    return details.get(gestureRecognizer).context;
  }

  public T getValue(GestureRecognizer gestureRecognizer) {
    return details.get(gestureRecognizer).value;
  }

  public ValueUpdater<T> getValueUpdater(GestureRecognizer gestureRecognizer) {
    return details.get(gestureRecognizer).updater;
  }

  public void setDelegate(HasHandlers delegate) {
    this.delegate = delegate;
  }

}
