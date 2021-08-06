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

import java.util.ArrayList;
import java.util.List;

import com.sencha.gxt.core.client.dom.XElement;

public class MultiEffect extends BaseEffect {

  protected List<Effect> effects;

  public MultiEffect() {
    this(null);
  }

  public MultiEffect(XElement el) {
    super(el);
    effects = new ArrayList<Effect>();
  }

  public void addEffects(Effect... effects) {
    for (int i = 0; i < effects.length; i++) {
      this.effects.add(effects[i]);
    }
  }

  public void addEffects(List<Effect> effects) {
    for (Effect e : effects) {
      this.effects.add(e);
    }
  }

  @Override
  public void onCancel() {
    for (Effect e : effects) {
      e.onCancel();
    }
  }

  @Override
  public void onComplete() {
    for (Effect e : effects) {
      e.onComplete();
    }
  }

  @Override
  public void onStart() {
    for (Effect e : effects) {
      e.onStart();
    }
  }

  @Override
  public void onUpdate(double progress) {
    for (Effect e : effects) {
      e.onUpdate(progress);
    }
  }

}
