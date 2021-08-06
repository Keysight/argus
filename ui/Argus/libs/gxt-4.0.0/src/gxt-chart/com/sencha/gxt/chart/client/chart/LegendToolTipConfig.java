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
package com.sencha.gxt.chart.client.chart;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * Configuration for a {@link Legend} tooltip.
 * 
 * @param <M> the data type used by the config
 */
public class LegendToolTipConfig<M> extends ToolTipConfig {

  private LabelProvider<LegendItem<M>> customLabelProvider;

  /**
   * Returns the custom label provider.
   * 
   * @return the custom label provider
   */
  public LabelProvider<LegendItem<M>> getCustomLabelProvider() {
    return customLabelProvider;
  }

  /**
   * Returns the custom label provider. Overrides numeric label provider.
   * 
   * @param customLabelProvider the custom label provider
   */
  public void setCustomLabelProvider(LabelProvider<LegendItem<M>> customLabelProvider) {
    this.customLabelProvider = customLabelProvider;
  }

  /**
   * Sets a custom {@link ValueProvider} for the tooltip.
   * 
   * @param valueProvider the custom value provider
   * @param labelProvider the label provider used on the value provider
   */
  public <V> void setValueProvider(final ValueProvider<LegendItem<M>, V> valueProvider,
      final LabelProvider<V> labelProvider) {
    this.customLabelProvider = new LabelProvider<LegendItem<M>>() {
      @Override
      public String getLabel(LegendItem<M> item) {
        return labelProvider.getLabel(valueProvider.getValue(item));
      }
    };
  }
}
