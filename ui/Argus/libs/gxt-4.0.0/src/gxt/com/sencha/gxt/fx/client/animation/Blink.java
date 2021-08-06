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
package com.sencha.gxt.fx.client.animation;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.core.client.dom.XElement;

public class Blink extends BaseEffect {

  private int interval;
  private Timer t;
  private boolean visible;

  public Blink(final XElement el, int interval) {
    super(el);
    this.interval = interval;
    t = new Timer() {
      @Override
      public void run() {
        el.setVisibility(visible);
        visible = !visible;
      }
    };
  }

  public int getInterval() {
    return interval;
  }

  @Override
  public void onCancel() {
    super.onCancel();
    t.cancel();
  }

  @Override
  public void onComplete() {
    t.cancel();
    // ensure timer is done executing
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        element.setVisibility(true);
      }
    });
  }

  @Override
  public void onStart() {
    t.scheduleRepeating(interval);
  }

  @Override
  public void onUpdate(double progress) {

  }

  public void setInterval(int interval) {
    this.interval = interval;
  }

}
