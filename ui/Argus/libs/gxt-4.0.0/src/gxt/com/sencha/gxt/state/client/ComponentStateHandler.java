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
package com.sencha.gxt.state.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.Component;

/**
 * Abstract state handler for Components, capable of using the widget's stateId
 * property as a key instead of being given one.
 * 
 * Will emit a warning in hosted mode (and if enabled, in production mode) if
 * the stateId is generated, as this might change between page loads, or as the
 * application changes.
 * 
 * @param <S> the state type
 * @param <C> the component
 */
public abstract class ComponentStateHandler<S, C extends Component> extends AbstractStateHandler<S, C> {

  private static Logger logger = Logger.getLogger(ComponentStateHandler.class.getName());

  public ComponentStateHandler(Class<S> stateType, C component) {
    super(stateType, component, component.getStateId());

    if (!GWT.isProdMode() && component.getStateId().startsWith("x-widget-")) {
      logger.warning(component.getStateId() + " State handler given a widget with a generated state id, this can result in state being incorrectly applied as generated ids change");
    }
  }

  public ComponentStateHandler(Class<S> stateType, C component, String key) {
    super(stateType, component, key);
  }

}
