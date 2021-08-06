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
package com.sencha.gxt.widget.core.client;

import java.util.Set;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

final class CellWidgetImplHelper {
  public native static boolean isFocusable(Element elem) /*-{
                                                         var c = @com.google.gwt.user.cellview.client.CellBasedWidgetImpl::get()();
                                                         return c.@com.google.gwt.user.cellview.client.CellBasedWidgetImpl::isFocusable(Lcom/google/gwt/dom/client/Element;)(element);
                                                         }-*/;

  public native static void onBrowserEvent(Widget widget, Event event) /*-{
                                                                       var c = @com.google.gwt.user.cellview.client.CellBasedWidgetImpl::get()();
                                                                       c.@com.google.gwt.user.cellview.client.CellBasedWidgetImpl::onBrowserEvent(Lcom/google/gwt/user/client/ui/Widget;Lcom/google/gwt/user/client/Event;)(widget, event);
                                                                       }-*/;

  public native static SafeHtml processHtml(SafeHtml html)/*-{
                                                          var c = @com.google.gwt.user.cellview.client.CellBasedWidgetImpl::get()();
                                                          return c.@com.google.gwt.user.cellview.client.CellBasedWidgetImpl::processHtml(Lcom/google/gwt/safehtml/shared/SafeHtml;)(html);
                                                          }-*/;

  public native static void resetFocus(ScheduledCommand command)/*-{
                                                                var c = @com.google.gwt.user.cellview.client.CellBasedWidgetImpl::get()();
                                                                c.@com.google.gwt.user.cellview.client.CellBasedWidgetImpl::resetFocus(Lcom/google/gwt/core/client/Scheduler$ScheduledCommand;)(command);
                                                                }-*/;

  public native static void sinkEvents(Widget widget, Set<String> typeNames)/*-{
                                                                            var c = @com.google.gwt.user.cellview.client.CellBasedWidgetImpl::get()();
                                                                            c.@com.google.gwt.user.cellview.client.CellBasedWidgetImpl::sinkEvents(Lcom/google/gwt/user/client/ui/Widget;Ljava/util/Set;)(widget, typeNames);
                                                                            }-*/;

}
