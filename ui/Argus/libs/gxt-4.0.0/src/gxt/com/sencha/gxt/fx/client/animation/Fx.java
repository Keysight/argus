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

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent.AfterAnimateHandler;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent.HasAfterAnimateHandlers;
import com.sencha.gxt.fx.client.animation.BeforeAnimateEvent.BeforeAnimateHandler;
import com.sencha.gxt.fx.client.animation.BeforeAnimateEvent.HasBeforeAnimateHandlers;
import com.sencha.gxt.fx.client.animation.CancelAnimationEvent.CancelAnimationHandler;
import com.sencha.gxt.fx.client.animation.CancelAnimationEvent.HasCancelAnimationHandlers;

/**
 * Runs an effect and fires events. The run multiple effects see
 * {@link MultiEffect}.
 */
public class Fx implements HasAfterAnimateHandlers, HasBeforeAnimateHandlers, HasCancelAnimationHandlers {

  private boolean isRunning;
  private SimpleEventBus eventBus;

  protected Effect effect;
  protected Animator animation;
  protected int duration = 250;

  public Fx() {
    animation = new Animator() {

      @Override
      public void onCancel() {
        Fx.this.onCancel();
        isRunning = false;
      }

      @Override
      public void onComplete() {
        Fx.this.onComplete();
        isRunning = false;
      }

      @Override
      public void onStart() {
        isRunning = true;
        Fx.this.onStart();
      }

      @Override
      public void onUpdate(double progress) {
        Fx.this.onUpdate(progress);
      }

    };
  }

  public Fx(int duration) {
    this();
    this.duration = duration;
  }

  @Override
  public HandlerRegistration addAfterAnimateHandler(AfterAnimateHandler handler) {
    return ensureHandlers().addHandler(AfterAnimateEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addBeforeAnimateHandler(BeforeAnimateHandler handler) {
    return ensureHandlers().addHandler(BeforeAnimateEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addCancelAnimationHandler(CancelAnimationHandler handler) {
    return ensureHandlers().addHandler(CancelAnimationEvent.getType(), handler);
  }

  /**
   * Cancels the effect.
   */
  public void cancel() {
    animation.cancel();
  }

  /**
   * Returns the effect.
   * 
   * @return the effect
   */
  public Effect getEffect() {
    return effect;
  }

  /**
   * Runs the effect for the given duration.
   * 
   * @param effect the effect run
   * @return true if the effect is run
   */
  public boolean run(Effect effect) {
    return run(duration > 0 ? duration : 500, effect);
  }

  /**
   * Runs the effect for the given duration.
   * 
   * @param duration the effect duration in milliseconds
   * @param effect the effect run
   * @return true if the effect is run
   */
  public boolean run(int duration, Effect effect) {
    if (isRunning) return false;
    this.effect = effect;
    animation.run(duration);
    return true;
  }

  protected boolean fireCancellableEvent(GwtEvent<?> event) {
    ensureHandlers().fireEvent(event);
    if (event instanceof CancellableEvent) {
      return !((CancellableEvent) event).isCancelled();
    }
    return true;
  }

  protected void onCancel() {
    effect.onCancel();
    ensureHandlers().fireEvent(new CancelAnimationEvent());
  }

  protected void onComplete() {
    effect.onComplete();
    ensureHandlers().fireEvent(new AfterAnimateEvent());
  }

  protected void onStart() {
    effect.onStart();
    ensureHandlers().fireEvent(new BeforeAnimateEvent());
  }

  protected void onUpdate(double progress) {
    effect.onUpdate(progress);
  }

  /**
   * Instantiates the eventBus if not already instantiated.
   * 
   * @return the eventBus
   */
  SimpleEventBus ensureHandlers() {
    return eventBus == null ? eventBus = new SimpleEventBus() : eventBus;
  }
}
