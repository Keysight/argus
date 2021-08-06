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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.AccessStack;
import com.sencha.gxt.widget.core.client.event.RegisterEvent;
import com.sencha.gxt.widget.core.client.event.RegisterEvent.HasRegisterHandlers;
import com.sencha.gxt.widget.core.client.event.RegisterEvent.RegisterHandler;
import com.sencha.gxt.widget.core.client.event.UnregisterEvent;
import com.sencha.gxt.widget.core.client.event.UnregisterEvent.HasUnregisterHandlers;
import com.sencha.gxt.widget.core.client.event.UnregisterEvent.UnregisterHandler;

/**
 * An object that represents a group of {@link Widget} instances and provides
 * z-order management and window activation behavior. By default, WindowManager
 * only provides these features for the {@link Window} class, as {@code Widget}s
 * don't have a concept of activation or moving to the front.
 * 
 * Can be extended and replaced to support non-{@code Window} classes - the main
 * extension points will be the {@link #activateLast()} and {@link #setActiveWin(Widget)}
 * methods, which should be made general enough for the specific use case. Then, 
 * the global {@code WindowManager} can be replaced with the subclass in the project
 * module file:
 * <pre><code>
&lt;replace-with class="com.example.project.client.ReplacementWindowManager"&gt;
    &lt;when-type-is class="com.sencha.gxt.widget.core.client.WindowManager" /&gt;
&lt;/replace-with&gt;
 */
public class WindowManager implements HasRegisterHandlers<Widget>, HasUnregisterHandlers<Widget> {

  private static WindowManager instance;

  /**
   * Returns the singleton instance.
   * 
   * @return the window manager
   */
  public static WindowManager get() {
    if (instance == null) instance = GWT.create(WindowManager.class);
    return instance;
  }

  private AccessStack<Widget> accessList;
  private Widget front;
  private List<Widget> widgets;
  private SimpleEventBus eventBus;

  /**
   * Creates a window manager.
   */
  public WindowManager() {
    accessList = new AccessStack<Widget>();
    widgets = new ArrayList<Widget>();
  }

  /**
   * Activates the next window in the access stack.
   * 
   * @param widget the reference window
   * @return true if the next window exists
   */
  public boolean activateNext(Widget widget) {
    int count = widgets.size();
    if (count > 1) {
      int idx = widgets.indexOf(widget);
      if (idx == count - 1) {
        return false;
      }
      setActiveWin(widgets.get(++idx));
      return true;
    }
    return false;
  }

  /**
   * Activates the previous widget in the access stack.
   * 
   * @param widget the reference window
   * @return true if a previous window exists
   */
  public boolean activatePrevious(Widget widget) {
    int count = widgets.size();
    if (count > 1) {
      int idx = widgets.indexOf(widget);
      if (idx == 0) {
        return false;
      }
      setActiveWin(widgets.get(--idx));
    }
    return false;
  }

  @Override
  public HandlerRegistration addRegisterHandler(RegisterHandler<Widget> handler) {
    return ensureHandlers().addHandler(RegisterEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addUnregisterHandler(UnregisterHandler<Widget> handler) {
    return ensureHandlers().addHandler(UnregisterEvent.getType(), handler);
  }

  /**
   * Brings the specified widget to the front of any other active widgets.
   * {@link Window} instances will automatically call this method when focused,
   * all other types must call manually.
   * 
   * @param widget the window
   * @return true if the widget was brought to the front, else false if it was
   *         already in front
   */
  public boolean bringToFront(Widget widget) {
    if (widget != front) {
      accessList.add(widget);
      activateLast();
      return true;
    } else {
      focus(widget);
    }
    return false;
  }

  /**
   * Gets the currently-active widget in the group.
   * 
   * @return the active window
   */
  public Widget getActive() {
    return front;
  }

  /**
   * Returns the ordered widgets.
   * 
   * @return the widgets
   */
  public Stack<Widget> getStack() {
    return accessList.getStack();
  }

  /**
   * Returns the visible widgets.
   * 
   * @return the widgets
   */
  public List<Widget> getWindows() {
    return Collections.unmodifiableList(widgets);
  }

  /**
   * Hides all widgets that are registered to this WindowManager.
   */
  public void hideAll() {
    for (int i = accessList.size() - 1; i >= 0; --i) {
      accessList.get(i).setVisible(false);
    }
  }

  /**
   * Registers the window with the WindowManager. {@link Window} instances will
   * automatically register and unregister themselves. All other types must
   * register / unregister manually.
   * 
   * @param widget the window
   */
  public void register(Widget widget) {
    accessList.add(widget);
    widgets.add(widget);
    ensureHandlers().fireEvent(new RegisterEvent<Widget>(widget));
  }

  /**
   * Sends the specified window to the back of other active widgets.
   * 
   * @param widget the widget
   */
  public void sendToBack(Widget widget) {
    accessList.getStack().pop();
    accessList.getStack().insertElementAt(widget, 0);
    activateLast();
  }

  /**
   * Unregisters the window with the WindowManager.
   * 
   * @param widget the widget
   */
  public void unregister(Widget widget) {
    if (front == widget) {
      front = null;
    }
    accessList.remove(widget);
    widgets.remove(widget);
    activateLast();
    ensureHandlers().fireEvent(new UnregisterEvent<Widget>(widget));
  }

  SimpleEventBus ensureHandlers() {
    return eventBus == null ? eventBus = new SimpleEventBus() : eventBus;
  }

  /**
   * Activates the top window in the stack. Protected so it can be overridden to activate
   * more than just GXT Window objects.
   */
  protected void activateLast() {
    for (int i = accessList.size() - 1; i >= 0; --i) {
      Widget last = accessList.get(i);
      if (last instanceof Window) {
        Window w = (Window) last;
        if (w.isVisible()) {
          setActiveWin(w);
          return;
        }
      }
    }
    setActiveWin(null);
  }

  protected void focus(Widget widget) {
    if (widget instanceof Component) {
      ((Component) widget).focus();
    } else {
      widget.getElement().focus();
    }
  }

  /**
   * Indicates that this window should be made active. Protected so it can be overridden
   * to provide specialized behavior when more than GXT Windows are used.
   * @param window
   */
  protected void setActiveWin(Widget window) {
    if (window != front) {
      if (front != null && front instanceof Window) {
        ((Window) front).setActive(false);
      }

      front = window;
      if (window != null) {
        if (window instanceof Window) {
          Window w = (Window) window;
          w.setActive(true);
          w.setZIndex(XDOM.getTopZIndex(10));
        } else {
          window.getElement().<XElement> cast().setZIndex(XDOM.getTopZIndex(10));
        }

        focus(window);
      }
    }
  }
}
