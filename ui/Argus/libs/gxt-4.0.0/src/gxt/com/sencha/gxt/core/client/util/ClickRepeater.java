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
package com.sencha.gxt.core.client.util;

import java.util.Date;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.ClickRepeaterEvent.ClickRepeaterHandler;
import com.sencha.gxt.core.client.util.ClickRepeaterEvent.HasClickRepeaterHandlers;

/**
 * A utility class that continues to fire a "click" event when the user holds
 * the mouse key down.
 */
public class ClickRepeater implements HasClickRepeaterHandlers {

  private class Handler implements MouseDownHandler, MouseOverHandler, MouseOutHandler, TouchStartHandler,
          TouchEndHandler, com.google.gwt.event.logical.shared.AttachEvent.Handler {

    @Override
    public void onAttachOrDetach(AttachEvent event) {
      if (event.isAttached()) {
        doAttach();
      } else {
        doDetach();
      }

    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
      XElement target = event.getNativeEvent().getEventTarget().cast();
      if (el == target) {
        event.stopPropagation();
        event.preventDefault();
        preview.add();
        handleMouseDown();
      }
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
      XElement target = event.getNativeEvent().getEventTarget().cast();
      if (el == target) {
        handleMouseOut();
      }

    }

    @Override
    public void onMouseOver(MouseOverEvent event) {
      XElement target = event.getNativeEvent().getEventTarget().cast();
      if (el == target) {
        handleMouseReturn();
      }
    }

    @Override
    public void onTouchStart(TouchStartEvent event) {
      XElement target = event.getNativeEvent().getEventTarget().cast();
      if (el == target) {
        event.stopPropagation();
        event.preventDefault();
        preview.add();
        handleMouseDown();
      }
    }

    @Override
    public void onTouchEnd(TouchEndEvent event) {
      XElement target = event.getNativeEvent().getEventTarget().cast();
      if (el == target) {
        handleMouseOut();
      }
    }
  }

  private XElement el;

  private Widget target;
  private boolean accelerate;

  private int delay = 250;
  private int interval = 20;
  private Date mousedownTime;
  private BaseEventPreview preview;
  private String pressClass;
  private Timer timer;
  private boolean waitForMouseOut;

  private boolean waitForMouseOver;
  private SimpleEventBus eventBus;

  private Handler handler = new Handler();

  /**
   * Creates a new click repeater.
   * 
   * @param target the target widget
   * @param el the element to be clicked
   */
  public ClickRepeater(Widget target, XElement el) {
    this.target = target;
    this.el = el;
    preview = new BaseEventPreview() {
      protected boolean onPreview(NativePreviewEvent pe) {
        if (pe.getTypeInt() == Event.ONMOUSEUP) {
          ClickRepeater.this.handleMouseUp();
        }
        return true;
      }
    };
    preview.setAutoHide(false);

    target.addDomHandler(handler, MouseDownEvent.getType());
    target.addDomHandler(handler, MouseOutEvent.getType());
    target.addDomHandler(handler, MouseOverEvent.getType());
    if (GXT.isTouch()) {
      target.addDomHandler(handler, TouchStartEvent.getType());
      target.addDomHandler(handler, TouchEndEvent.getType());
    }
    target.addAttachHandler(handler);

    if (target.isAttached()) {
      doAttach();
    }
  }

  @Override
  public HandlerRegistration addClickHandler(ClickRepeaterHandler handler) {
    return ensureHandlers().addHandler(ClickRepeaterEvent.getType(), handler);
  }

  /**
   * Returns the amount before events are fired once the user holds the mouse
   * down.
   * 
   * @return the delay in milliseconds
   */
  public int getDelay() {
    return delay;
  }

  /**
   * Returns the "click" element.
   * 
   * @return the element
   */
  public XElement getEl() {
    return el;
  }

  /**
   * Returns the amount of time between "clicks".
   * 
   * @return the time in milliseconds
   */
  public int getInterval() {
    return interval;
  }

  /**
   * Returns the press CSS style name.
   * 
   * @return the press class
   */
  public String getPressClass() {
    return pressClass;
  }

  public Widget getTarget() {
    return target;
  }

  /**
   * Returns true if acceleration is enabled.
   * 
   * @return true if enabled
   */
  public boolean isAccelerate() {
    return accelerate;
  }

  /**
   * True if autorepeating should start slowly and accelerate (defaults to
   * false). "interval" and "delay" are ignored.
   * 
   * @param accelerate true to accelerate
   */
  public void setAccelerate(boolean accelerate) {
    this.accelerate = accelerate;
  }

  /**
   * The initial delay before the repeating event begins firing (defaults to
   * 250). Similar to an autorepeat key delay.
   * 
   * @param delay the delay in milliseconds
   */
  public void setDelay(int delay) {
    this.delay = delay;
  }

  /**
   * Sets the interval (defaults to 250).
   * 
   * @param interval the interval in milliseconds
   */
  public void setInterval(int interval) {
    this.interval = interval;
  }

  /**
   * A CSS class name to be applied to the element while pressed.
   * 
   * @param pressClass the style name
   */
  public void setPressClass(String pressClass) {
    this.pressClass = pressClass;
  }

  // private
  protected void click() {
    ensureHandlers().fireEventFromSource(new ClickRepeaterEvent(), this);

    timer.schedule(accelerate ? easeOutExpo(new Date().getTime() - mousedownTime.getTime(), 400, -390, 12000)
        : interval);
  }

  protected void doAttach() {
    el.disableTextSelection(true);
  }

  protected void doDetach() {
    el.disableTextSelection(false);
  }

  protected int easeOutExpo(long t, int b, int c, int d) {
    return (int) ((t == d) ? b + c : c * (-Math.pow(2, -10 * t / d) + 1) + b);
  }

  protected void handleMouseDown() {
    if (timer == null) {
      timer = new Timer() {
        public void run() {
          click();
        }
      };
    }
    timer.cancel();
    el.blur();

    if (pressClass != null) {
      el.addClassName(pressClass);
    }
    mousedownTime = new Date();

    waitForMouseOut = true;
    // fireEvent(Events.OnMouseDown);
    ensureHandlers().fireEventFromSource(new ClickRepeaterEvent(), this);

    // Do not honor delay or interval if acceleration wanted.
    if (accelerate) {
      delay = 400;
    }
    timer.schedule(delay);
  }

  protected void handleMouseOut() {
    if (waitForMouseOut) {
      timer.cancel();
      if (pressClass != null) {
        el.removeClassName(pressClass);
      }
      waitForMouseOver = true;
    }
  }

  protected void handleMouseReturn() {
    if (waitForMouseOver) {
      waitForMouseOver = false;
      if (pressClass != null) {
        el.addClassName(pressClass);
      }
      click();
    }
  }

  protected void handleMouseUp() {
    if (waitForMouseOut) {
      timer.cancel();
      waitForMouseOut = false;
      waitForMouseOver = false;

      if (pressClass != null) {
        el.removeClassName(pressClass);
      }

      preview.remove();
      // fireEvent(Events.OnMouseUp);
    }
  }

  SimpleEventBus ensureHandlers() {
    return eventBus == null ? eventBus = new SimpleEventBus() : eventBus;
  }
}
