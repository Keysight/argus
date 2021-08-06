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

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.sencha.gxt.state.client.BorderLayoutStateHandler.BorderLayoutState;
import com.sencha.gxt.state.client.GridFilterStateHandler.GridFilterState;
import com.sencha.gxt.state.client.GridStateHandler.GridState;
import com.sencha.gxt.state.client.TreeStateHandler.TreeState;

/**
 * Default <code>AutoBeanFactory</code> used by the {@link StateManager}. The
 * auto bean factory is specified using a module rule:
 * &lt;set-configuration-property name="GXT.state.autoBeanFactory"
 * value="com.sencha.gxt.state.client.DefaultStateAutoBeanFactory" />.
 * 
 * <p />
 * To add additional beans to the factory, this interface should be extended.
 * The new interface should then be specified in your applications module file
 * to 'override' the current rule.
 */
public interface DefaultStateAutoBeanFactory extends AutoBeanFactory {
  
  AutoBean<TreeState> tree();

  AutoBean<BorderLayoutState> borderLayout();

  AutoBean<GridState> grid();

  AutoBean<GridFilterState> gridFilter();
}
