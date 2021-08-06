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

import com.google.gwt.animation.client.Animation;
import com.sencha.gxt.fx.client.easing.Default;
import com.sencha.gxt.fx.client.easing.EasingFunction;

/**
 * Adds additional features and functionality to the {@link Animation} class.
 * 
 * The {@link EasingFunction} replaces the interpolation of the
 * {@link Animation}'s progress.
 */
public abstract class Animator extends Animation {

  private EasingFunction easing = new Default();

  /**
   * Returns the {@link EasingFunction} used in this animation.
   * 
   * @return the {@link EasingFunction} used in this animation
   */
  public EasingFunction getEasing() {
    return easing;
  }

  /**
   * Calls {@link Animation#run(int, double)} and sets the
   * {@link EasingFunction} to be used in the animation.
   */
  public void run(int duration, double startTime, EasingFunction easing) {
    this.easing = easing;
    run(duration, startTime);
  }

  /**
   * Calls {@link Animation#run(int)} and sets the {@link EasingFunction} to be
   * used in the animation.
   */
  public void run(int duration, EasingFunction easing) {
    this.easing = easing;
    run(duration);
  }

  /**
   * Sets the {@link EasingFunction} that the animation will use.
   * 
   * @param easing the {@link EasingFunction} that the animation will use
   */
  public void setEasing(EasingFunction easing) {
    this.easing = easing;
  }

  @Override
  protected double interpolate(double progress) {
    return easing.func(progress);
  }

}
