/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktop.client.theme.base.shortcut;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.sencha.gxt.desktop.client.widget.ShortcutCell.ShortcutCellAppearance;
import com.sencha.gxt.theme.base.client.button.ButtonCellDefaultAppearance;
import com.sencha.gxt.theme.base.client.frame.TableFrame;
import com.sencha.gxt.theme.base.client.frame.TableFrame.TableFrameResources;

public class ShortcutCellDefaultAppearance<C> extends ButtonCellDefaultAppearance<C> implements
    ShortcutCellAppearance<C> {

  public interface ShortcutCellResources extends ButtonCellResources, ClientBundle {
    @Source({"com/sencha/gxt/theme/base/client/button/ButtonCell.gss", "ShortcutCell.gss"})
    @Override
    ShortcutCellStyle style();
  }

  public interface ShortcutCellStyle extends ButtonCellStyle {
  }

  public ShortcutCellDefaultAppearance() {
    super(GWT.<ButtonCellResources> create(ShortcutCellResources.class),
        GWT.<ButtonCellTemplates> create(ButtonCellTemplates.class), new TableFrame(
            GWT.<TableFrameResources> create(ShortcutTableFrameResources.class)));
  }

}
