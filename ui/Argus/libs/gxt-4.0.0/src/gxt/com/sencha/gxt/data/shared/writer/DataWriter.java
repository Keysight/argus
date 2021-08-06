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
package com.sencha.gxt.data.shared.writer;

/**
 * Data writers are a simple abstraction to turn logical objects into a format
 * that can more easily be sent over the wire. In many cases, such as saving
 * user changes, this simply means turning objects back into JSON or XML, the
 * format they were loaded in from the server (effectively the reverse of
 * {@link com.sencha.gxt.data.shared.loader.DataReader}). In other cases, this
 * can mean copying properties from load configs into an object format which can
 * be sent via RPC or RequestFactory.
 * 
 * In contrast with DataReader, this is not expected to have access to any other
 * context such as a load config, it operates directly on the data as-is. Custom
 * implementations can provide other context, but none is available by default.
 * 
 * @param <M> the source type (starting data format) of the model
 * @param <D> the target type (data format to be produced), usually to send the
 *          data over the wire
 */
public interface DataWriter<M, D> {
  /**
   * Converts a source model to its equivalent target type.
   * 
   * @param model the source model
   * @return the equivalent target type
   */
  D write(M model);
}
