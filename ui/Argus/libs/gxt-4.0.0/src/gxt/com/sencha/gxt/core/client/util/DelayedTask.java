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

import com.google.gwt.user.client.Timer;

/**
 * A <code>Timer</code> that is canceled if a new request is made.
 */
public abstract class DelayedTask {

  private Timer timer;

  /**
   * Creates a new delayed task.
   */
  public DelayedTask() {
    timer = new Timer() {
      public void run() {
        onExecute();
      }
    };
  }

  /**
   * Cancels the task.
   */
  public void cancel() {
    timer.cancel();
  }

  /**
   * Cancels any running timers and starts a new one.
   * 
   * @param delay the delay in ms
   */
  public void delay(int delay) {
    timer.cancel();
    if (delay > 0) {
      timer.schedule(delay);
    } else {
      timer.run();
    }
  }

  /**
   * Called when the task should execute.
   */
  public abstract void onExecute();

}
