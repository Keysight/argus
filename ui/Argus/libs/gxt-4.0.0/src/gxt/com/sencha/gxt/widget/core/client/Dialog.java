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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.HasDialogHideHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * A <code>Window</code> with specialized support for buttons. Defaults to a dialog with an 'ok' button.</p>
 * 
 * Code snippet:
 * 
 * <pre>
 * Dialog d = new Dialog();
 * d.setHeading("Exit Warning!");
 * d.setWidget(new HTML("Do you wish to save before exiting?"));
 * d.setBodyStyle("fontWeight:bold;padding:13px;");
 * d.setPixelSize(300, 100);
 * d.setHideOnButtonClick(true);
 * d.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
 * d.show();
 * </pre>
 * 
 * <p />
 * To check which button is clicked using the <a href='http://www.gwtproject.org/articles/mvp-architecture.html'>basic
 * MVP pattern</a>, add a {@link HasSelectHandlers} or {@link HasDialogHideHandlers} method to the view's display
 * interface defined by the presenter:
 * 
 * <pre>
 * public interface Display extends IsWidget {
 *   HasSelectHandlers getYesButton();
 *   HasDialogHideHandlers getDialog();
 * }
 * </pre>
 * 
 * <p />
 * 
 * Add an implementation of the method(s) to the view:
 * 
 * <pre>
 * {@literal @}Override
 * public HasSelectHandlers getYesButton() {
 *   return dialog.getButton(PredefinedButton.YES);
 * }
 *
 * {@literal @}Override
 * public HasDialogHideHandlers getDialog() {
 *   return dialog;
 * }
 * </pre>
 * <p />
 * And add a handler for the method(s) to the presenter:
 * 
 * <pre>
 *   display.getYesButton().addSelectHandler(new SelectHandler() {
 *     {@literal @}Override
 *     public void onSelect(SelectEvent event) {
 *       // Take action when user hides dialog by clicking on YES button
 *     }
 *   });
 *   
 *   display.getDialog().addDialogHideHandler(new DialogHideHandler() {
 *     {@literal @}Override
 *     public void onDialogHide(DialogHideEvent event) {
 *       // Invoke event.getHideButton() and take action based on value of returned enum
 *     }
 *   });
 * </pre>
 * 
 * Note: this example illustrates using both a select handler and a dialog hide handler; you can use either approach.
 */
public class Dialog extends Window implements HasDialogHideHandlers {

  /**
   * The translatable strings (e.g. button text and ToolTips) for the dialog window.
   */
  public interface DialogMessages {

    /**
     * Returns the text that appears on the button for {@link PredefinedButton#CANCEL}.
     * 
     * @return the "Cancel" button text
     */
    String cancel();

    /**
     * Returns the text that appears on the button for {@link PredefinedButton#CLOSE}.
     * 
     * @return the "Close" button text
     */
    String close();

    /**
     * Returns the text that appears on the button for {@link PredefinedButton#NO}.
     * 
     * @return the "No" button text
     */
    String no();

    /**
     * Returns the text that appears on the button for {@link PredefinedButton#OK}.
     * 
     * @return the "OK" button text
     */
    String ok();

    /**
     * Returns the text that appears on the button for {@link PredefinedButton#YES}.
     * 
     * @return the "Yes" button text
     */
    String yes();

  }

  /**
   * The predefined buttons supported by this dialog window.
   */
  public enum PredefinedButton {
    /**
     * An "OK" button
     */
    OK,
    /**
     * A "Cancel" button
     */
    CANCEL,
    /**
     * A "Close" button
     */
    CLOSE,
    /**
     * A "Yes button
     */
    YES,
    /**
     * A "No" button
     */
    NO
  }

  protected class DefaultDialogMessages implements DialogMessages {

    public String cancel() {
      return DefaultMessages.getMessages().messageBox_cancel();
    }

    public String close() {
      return DefaultMessages.getMessages().messageBox_close();
    }

    public String no() {
      return DefaultMessages.getMessages().messageBox_no();
    }

    public String ok() {
      return DefaultMessages.getMessages().messageBox_ok();
    }

    public String yes() {
      return DefaultMessages.getMessages().messageBox_yes();
    }

  }

  private boolean hideOnButtonClick = false;

  private List<PredefinedButton> buttons = new ArrayList<PredefinedButton>();
  private SelectHandler handler = new SelectHandler() {

    @Override
    public void onSelect(SelectEvent event) {
      onButtonPressed((TextButton) event.getSource());
    }
  };

  private DialogMessages dialogMessages;

  private TextButton hideButton;

  /**
   * Creates a dialog window with default appearance.
   */
  public Dialog() {
    setPredefinedButtons(PredefinedButton.OK);
  }

  /**
   * Creates a dialog window with the specified appearance.
   * 
   * @param appearance the dialog window appearance
   */
  public Dialog(WindowAppearance appearance) {
    super(appearance);
    setPredefinedButtons(PredefinedButton.OK);
  }

  @Override
  public HandlerRegistration addDialogHideHandler(DialogHideHandler handler) {
    return addHandler(handler, DialogHideEvent.getType());
  }

  /**
   * Returns the text button associated with the specified predefined button.
   * 
   * @param predefinedButton the predefined button
   * @return the text button associated with the predefined button, or null if the predefined button has not been added
   *         to the dialog box (see {@link #setPredefinedButtons(PredefinedButton...)}).
   */
  public TextButton getButton(PredefinedButton predefinedButton) {
    return (TextButton) buttonBar.getItemByItemId(predefinedButton.name());
  }

  /**
   * Returns the translatable strings (e.g. button text and ToolTips) for the dialog window.
   * 
   * @return the translatable strings for the dialog window
   */
  public DialogMessages getDialogMessages() {
    if (dialogMessages == null) {
      dialogMessages = new DefaultDialogMessages();
    }
    return dialogMessages;
  }

  /**
   * Returns the predefined button associated with the specified text button or null if no predefined button is
   * associated with the text button.
   * 
   * @param textButton the text button to look up
   * @return the associated predefined button
   */
  public PredefinedButton getPredefinedButton(TextButton textButton) {
    PredefinedButton predefinedButton;
    try {
      predefinedButton = PredefinedButton.valueOf(textButton.getItemId());
    } catch (IllegalArgumentException e) {
      predefinedButton = null;
    }
    return predefinedButton;
  }

  /**
   * Returns the buttons that are currently configured for this dialog window.
   * 
   * @return the buttons the buttons
   */
  public List<PredefinedButton> getPredefinedButtons() {
    return Collections.unmodifiableList(buttons);
  }

  @Override
  public void hide() {
    boolean hidePossible = !hidden;
    super.hide();
    if (hidePossible && hidden) {
      // hideButton is null when invoked under program control or via Close ToolButton
      PredefinedButton predefinedButton = hideButton == null ? null : getPredefinedButton(hideButton);
      fireEvent(new DialogHideEvent(predefinedButton));
    }
  }

  /**
   * Returns true if the dialog will be hidden on any button click.
   * 
   * @return the hide on button click state
   */
  public boolean isHideOnButtonClick() {
    return hideOnButtonClick;
  }

  /**
   * Sets the translatable strings (e.g. button text and ToolTips) for the dialog window.
   * 
   * @param dialogMessages the translatable strings
   */
  public void setDialogMessages(DialogMessages dialogMessages) {
    this.dialogMessages = dialogMessages;
  }

  /**
   * True to hide the dialog on any button click.
   * 
   * @param hideOnButtonClick true to hide
   */
  public void setHideOnButtonClick(boolean hideOnButtonClick) {
    this.hideOnButtonClick = hideOnButtonClick;
  }

  /**
   * Sets the predefined buttons to display (defaults to OK). Can be any combination of:
   * 
   * <pre>
   * {@link PredefinedButton#OK}
   * {@link PredefinedButton#CANCEL}
   * {@link PredefinedButton#CLOSE}
   * {@link PredefinedButton#YES}
   * {@link PredefinedButton#NO}
   * </pre>
   * 
   * @param buttons the buttons to display
   */
  public void setPredefinedButtons(PredefinedButton... buttons) {
    this.buttons.clear();
    for (PredefinedButton b : buttons) {
      this.buttons.add(b);
    }

    createButtons();
  }

  /**
   * Creates the buttons based on button creation constant
   */
  protected void createButtons() {
    Widget focusWidget = getFocusWidget();

    boolean focus = focusWidget == null || (focusWidget != null && getButtonBar().getWidgetIndex(focusWidget) != -1);

    getButtonBar().clear();

    for (int i = 0; i < buttons.size(); i++) {
      PredefinedButton b = buttons.get(i);
      TextButton tb = new TextButton(getText(b));
      tb.setItemId(b.name());
      tb.addSelectHandler(handler);
      if (i == 0 && focus) {
        setFocusWidget(tb);
      }
      addButton(tb);
    }
  }

  protected String getText(PredefinedButton button) {
    switch (button) {
      case OK:
        return getDialogMessages().ok();
      case CANCEL:
        return getDialogMessages().cancel();
      case CLOSE:
        return getDialogMessages().close();
      case YES:
        return getDialogMessages().yes();
      case NO:
        return getDialogMessages().no();
      default:
        // Should never happen
        throw new IllegalArgumentException("No text available for this button");
    }
  }

  /**
   * Called after a button in the button bar is selected. If {@link #setHideOnButtonClick(boolean)} is true, hides the
   * dialog when any button is pressed.
   * 
   * @param textButton the button
   */
  protected void onButtonPressed(TextButton textButton) {
    if (textButton == getButton(PredefinedButton.CLOSE) || hideOnButtonClick) {
      hideButton = textButton;
      hide();
      hideButton = null;
    }
  }

}
