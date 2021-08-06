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
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.shared.DateTimeFormatInfo;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.dom.CompositeElement;
import com.sencha.gxt.core.client.dom.CompositeFunction;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent.AfterAnimateHandler;
import com.sencha.gxt.fx.client.animation.Fx;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.XEvent;

/**
 * A date picker that displays a calendar for the specified month and provides
 * the user the ability to select the month, year and day.
 */
public class DatePicker extends Component implements HasValue<Date> {

  public enum DateState {
    ACTIVE, TODAY, SELECTED, DISABLED, OVER, PREVIOUS, NEXT
  }

  /**
   * The appearance of the date picker.
   */
  public interface DatePickerAppearance {

    String dateSelector();

    /**
     * Returns the selector that identifies the element representing a day.
     *
     * @return the day selector
     */
    String daySelector();

    NodeList<Element> getDateCells(XElement parent);

    boolean isDisabled(Element cell);

    /**
     * Returns the selector that identifies the element representing the left
     * month arrow.
     *
     * @return the left month arrow selector
     */
    String leftMonthSelector();

    /**
     * Returns the selector that identifies the element representing the left
     * year arrow (in the month / year picker).
     *
     * @return the left year arrow selector
     */
    String leftYearSelector();

    /**
     * Returns the selector that identifies the element representing the month
     * button (for displaying the month / year picker).
     *
     * @return the month button selector
     */
    String monthButtonSelector();

    /**
     * Returns the selector that identifies the element representing the month
     * picker cancel button (in the month / year picker).
     *
     * @return the month pick cancel selector
     */
    String monthPickerCancelSelector();

    /**
     * Returns the selector that identifies the elements representing the months
     * (in the month / year picker).
     *
     * @return the month picker month selector
     */
    String monthPickerMonthSelector();

    /**
     * Returns the selector that identifies the element representing the OK
     * button (in the month / year picker).
     *
     * @return the month picker OK selector
     */
    String monthPickerOkSelector();

    /**
     * Returns the selector that identifies the element representing the year
     * (in the month / year picker).
     *
     * @return the month picker year year selector
     */
    String monthPickerYearSelector();

    void onMonthButtonHtmlChange(XElement parent, SafeHtml html);

    void onMonthPickerSize(XElement monthPicker, int width, int height);

    void onMonthSelected(Element cell, boolean select);

    void onHtmlChange(Element cell, SafeHtml html);

    void onUpdateDateStyle(Element cell, DateState state, boolean add);

    void onUpdateDayOfWeeks(XElement parent, List<SafeHtml> days);

    /**
     * Renders the appearance of a date picker as HTML into a
     * {@link SafeHtmlBuilder}, suitable for passing to
     * {@link Element#setInnerSafeHtml(SafeHtml)} on a container element.
     *
     * @param sb receives the rendered appearance
     */
    void render(SafeHtmlBuilder sb);

    /**
     * Renders the appearance of a month / year picker as HTML into a
     * {@link SafeHtmlBuilder}, suitable for passing to
     * {@link Element#setInnerSafeHtml(SafeHtml)} on a container element.
     *
     * @param sb         receives the rendered appearance
     * @param messages   the translatable messages (e.g. for ToolTips)
     * @param monthNames the month names
     */
    void renderMonthPicker(SafeHtmlBuilder sb, DatePickerMessages messages, String[] monthNames);

    /**
     * Returns the selector that identifies the element representing the right
     * month arrow.
     *
     * @return the right month arrow selector
     */
    String rightMonthSelector();

    /**
     * Returns the selector that identifies the element representing the right
     * year arrow (in the month / year picker).
     *
     * @return the right year arrow selector
     */
    String rightYearSelector();

    /**
     * Returns the selector that identifies the element representing the today
     * button.
     *
     * @return the today button selector
     */
    String todayButtonSelector();

  }

  /**
   * The translatable strings (e.g. button text and ToolTips) for date picker.
   */
  public static interface DatePickerMessages {

    /**
     * Returns the "Cancel" button text.
     *
     * @return the "Cancel" button text
     */
    String cancelText();

    /**
     * Returns the advisory title of elements after the maximum date.
     *
     * @return the advisory title of elements after the maximum date.
     */
    String maxText();

    /**
     * Returns the advisory title of elements before the maximum date.
     *
     * @return the advisory title of elements before the maximum date.
     */
    String minText();

    /**
     * Returns the advisory title of the month / year element.
     *
     * @return the advisory title of the month / year element
     */
    String monthYearText();

    /**
     * Returns the advisory title of the next month element.
     *
     * @return the advisory title of the next month element.
     */
    String nextText();

    /**
     * Returns the "OK" button text.
     *
     * @return the "OK" button text
     */
    String okText();

    /**
     * Returns the advisory title of the previous month element.
     *
     * @return the advisory title of the previous month element.
     */
    String prevText();

    /**
     * Returns the "Today" button text.
     *
     * @return the "Today" button text
     */
    String todayText();

    /**
     * Returns the ToolTip to use with the Today button.
     *
     * @param date formatted representation of today's date
     * @return the ToolTip for the Today button
     */
    String todayTip(String date);
  }

  /**
   * The translatable strings (e.g. button text and ToolTips) for date picker.
   */
  public class DatePickerDefaultMessages implements DatePickerMessages {

    @Override
    public String cancelText() {
      return DefaultMessages.getMessages().datePicker_cancelText();
    }

    @Override
    public String maxText() {
      return DefaultMessages.getMessages().datePicker_maxText();
    }

    @Override
    public String minText() {
      return DefaultMessages.getMessages().datePicker_minText();
    }

    @Override
    public String monthYearText() {
      return DefaultMessages.getMessages().datePicker_monthYearText();
    }

    @Override
    public String nextText() {
      return DefaultMessages.getMessages().datePicker_nextText();
    }

    @Override
    public String okText() {
      return DefaultMessages.getMessages().datePicker_okText();
    }

    @Override
    public String prevText() {
      return DefaultMessages.getMessages().datePicker_prevText();
    }

    @Override
    public String todayText() {
      return DefaultMessages.getMessages().datePicker_todayText();
    }

    @Override
    public String todayTip(String date) {
      return DefaultMessages.getMessages().datePicker_todayTip(date);
    }

  }

  /**
   * Provides the date time info constants the date picker. Default values use the GWT {@link DateTimeFormat} returned
   * by {@code LocaleInfo.getCurrentLocale().getDateTimeFormatInfo()}. Modifications to the default values will only be
   * applied when {@link #setDateTimeInfo(com.sencha.gxt.widget.core.client.DatePicker.DatePickerDateTimeFormatInfo)} is
   * called.
   */
  public static class DatePickerDateTimeFormatInfo {

    private int firstDayOfTheWeek;
    private String[] weekdaysNarrow;
    private String[] monthsShort;
    private String[] monthsFullStandalone;

    public DatePickerDateTimeFormatInfo() {
      DateTimeFormatInfo info = LocaleInfo.getCurrentLocale().getDateTimeFormatInfo();
      firstDayOfTheWeek = info.firstDayOfTheWeek();
      weekdaysNarrow = info.weekdaysNarrow();
      monthsShort = info.monthsShort();
      monthsFullStandalone = info.monthsFullStandalone();
    }

    public int getFirstDayOfTheWeek() {
      return firstDayOfTheWeek;
    }

    public void setFirstDayOfTheWeek(int firstDayOfTheWeek) {
      this.firstDayOfTheWeek = firstDayOfTheWeek;
    }

    public String[] getWeekdaysNarrow() {
      return weekdaysNarrow;
    }

    public void setWeekdaysNarrow(String[] weekdaysNarrow) {
      this.weekdaysNarrow = weekdaysNarrow;
    }

    public String[] getMonthsShort() {
      return monthsShort;
    }

    public void setMonthsShort(String[] monthsShort) {
      this.monthsShort = monthsShort;
    }

    public String[] getMonthsFullStandalone() {
      return monthsFullStandalone;
    }

    public void setMonthsFullStandalone(String[] monthsFullStandalone) {
      this.monthsFullStandalone = monthsFullStandalone;
    }
  }

  private final DatePickerAppearance appearance;
  protected TextButton todayBtn;
  protected TextButton monthPickerOkButton;
  protected TextButton monthPickerCancelButton;
  protected DateWrapper activeDate, value;
  protected long today;
  protected Date maxDate, minDate;
  protected Element[] cells;
  protected int startDay = Integer.MIN_VALUE;
  protected XElement monthPicker;
  protected CompositeElement mpMonths, mpYears;
  protected int mpSelMonth, mpSelYear;
  protected int mpyear;
  protected DatePickerMessages messages;
  protected XElement overElement;
  protected DatePickerDateTimeFormatInfo dateTimeInfo = GWT.create(DatePickerDateTimeFormatInfo.class);

  /**
   * Creates a date picker with the default appearance.
   */
  public DatePicker() {
    this(GWT.<DatePickerAppearance>create(DatePickerAppearance.class));
  }

  /**
   * Creates a date picker with the specified appearance.
   *
   * @param appearance the appearance of the date picker
   */
  public DatePicker(DatePickerAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    this.appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));

    setAllowTextSelection(false);

    setStartDay(dateTimeInfo.getFirstDayOfTheWeek());

    todayBtn = new TextButton(getMessages().todayText());
    todayBtn.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        selectToday();
      }
    });

    todayBtn.setToolTip(getMessages().todayTip(DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT).format(new Date())));

    getElement().selectNode(appearance.todayButtonSelector()).appendChild(todayBtn.getElement());

    monthPickerOkButton = new TextButton(getMessages().okText());
    monthPickerOkButton.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        DateWrapper d = new DateWrapper(mpSelYear, mpSelMonth, 1);
        update(d);
        focus();
        hideMonthPicker();
      }
    });

    monthPickerCancelButton = new TextButton(getMessages().cancelText());
    monthPickerCancelButton.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        focus();
        hideMonthPicker();
      }
    });

    monthPicker = XElement.createElement("div");
    monthPicker.getStyle().setDisplay(Display.NONE);

    getElement().appendChild(monthPicker);

    cells = Util.toElementArray(appearance.getDateCells(getElement()));

    activeDate = value != null ? value : new DateWrapper();
    update(activeDate);

    new KeyNav(this) {
      @Override
      public void onKeyPress(NativeEvent evt) {
        handlerKeyPress(evt);
      }
    };

    getElement().makePositionable();
    getElement().setTabIndex(0);
    getElement().setAttribute("hideFocus", "true");
    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.ONFOCUS);

    XElement target = getElement().selectNode(appearance.leftMonthSelector());
    target.setTitle(messages.prevText());
    target = getElement().selectNode(appearance.rightMonthSelector());
    target.setTitle(messages.nextText());
    target = getElement().selectNode(appearance.monthButtonSelector());
    target.setTitle(messages.monthYearText());

    addGestureRecognizer(new TapGestureRecognizer() {

      @Override
      protected void onTap(TouchData touchData) {
        super.onTap(touchData);
        DatePicker.this.onTap(touchData);
      }
    });
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  public DatePickerAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the field's maximum allowed date.
   *
   * @return the max date
   */
  public Date getMaxDate() {
    return maxDate;
  }

  /**
   * Sets the picker's maximum allowed date.
   *
   * @param maxDate the max date
   */
  public void setMaxDate(Date maxDate) {
    if (maxDate != null) {
      maxDate = new DateWrapper(maxDate).resetTime().asDate();
    }
    this.maxDate = maxDate;
    update(activeDate);
    enableTodayButton();
  }

  /**
   * Returns the date picker messages.
   *
   * @return the messages
   */
  public DatePickerMessages getMessages() {
    if (messages == null) {
      messages = new DatePickerDefaultMessages();
    }
    return messages;
  }

  /**
   * Optionally, sets the date picker messages.
   *
   * @param messages the messages
   */
  public void setMessages(DatePickerMessages messages) {
    this.messages = messages;
  }

  /**
   * Returns the date time messages used by the date picker. Changes to the returned instance (calling setters) will not be applied to the date picker until {@link #setDateTimeInfo(com.sencha.gxt.widget.core.client.DatePicker.DatePickerDateTimeFormatInfo)} is called.
   *
   * @return the date time info
   */
  public DatePickerDateTimeFormatInfo getDateTimeInfo() {
    return dateTimeInfo;
  }

  /**
   * Sets the date time info instance and updates its values. This method allows the date picker's constants to be changed at runtime.
   *
   * @param dateTimeInfo the date time info
   */
  public void setDateTimeInfo(DatePickerDateTimeFormatInfo dateTimeInfo) {
    this.dateTimeInfo = dateTimeInfo;
    setStartDay(this.dateTimeInfo.getFirstDayOfTheWeek());
  }

  /**
   * Returns the picker's minimum data.
   *
   * @return the minimum date
   */
  public Date getMinDate() {
    return minDate;
  }

  /**
   * Sets the picker's minimum allowed date.
   *
   * @param minDate the minimum date
   */
  public void setMinDate(Date minDate) {
    if (minDate != null) {
      minDate = new DateWrapper(minDate).resetTime().asDate();
    }
    this.minDate = minDate;
    update(activeDate);
    enableTodayButton();
  }

  /**
   * Returns the picker's start day.
   *
   * @return the start day
   */
  public int getStartDay() {
    return startDay;
  }

  /**
   * Sets the picker's start day index as returned by {@link DateTimeFormatInfo#weekdaysNarrow()}.
   *
   * @param startDay the start day index
   */
  public void setStartDay(int startDay) {
    this.startDay = startDay;

    String[] dn = dateTimeInfo.getWeekdaysNarrow();

    List<SafeHtml> days = new ArrayList<SafeHtml>();
    for (int i = 0; i < 7; i++) {
      days.add(SafeHtmlUtils.fromString(dn[(i + startDay) % 7]));
    }

    appearance.onUpdateDayOfWeeks(getElement(), days);

    update(activeDate);
  }

  @Override
  public Date getValue() {
    return value != null ? value.asDate() : null;
  }

  @Override
  public void setValue(Date date) {
    setValue(date, true);
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    if (!isEnabled()) {
      return;
    }
    switch (event.getTypeInt()) {
      case Event.ONCLICK:
        onClick(event);
        break;
      case Event.ONMOUSEOVER:
        onMouseOver(event);
        break;
      case Event.ONMOUSEOUT:
        onMouseOut(event);
        break;
      case Event.ONFOCUS:
        onFocus(event);
        break;
    }
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    todayBtn.enable();
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    todayBtn.disable();
  }

  @Override
  public void setValue(Date date, boolean fireEvents) {
    if (date == null) {
      this.value = null;
      update(new DateWrapper().resetTime());
    } else {
      this.value = new DateWrapper(date).resetTime();
      update(value);
    }

    if (overElement != null) {
      appearance.onUpdateDateStyle(overElement, DateState.OVER, false);
    }

    if (fireEvents) {
      ValueChangeEvent.fire(this, date);
    }
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(todayBtn);
    ComponentHelper.doAttach(monthPickerOkButton);
    ComponentHelper.doAttach(monthPickerCancelButton);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(todayBtn);
    ComponentHelper.doDetach(monthPickerOkButton);
    ComponentHelper.doDetach(monthPickerCancelButton);
  }

  protected int getCalculatedStartDay() {
    return startDay != Integer.MIN_VALUE ? startDay : dateTimeInfo.getFirstDayOfTheWeek();
  }

  protected void handlerKeyPress(NativeEvent evt) {
    if (!isEnabled()) {
      return;
    }
    switch (evt.getKeyCode()) {
      case KeyCodes.KEY_DOWN:
        onKeyDown(evt);
        break;
      case KeyCodes.KEY_END:
        onKeyEnd(evt);
        break;
      case KeyCodes.KEY_ENTER:
        onKeyEnter(evt);
        break;
      case KeyCodes.KEY_HOME:
        onKeyHome(evt);
        break;
      case KeyCodes.KEY_LEFT:
        onKeyLeft(evt);
        break;
      case KeyCodes.KEY_PAGEDOWN:
        onKeyPageDown(evt);
        break;

      case KeyCodes.KEY_PAGEUP:
        onKeyPageUp(evt);
        break;

      case KeyCodes.KEY_RIGHT:
        onKeyRight(evt);
        break;

      case KeyCodes.KEY_UP:
        onKeyUp(evt);
        break;
    }

    if (evt.getKeyCode() == 32) {
      EventTarget eventTarget = evt.getEventTarget();
      if (!(Element.is(eventTarget) && todayBtn.getElement().isOrHasChild(Element.as(eventTarget)))) {
        selectToday();
      }
    }
  }

  protected void hideMonthPicker() {
    Fx fx = new Fx();
    fx.addAfterAnimateHandler(new AfterAnimateHandler() {
      @Override
      public void onAfterAnimate(AfterAnimateEvent event) {
        monthPicker.setVisible(false);
      }
    });
    monthPicker.<FxElement>cast().slideOut(Direction.UP, fx);
  }

  protected void onClick(Event event) {
    event.preventDefault();
    XElement target = event.getEventTarget().cast();
    XElement pn = null;

    if ((pn = target.findParent(appearance.daySelector(), 2)) != null) {
      event.preventDefault();
      onDayClick(pn);
    } else if ((pn = target.findParent(appearance.leftMonthSelector(), 1)) != null) {
      showPrevMonth();
    } else if ((pn = target.findParent(appearance.rightMonthSelector(), 1)) != null) {
      showNextMonth();
    } else if ((pn = target.findParent(appearance.monthButtonSelector(), 5)) != null) {
      showMonthPicker();
    }

    if ((pn = target.findParent(appearance.monthPickerMonthSelector(), 2)) != null) {
      for (int i = 0; i < mpMonths.getCount(); i++) {
        appearance.onMonthSelected(mpMonths.getElement(i), false);
      }
      appearance.onMonthSelected(pn, true);
      mpSelMonth = pn.getPropertyInt("xmonth");
    } else if ((pn = target.findParent(appearance.monthPickerYearSelector(), 2)) != null) {
      for (int i = 0; i < mpYears.getCount(); i++) {
        appearance.onMonthSelected(mpYears.getElement(i), false);
      }
      appearance.onMonthSelected(pn, true);
      mpSelYear = pn.getPropertyInt("xyear");
    } else if (target.is(appearance.leftYearSelector())) {
      updateMPYear(mpyear - 10);
    } else if (target.is(appearance.rightYearSelector())) {
      updateMPYear(mpyear + 10);
    }

    if (GXT.isSafari()) {
      focus();
    }
  }

  protected void onDayClick(XElement e) {
    if (e != null) {
      String dt = e.getPropertyString("dateValue");
      if (dt != null) {
        handleDateClick(e, dt);
        return;
      }
    }
  }

  protected void onKeyDown(NativeEvent evt) {
    if (evt.<XEvent>cast().getCtrlOrMetaKey()) {
      showPreviousYear();
    } else {
      setValue(activeDate.addDays(7).asDate(), false);
    }
  }

  protected void onKeyEnd(NativeEvent evt) {
    if (evt.getShiftKey()) {
      setValue(new DateWrapper(activeDate.getFullYear(), 11, 31).asDate());
    } else {
      setValue(activeDate.getLastDateOfMonth().asDate(), false);
    }
  }

  protected void onKeyEnter(NativeEvent evt) {
    evt.preventDefault();
    evt.stopPropagation();
    setValue(activeDate.asDate());
  }

  protected void onKeyHome(NativeEvent evt) {
    if (evt.<XEvent>cast().getCtrlOrMetaKey()) {
      setValue(new DateWrapper(activeDate.getFullYear(), 0, 1).asDate());
    } else {
      setValue(activeDate.getFirstDayOfMonth().asDate(), false);
    }
  }

  protected void onKeyLeft(NativeEvent evt) {
    XEvent e = evt.cast();
    e.stopEvent();
    if (e.getCtrlOrMetaKey()) {
      showPrevMonth();
    } else {
      setValue(activeDate.addDays(-1).asDate(), false);
    }
  }

  protected void onKeyPageDown(NativeEvent evt) {
    if (evt.getShiftKey()) {
      setValue(activeDate.addYears(1).asDate(), false);
    } else {
      setValue(activeDate.addMonths(1).asDate(), false);
    }
  }

  protected void onKeyPageUp(NativeEvent evt) {
    if (evt.getShiftKey()) {
      setValue(activeDate.addYears(-1).asDate(), false);
    } else {
      setValue(activeDate.addMonths(-1).asDate(), false);
    }
  }

  protected void onKeyRight(NativeEvent evt) {
    XEvent e = evt.cast();
    e.stopEvent();
    if (e.getCtrlOrMetaKey()) {
      showNextMonth();
    } else {
      setValue(activeDate.addDays(1).asDate(), false);
    }
  }

  protected void onKeyUp(NativeEvent evt) {
    XEvent e = evt.cast();
    e.stopEvent();
    if (e.getCtrlOrMetaKey()) {
      showNextYear();
    } else {
      setValue(activeDate.addDays(-7).asDate(), false);
    }
  }

  protected void onMouseOut(Event event) {
    XElement ce = event.getEventTarget().cast();

    XElement target = ce.findParent(appearance.dateSelector(), 3);
    if (target != null && target == overElement) {
      overElement = null;
      appearance.onUpdateDateStyle(target, DateState.OVER, false);
    }
  }

  protected void onMouseOver(Event event) {
    XElement ce = event.getEventTarget().cast();

    XElement target = ce.findParent(appearance.dateSelector(), 3);

    if (target != null && overElement != target) {
      overElement = target;
      appearance.onUpdateDateStyle(target, DateState.OVER, true);
    }
  }

  protected void onTap(TouchData touchData) {
    Event touchEvent = touchData.getLastNativeEvent().cast();
    onClick(touchEvent);
  }

  protected void showMonthPicker() {
    createMonthPicker();

    Size s = getElement().getSize(true);
    s.setHeight(s.getHeight() - 2);

    monthPicker.setTop(1);

    appearance.onMonthPickerSize(monthPicker, s.getWidth(), s.getHeight());

    mpSelMonth = (activeDate != null ? activeDate : value).getMonth();

    updateMPMonth(mpSelMonth);
    mpSelYear = (activeDate != null ? activeDate : value).getFullYear();
    updateMPYear(mpSelYear);

    monthPicker.getStyle().setDisplay(Display.BLOCK);
    monthPicker.makePositionable(true);
    monthPicker.<FxElement>cast().slideIn(Direction.DOWN);
  }

  protected void showNextMonth() {
    setValue(activeDate.addMonths(+1).asDate(), false);
  }

  protected void showNextYear() {
    setValue(activeDate.addYears(1).asDate(), false);
  }

  protected void showPreviousYear() {
    setValue(activeDate.addYears(-1).asDate(), false);
  }

  protected void showPrevMonth() {
    setValue(activeDate.addMonths(-1).asDate(), false);
  }

  protected void updateMPMonth(int month) {
    for (int i = 0; i < mpMonths.getCount(); i++) {
      Element elem = mpMonths.item(i);
      int xmonth = elem.getPropertyInt("xmonth");
      appearance.onMonthSelected(elem, xmonth == month);
    }
  }

  protected void updateMPYear(int year) {
    mpyear = year;
    for (int i = 1; i <= 10; i++) {
      XElement td = XElement.as(mpYears.item(i - 1));
      int y2;
      if (i % 2 == 0) {
        y2 = (int) (year + (Math.round(i * .5)));
      } else {
        y2 = (int) (year - (5 - Math.round(i * .5)));
      }
      td.getFirstChildElement().setInnerText("" + y2);
      td.setPropertyInt("xyear", y2);
      appearance.onMonthSelected(td, y2 == mpSelYear);
    }
  }

  protected void createMonthPicker() {
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    appearance.renderMonthPicker(builder, messages, dateTimeInfo.getMonthsShort());

    monthPicker.removeChildren();
    monthPicker.setInnerSafeHtml(builder.toSafeHtml());

    monthPicker.selectNode(appearance.monthPickerOkSelector()).appendChild(monthPickerOkButton.getElement());
    monthPicker.selectNode(appearance.monthPickerCancelSelector()).appendChild(monthPickerCancelButton.getElement());

    mpMonths = new CompositeElement(Util.toElementArray(monthPicker.select(appearance.monthPickerMonthSelector())));
    mpYears = new CompositeElement(Util.toElementArray(monthPicker.select(appearance.monthPickerYearSelector())));

    mpMonths.each(new CompositeFunction() {

      public void doFunction(Element elem, CompositeElement ce, int index) {
        index += 1;
        if (index % 2 == 0) {
          elem.setPropertyInt("xmonth", (int) (5 + (Math.round(index * .5))));
        } else {
          elem.setPropertyInt("xmonth", (int) (Math.round((index - 1) * .5)));
        }
      }

    });
  }

  @SuppressWarnings("deprecation")
  protected void enableTodayButton() {
    boolean minDateDisable = false;
    boolean maxDateDisable = false;

    if (minDate != null) {
      Date endOfToday = new Date();
      CalendarUtil.addDaysToDate(endOfToday, 1);
      endOfToday.setHours(0);
      endOfToday.setMinutes(0);
      endOfToday.setSeconds(0);
      minDateDisable = minDate.after(endOfToday);
    }

    if (maxDate != null) {
      Date endOfYesterday = new Date();
      CalendarUtil.addDaysToDate(endOfYesterday, -1);
      endOfYesterday.setHours(23);
      endOfYesterday.setMinutes(59);
      endOfYesterday.setSeconds(59);
      maxDateDisable = maxDate.before(endOfYesterday);
    }

    if (minDateDisable || maxDateDisable) {
      todayBtn.setEnabled(false);
    } else {
      todayBtn.setEnabled(true);
    }
  }

  protected void handleDateClick(XElement target, String dt) {
    String[] tokens = dt.split(",");
    int year = Integer.parseInt(tokens[0]);
    int month = Integer.parseInt(tokens[1]);
    int day = Integer.parseInt(tokens[2]);
    Date d = new DateWrapper(year, month, day).asDate();
    if (d != null && !appearance.isDisabled(target.getParentElement())) {
      setValue(d);
    }
  }

  protected void selectToday() {
    setValue(new DateWrapper().asDate());
  }

  protected void setCellStyle(Element cell, Date d, long sel, long min, long max) {
    long t = d.getTime();

    DateWrapper w = new DateWrapper(d);
    int year = w.getFullYear();
    int month = w.getMonth();
    int day = w.getDate();

    String dd = year + "," + month + "," + day;

    cell.getFirstChildElement().setPropertyString("dateValue", dd);

    appearance.onUpdateDateStyle(cell, DateState.TODAY, t == today);
    appearance.onUpdateDateStyle(cell, DateState.SELECTED, t == sel);
    appearance.onUpdateDateStyle(cell, DateState.DISABLED, t > max || t < min);

    if (t > max || t < min) {
      if (t > max) {
        cell.setTitle(messages.maxText());
      } else {
        cell.setTitle(messages.minText());
      }
    }
  }

  protected void update(DateWrapper date) {
    DateWrapper vd = activeDate;
    activeDate = date;
    if (vd != null) {
      int days = date.getDaysInMonth();
      DateWrapper firstOfMonth = date.getFirstDayOfMonth();
      int startingPos = firstOfMonth.getDayInWeek() - startDay;

      if (startingPos <= startDay) {
        startingPos += 7;
      }

      // go to previous month.
      DateWrapper pm = activeDate.addMonths(-1);
      int prevStart = pm.getDaysInMonth() - startingPos;

      days += startingPos;

      DateWrapper d = new DateWrapper(pm.getFullYear(), pm.getMonth(), prevStart).resetTime();
      today = new DateWrapper().resetTime().getTime();
      long sel = value != null ? value.resetTime().getTime() : Long.MIN_VALUE;
      long min = minDate != null ? new DateWrapper(minDate).getTime() : Long.MIN_VALUE;
      long max = maxDate != null ? new DateWrapper(maxDate).getTime() : Long.MAX_VALUE;

      int i = 0;
      for (; i < startingPos; i++) {
        appearance.onHtmlChange(cells[i], SafeHtmlUtils.fromTrustedString("" + ++prevStart));
        d = d.addDays(1);
        appearance.onUpdateDateStyle(cells[i], DateState.PREVIOUS, true);
        setCellStyle(cells[i], d.asDate(), sel, min, max);

        cells[i].setTitle(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(d.asDate()));
      }
      for (; i < days; i++) {
        int intDay = i - startingPos + 1;
        appearance.onHtmlChange(cells[i], SafeHtmlUtils.fromTrustedString("" + intDay));
        d = d.addDays(1);
        appearance.onUpdateDateStyle(cells[i], DateState.PREVIOUS, false);
        appearance.onUpdateDateStyle(cells[i], DateState.NEXT, false);
        appearance.onUpdateDateStyle(cells[i], DateState.ACTIVE, true);
        setCellStyle(cells[i], d.asDate(), sel, min, max);
        cells[i].setTitle(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(d.asDate()));
      }
      int extraDays = 0;
      for (; i < 42; i++) {
        appearance.onHtmlChange(cells[i], SafeHtmlUtils.fromTrustedString("" + ++extraDays));
        d = d.addDays(1);
        appearance.onUpdateDateStyle(cells[i], DateState.NEXT, true);
        setCellStyle(cells[i], d.asDate(), sel, min, max);
        cells[i].setTitle(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(d.asDate()));
      }
      int month = activeDate.getMonth();

      SafeHtml t = SafeHtmlUtils.fromString(dateTimeInfo.getMonthsFullStandalone()[month] + " "
          + activeDate.getFullYear());

      appearance.onMonthButtonHtmlChange(getElement(), t);
    }

  }

}
