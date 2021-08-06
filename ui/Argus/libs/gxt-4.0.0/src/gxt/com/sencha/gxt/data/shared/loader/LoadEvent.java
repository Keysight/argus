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
package com.sencha.gxt.data.shared.loader;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event type for loader events.
 */
public class LoadEvent<C, M> extends GwtEvent<LoadHandler<C, M>> {

  /**
   * Handler type.
   */
  private static Type<LoadHandler<?, ?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<LoadHandler<?, ?>> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<LoadHandler<?, ?>>());
  }

  private C loadConfig;
  private M loadResult;

  /**
   * Creates a load event with given load config and load result.
   * 
   * @param loadConfig the load config for this load event
   * @param loadResult the load result for this load event
   */
  public LoadEvent(C loadConfig, M loadResult) {
    this.loadConfig = loadConfig;
    this.loadResult = loadResult;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<LoadHandler<C, M>> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Returns the load config.
   * 
   * @return the load config
   */
  public C getLoadConfig() {
    return loadConfig;
  }

  /**
   * Returns the load result.
   * 
   * @return the load result
   */
  public M getLoadResult() {
    return loadResult;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Loader<M, C> getSource() {
    return (Loader<M, C>) super.getSource();
  }

  @Override
  protected void dispatch(LoadHandler<C, M> handler) {
    handler.onLoad(this);

  }

}
