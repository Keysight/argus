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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.CompositeElement;
import com.sencha.gxt.core.client.dom.DomHelper;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.LongPressOrTapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.PointerEventsSupport;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer.ScrollDirection;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent.LoadExceptionHandler;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.widget.core.client.cell.DefaultHandlerManagerContext;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.RefreshEvent.HasRefreshHandlers;
import com.sencha.gxt.widget.core.client.event.RefreshEvent.RefreshHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

/**
 * A {@link ListView} provides support for displaying a list of data. The list
 * view gets its data from a {@link ListStore}. Each model in the store is
 * rendered as an item in the list view. Any updates to the store are
 * automatically pushed to the list view.
 * <p/>
 * In GXT version 3, {@link ModelKeyProvider}s and {@link ValueProvider}s
 * provide the interface between your data model, the list store and the list
 * view. This enables a list view to work with data of any object type. The list
 * view uses a value provider, passed to the constructor, to get the value to
 * display for each model in the list view.
 * <p/>
 * You can provide your own implementation of these interfaces, or you can use a
 * Sencha supplied generator to create them for you automatically. A generator
 * runs at compile time to create a Java class that is compiled to JavaScript.
 * The Sencha supplied generator can create classes for interfaces that extend
 * the {@link PropertyAccess} interface. The generator transparently creates the
 * class at compile time and the {@link GWT#create(Class)} method returns an
 * instance of that class at run time. The generated class is managed by GWT and
 * GXT and you generally do not need to worry about what the class is called,
 * where it is located, or other similar details.
 * <p/>
 * To customize the appearance of the item in the list view, provide a cell
 * implementation using {@link ListView#setCell(Cell)}.
 * <p/>
 * The following code snippet illustrates the creation of a simple list view
 * with local data for test purposes. For more practical examples that show how
 * to customize list views to display images or use other types of cells, see
 * the ListView, Advanced ListView and DateCell ListView examples in the online
 * Explorer demo.
 * </p>
 * <p/>
 * <pre>
 *   // Generate the key provider and value provider for the Data class
 *   DataProperties dp = GWT.create(DataProperties.class);
 *
 *   // Create the store that the contains the data to display in the grid
 *   ListStore<Data> s = new ListStore<Test.Data>(dp.key());
 *   s.add(new Data("name1", "value1"));
 *   s.add(new Data("name2", "value2"));
 *   s.add(new Data("name3", "value3"));
 *   s.add(new Data("name4", "value4"));
 *
 *   // Create the tree using the store and value provider for the name field
 *   ListView<Data, String> t = new ListView<Data, String>(s, dp.name());
 *
 *   // Add the tree to a container
 *   RootPanel.get().add(t);
 * </pre>
 * <p/>
 * To use the Sencha supplied generator to create model key providers and value
 * providers, extend the <code>PropertyAccess</code> interface, parameterized
 * with the type of data you want to access (as shown below) and invoke the
 * <code>GWT.create</code> method on its <code>class</code> member (as shown in
 * the code snippet above). This creates an instance of the class that can be
 * used to initialize the list view and list store. In the following code
 * snippet we define a new interface called <code>DataProperties</code> that
 * extends the <code>PropertyAccess</code> interface and is parameterized with
 * <code>Data</code>, a Plain Old Java Object (POJO).
 * <p/>
 * <pre>
 * public interface DataProperties extends PropertyAccess<Data> {
 *   &#64;Path("name")
 *   ModelKeyProvider<Data> key();
 *   ValueProvider&lt;Data, String> name();
 *   ValueProvider&lt;Data, String> value();
 * }
 *
 * public class Data {
 *   private String name;
 *   private String value;
 *
 *   public Data(String name, String value) {
 *     super();
 *     this.name = name;
 *     this.value = value;
 *   }
 *   public String getName() {
 *     return name;
 *   }
 *   public String getValue() {
 *     return value;
 *   }
 *   public void setName(String name) {
 *     this.name = name;
 *   }
 *   public void setValue(String value) {
 *     this.value = value;
 *   }
 * }
 * </pre>
 *
 * @param <M> the model type
 * @param <N> the cell data type
 */
public class ListView<M, N> extends Component implements HasRefreshHandlers {

  /**
   * The appearance of a list view.
   *
   * @param <M> the model type
   */
  public interface ListViewAppearance<M> {

    /**
     * Returns the cell's parent element if it exists. Default implementation
     * returns the cell's root element.
     *
     * @param item the cell whose parent is to be returned
     * @return the root from which other elements can be found
     */
    Element findCellParent(XElement item);

    /**
     * Returns the matching element.
     *
     * @param child the element or any child element
     * @return the parent element
     */
    Element findElement(XElement child);

    /**
     * Returns the child elements.
     *
     * @param parent the parent element
     * @return the child elements
     */
    List<Element> findElements(XElement parent);

    /**
     * Handles a cursor over event.
     *
     * @param item the item affected by the cursor
     * @param over true if the cursor is over the item
     */
    void onOver(XElement item, boolean over);

    /**
     * Handles a select event.
     *
     * @param item the item affected by the select
     * @param select true if the item is selected
     */
    void onSelect(XElement item, boolean select);

    /**
     * Renders the container.
     *
     * @param builder the builder
     */
    void render(SafeHtmlBuilder builder);

    /**
     * Optionally renders extra markup at the end of the the list.
     *
     * @param builder the builder
     */
    void renderEnd(SafeHtmlBuilder builder);

    /**
     * Renders a single item.
     *
     * @param builder the builder
     * @param content the item content
     */
    void renderItem(SafeHtmlBuilder builder, SafeHtml content);

  }


  private static final Logger logger = Logger.getLogger(ListView.class.getName());

  private final ListViewAppearance<M> appearance;
  protected XElement focusEl;
  protected final FocusImpl focusImpl = FocusImpl.getFocusImplForPanel();

  protected int rowSelectorDepth = 5;
  protected ListStore<M> store;

  final protected ValueProvider<? super M, N> valueProvider;

  private CompositeElement all;

  private Cell<N> cell;

  private boolean enableQuickTip = true;
  private HandlerRegistration storeHandlersRegistration;
  private GroupingHandlerRegistration loadHandlerRegistration;
  private SafeHtml loadingIndicator = SafeHtmlUtils.EMPTY_SAFE_HTML;

  private XElement overElement;
  private QuickTip quickTip;
  private boolean selectOnHover;
  private ListViewSelectionModel<M> sm;
  private StoreHandlers<M> storeHandlers;
  // by default, disable row highlighting on touch devices - there's some difficulties with touch events between taps
  // since we lumped MSEdge into touch devices, we need to make sure to leave row highlighting for MSEdge
  private boolean trackMouseOver = GXT.isMSEdge() || !GXT.isTouch();

  /**
   * Creates a new list view.
   *
   * @param store the store
   * @param valueProvider the value provider
   */
  public ListView(ListStore<M> store, ValueProvider<? super M, N> valueProvider) {
    this(store, valueProvider, GWT.<ListViewAppearance<M>>create(ListViewAppearance.class));
  }

  /**
   * Creates a new list view.
   *
   * @param store the store
   * @param valueProvider the value provider
   * @param cell the cell
   */
  public ListView(ListStore<M> store, ValueProvider<? super M, N> valueProvider, Cell<N> cell) {
    this(store, valueProvider, GWT.<ListViewAppearance<M>>create(ListViewAppearance.class));
    setCell(cell);
  }

  /**
   * Creates a new list view.
   *
   * @param store the store
   * @param valueProvider the value provider
   * @param appearance the appearance
   */
  public ListView(final ListStore<M> store, ValueProvider<? super M, N> valueProvider, ListViewAppearance<M> appearance) {
    this.appearance = appearance;
    this.valueProvider = valueProvider;
    setSelectionModel(new ListViewSelectionModel<M>());

    SafeHtmlBuilder markupBuilder = new SafeHtmlBuilder();
    appearance.render(markupBuilder);

    setElement((Element) XDOM.create(markupBuilder.toSafeHtml()));

    all = new CompositeElement();
    setAllowTextSelection(false);

    storeHandlers = new StoreHandlers<M>() {

      @Override
      public void onAdd(StoreAddEvent<M> event) {
        ListView.this.onAdd(event.getItems(), event.getIndex());
      }

      @Override
      public void onClear(StoreClearEvent<M> event) {
        refresh();
      }

      @Override
      public void onDataChange(StoreDataChangeEvent<M> event) {
        refresh();
      }

      @Override
      public void onFilter(StoreFilterEvent<M> event) {
        refresh();
      }

      @Override
      public void onRecordChange(StoreRecordChangeEvent<M> event) {
        if (ListView.this.valueProvider == event.getProperty()) {
          ListView.this.onUpdate(event.getRecord().getModel(), store.indexOf(event.getRecord().getModel()));
        }
      }

      @Override
      public void onRemove(StoreRemoveEvent<M> event) {
        ListView.this.onRemove(event.getItem(), event.getIndex());
      }

      @Override
      public void onSort(StoreSortEvent<M> event) {
        refresh();
      }

      @Override
      public void onUpdate(StoreUpdateEvent<M> event) {
        List<M> items = event.getItems();
        for (int i = 0; i < items.size(); i++) {
          M item = items.get(i);
          ListView.this.onUpdate(item, store.indexOf(item));
        }
      }
    };

    setStore(store);

    ensureFocusElement();

    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS | Event.ONKEYDOWN);
    addGestureRecognizer(new LongPressOrTapGestureRecognizer() {
      @Override
      protected void onLongPress(TouchData touch) {
        ListView.this.onLongPress(touch);
        super.onLongPress(touch);
      }

      @Override
      protected void onTap(TouchData touch) {
        ListView.this.onTap(touch);
        super.onTap(touch);
      }

      @Override
      protected void handlePreventDefault(NativeEvent event) {
        // preventDefault on everything except input elements
        if (sm != null && sm.isInput(event.getEventTarget().<Element>cast())) {
          return;
        }
        event.preventDefault();
      }
    });
    addGestureRecognizer(new ScrollGestureRecognizer(getElement(), ScrollDirection.BOTH));
  }

  @Override
  public HandlerRegistration addRefreshHandler(RefreshHandler handler) {
    return addHandler(handler, RefreshEvent.getType());
  }

  /**
   * Returns the matching element.
   *
   * @param element the element or any child element
   * @return the parent element
   */
  public Element findElement(Element element) {
    return appearance.findElement(element.<XElement>cast());
  }

  /**
   * Returns the element's index.
   *
   * @param element the element or any child element
   * @return the element index or -1 if no match
   */
  public int findElementIndex(Element element) {
    Element elem = findElement(element);
    if (elem != null) {
      return indexOf(elem);
    }
    return -1;
  }

  @Override
  public void focus() {
    if (GXT.isGecko()) {
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
        public void execute() {
          if (ListView.this.focusEl != null) {
            ListView.this.focusImpl.focus(ListView.this.focusEl);
          }
        }
      });
    } else if (focusEl != null) {
      focusImpl.focus(focusEl);
    }
  }

  public ListViewAppearance<M> getAppearance() {
    return appearance;
  }

  /**
   * Returns the view's cell.
   *
   * @return the cell, or null if none specified
   */
  public Cell<N> getCell() {
    return cell;
  }

  /**
   * Returns the element at the given index.
   *
   * @param index the index
   * @return the element
   */
  public XElement getElement(int index) {
    return all.getElement(index).<XElement>cast();
  }

  /**
   * Returns all of the child elements.
   *
   * @return the elements
   */
  public List<Element> getElements() {
    return all.getElements();
  }

  /**
   * Returns the number of models in the view.
   *
   * @return the count
   */
  public int getItemCount() {
    return store == null ? 0 : store.size();
  }

  /**
   * Returns the view's loading indicator as html.
   *
   * @return the loading indicator html
   */
  public SafeHtml getLoadingIndicator() {
    return loadingIndicator;
  }

  /**
   * Returns the view's quick tip instance.
   *
   * @return the quicktip instance or null if not enabled
   */
  public QuickTip getQuickTip() {
    return quickTip;
  }

  /**
   * Returns the view's selection model.
   *
   * @return the selection model
   */
  public ListViewSelectionModel<M> getSelectionModel() {
    return sm;
  }

  /**
   * Returns true if select on hover is enabled.
   *
   * @return the select on hover state
   */
  public boolean getSelectOnOver() {
    return selectOnHover;
  }

  /**
   * Returns the combo's store.
   *
   * @return the store
   */
  public ListStore<M> getStore() {
    return store;
  }

  /**
   * Returns the index of the element.
   *
   * @param element the element
   * @return the index
   */
  public int indexOf(Element element) {
    if (element == null) {
      return -1;
    }
    if (element.getPropertyString("viewIndex") != null) {
      return Util.parseInt(element.getPropertyString("viewIndex"), -1);
    }
    return all.indexOf(element);
  }

  /**
   * Returns true if quicktips are enabled.
   *
   * @return true for enabled
   */
  public boolean isEnableQuickTips() {
    return enableQuickTip;
  }

  /**
   * Returns true if items are highlighted on mouse over.
   *
   * @return the track mouse state
   */
  public boolean isTrackMouseOver() {
    return trackMouseOver;
  }

  /**
   * Moves the current selections down one level.
   */
  public void moveSelectedDown() {
    List<M> sel = getSelectionModel().getSelectedItems();

    Collections.sort(sel, new Comparator<M>() {
      public int compare(M o1, M o2) {
        return store.indexOf(o1) < store.indexOf(o2) ? 1 : 0;
      }
    });

    for (M m : sel) {
      int idx = store.indexOf(m);
      if (idx < (store.size() - 1)) {
        store.remove(m);
        store.add(idx + 1, m);
      }
    }
    getSelectionModel().select(sel, false);
  }

  /**
   * Moves the current selections up one level.
   */
  public void moveSelectedUp() {
    List<M> sel = getSelectionModel().getSelectedItems();

    Collections.sort(sel, new Comparator<M>() {
      public int compare(M o1, M o2) {
        return store.indexOf(o1) > store.indexOf(o2) ? 1 : 0;
      }
    });

    for (M m : sel) {
      int idx = store.indexOf(m);
      if (idx > 0) {
        store.remove(m);
        store.add(idx - 1, m);
      }
    }
    getSelectionModel().select(sel, false);
  }

  @Override
  public void onBrowserEvent(Event event) {
    if (!isEnabled()) {
      return;
    }
    if (cell != null) {
      CellWidgetImplHelper.onBrowserEvent(this, event);
    }

    handleEventForCell(event);

    super.onBrowserEvent(event);

    switch (event.getTypeInt()) {
      case Event.ONMOUSEOVER:
        onMouseOver(event);
        break;
      case Event.ONMOUSEOUT:
        onMouseOut(event);
        break;
      case Event.ONMOUSEDOWN:
        // we don't want mousedown happening while scrolling - super.onBrowserEvent pipes the events through the gesture
        // recognizers, all we need to do is return.
        if (!PointerEventsSupport.impl.isSupported()) {
          onMouseDown(event);
        }
        break;
    }
  }

  /**
   * Refreshes the view by reloading the data from the store and re-rendering
   * the template.
   */
  public void refresh() {
    if (!isOrWasAttached()) {
      return;
    }
    getElement().setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    getElement().repaint();

    List<M> models = store == null ? new ArrayList<M>() : store.getAll();
    all.removeAll();

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    bufferRender(models, sb);

    SafeHtml markup = sb.toSafeHtml();
    getElement().setInnerSafeHtml(markup);

    SafeHtmlBuilder esb = new SafeHtmlBuilder();
    appearance.renderEnd(esb);
    DomHelper.insertHtml("beforeend", getElement(), esb.toSafeHtml());

    List<Element> elems = appearance.findElements(getElement());

    all = new CompositeElement(elems);
    updateIndexes(0, -1);

    ensureFocusElement();

    fireEvent(new RefreshEvent());
  }

  /**
   * Refreshes an individual node's data from the store.
   *
   * @param index the items data index in the store
   */
  public void refreshNode(int index) {
    onUpdate(store.get(index), index);
  }

  @Override
  public void setAllowTextSelection(boolean enable) {
    allowTextSelection = enable;

    getElement().setClassName(CommonStyles.get().unselectableSingle(), !enable);
  }

  /**
   * Optionally sets the view's cell. If a cell is not provided, toString is
   * called on the value returned by the view's value provider.
   *
   * @param cell the cell
   */
  public void setCell(Cell<N> cell) {
    this.cell = cell;

    if (cell != null) {
      CellWidgetImplHelper.sinkEvents(this, cell.getConsumedEvents());
    }
  }

  /**
   * True to enable quicktips (defaults to true, pre-render).
   *
   * @param enableQuickTip true to enable quicktips
   */
  public void setEnableQuickTips(boolean enableQuickTip) {
    assertPreRender();
    this.enableQuickTip = enableQuickTip;
  }

  /**
   * Sets the loader. Used for interacting with before load and load exception events.
   * <p/>
   * <ul>
   * <li>Used with {@link #setLoadingIndicator(SafeHtml)} or {@link #setLoadingIndicator(String)}.</li>
   * <li>{@link #onBeforeLoad()} is called before a load and sets the loading HTML.</li>
   * <li>{@link #onLoadError(com.sencha.gxt.data.shared.loader.LoadExceptionEvent)} is called on load exception.</li>
   * </ul>
   *
   * @param loader is the list loader
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void setLoader(Loader<?, ?> loader) {
    if (loadHandlerRegistration != null) {
      loadHandlerRegistration.removeHandler();
      loadHandlerRegistration = null;
    }
    if (loader != null) {
      if (loadHandlerRegistration == null) {
        loadHandlerRegistration = new GroupingHandlerRegistration();
      }
      loadHandlerRegistration.add(loader.addBeforeLoadHandler(new BeforeLoadEvent.BeforeLoadHandler() {
        @Override
        public void onBeforeLoad(BeforeLoadEvent event) {
          ListView.this.onBeforeLoad();
        }
      }));
      loadHandlerRegistration.add(loader.addLoadExceptionHandler(new LoadExceptionHandler() {
        @Override
        public void onLoadException(LoadExceptionEvent event) {
          refresh();
          ListView.this.onLoadError(event);
        }
      }));
    }
  }

  /**
   * Sets the loading indicator html to be displayed during a load request.
   * <p/>
   * <ul>
   * <li>The {@link #setLoader(Loader)} has to be set for this use.
   * <li>The css class name 'loading-indicator' can style the loading indicator html.
   * </ul>
   *
   * @param html the loading html
   */
  public void setLoadingIndicator(SafeHtml html) {
    this.loadingIndicator = html;
  }

  /**
   * Sets the loading indicator as text to be displayed during a load request.
   * <p/>
   * <ul>
   * <li>The {@link #setLoader(Loader)} has to be set for this use.
   * <li>The css class name 'loading-indicator' can style the loading indicator text.
   * </ul>
   *
   * @param text the loading text
   */
  public void setLoadingIndicator(String text) {
    this.loadingIndicator = SafeHtmlUtils.fromString(text);
  }

  /**
   * Sets the selection model.
   *
   * @param sm the selection model
   */
  public void setSelectionModel(ListViewSelectionModel<M> sm) {
    if (this.sm != null) {
      this.sm.bindList(null);
    }
    this.sm = sm;
    if (sm != null) {
      sm.bindList(this);
    }
  }

  /**
   * True to select the item when mousing over a element (defaults to false).
   *
   * @param selectOnHover true to select on mouse over
   */
  public void setSelectOnOver(boolean selectOnHover) {
    this.selectOnHover = selectOnHover;
  }

  /**
   * Changes the data store bound to this view and refreshes it.
   *
   * @param store the store to bind this view
   */
  public void setStore(ListStore<M> store) {
    if (this.store != null) {
      storeHandlersRegistration.removeHandler();
    }
    if (store != null) {
      storeHandlersRegistration = store.addStoreHandlers(storeHandlers);
    }
    this.store = store;
    sm.bindList(this);

    if (store != null) {
      refresh();
    }
  }

  /**
   * True to highlight items when the mouse is over (defaults to true).
   *
   * @param trackMouseOver true to highlight rows on mouse over
   */
  public void setTrackMouseOver(boolean trackMouseOver) {
    this.trackMouseOver = trackMouseOver;
  }

  protected void bufferRender(List<M> models, SafeHtmlBuilder sb) {
    for (int i = 0, len = models.size(); i < len; i++) {
      M m = models.get(i);
      SafeHtmlBuilder cellBuilder = new SafeHtmlBuilder();
      N v = getValue(m);
      if (cell == null) {
        String text = null;
        if (v != null) {
          text = v.toString();
        }
        cellBuilder.append(Util.isEmptyString(text) ? Util.NBSP_SAFE_HTML : SafeHtmlUtils.fromString(text));
      } else {
        Context context = new Context(i, 0, store.getKeyProvider().getKey(m));
        cell.render(context, v, cellBuilder);
      }

      appearance.renderItem(sb, cellBuilder.toSafeHtml());
    }
  }

  protected boolean cellConsumesEventType(Cell<N> cell, String eventType) {
    Set<String> consumedEvents = cell.getConsumedEvents();
    return consumedEvents != null && consumedEvents.contains(eventType);
  }

  protected void focusItem(int index) {
    XElement elem = getElement(index);
    if (elem != null) {
      elem.scrollIntoView(getElement(), false);
      focusEl.setXY(elem.getXY());
    }
    focus();
  }

  protected N getValue(M m) {
    N value;
    if (store.hasRecord(m)) {
      value = store.getRecord(m).getValue(valueProvider);
    } else {
      value = valueProvider.getValue(m);
    }
    return value;
  }

  protected void handleEventForCell(Event event) {
    // Get the event target.
    EventTarget eventTarget = event.getEventTarget();
    if (cell == null || !Element.is(eventTarget)) {
      return;
    }
    final XElement target = event.getEventTarget().cast();

    int rowIndex = findElementIndex(appearance.findElement(target));

    final M m = getStore().get(rowIndex);
    Element cellParent = appearance.findCellParent(getElement(rowIndex));

    if (m != null && cellParent != null) {
      DefaultHandlerManagerContext context = new DefaultHandlerManagerContext(rowIndex, 0,
          getStore().getKeyProvider().getKey(m), ComponentHelper.ensureHandlers(this));

      if (cellConsumesEventType(cell, event.getType())) {
        cell.onBrowserEvent(context, cellParent, getValue(m), event, new ValueUpdater<N>() {

          @Override
          public void update(N value) {
            ListView.this.getStore().getRecord(m).addChange(valueProvider, value);
          }
        });
      }
    }
  }

  protected void onAdd(List<M> models, final int index) {
    if (!isOrWasAttached()) {
      return;
    }

    boolean empty = all.getCount() == 0;
    // add on sorted store, fires multiple adds while store has all models
    // before firing
    if (empty && models.size() == store.size()) {
      refresh();
      return;
    }
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    bufferRender(models, sb);

    Element d = Document.get().createDivElement();
    d.setInnerSafeHtml(sb.toSafeHtml());
    List<Element> list = appearance.findElements(d.<XElement>cast());

    final Element ref = index == 0 ? null : all.getElement(index - 1);
    final Element n = ref == null ? null : ref.getParentElement();

    for (int i = list.size() - 1; i >= 0; i--) {
      Element e = list.get(i);
      if (index == 0) {
        getElement().insertFirst(e);
      } else {
        Node next = ref == null ? null : ref.getNextSibling();
        if (next == null) {
          n.appendChild(e);
        } else {
          n.insertBefore(e, next);
        }
      }
    }

    all.insert(Util.toElementArray(list), index);
    updateIndexes(index, -1);
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    if (enableQuickTip) {
      quickTip = new QuickTip(this);
    }
    refresh();
  }

  /**
   * Is called before a load when a {@link #setLoader(Loader) and {@link #setLoadingIndicator(SafeHtml) or
   * {@link #setLoadingIndicator(String)} are used the list will display the loading HTML.
   * <p/>
   * <ul>
   * <li>The {@link #setLoader(Loader)} has to be set for this to be called.
   * <li>The css class name 'loading-indicator' can style the loading HTML or text.
   * </ul>
   */
  protected void onBeforeLoad() {
    if (loadingIndicator != SafeHtmlUtils.EMPTY_SAFE_HTML) {
      SafeHtmlBuilder sb = new SafeHtmlBuilder();
      sb.appendHtmlConstant("<div class='loading-indicator'>");
      sb.append(loadingIndicator);
      sb.appendHtmlConstant("</div>");
      getElement().setInnerSafeHtml(sb.toSafeHtml());
      all.removeAll();
    }
  }

  /**
   * Is called on loader exception. Override to add error handling.
   * <p/>
   * <ul>
   * <li>The {@link #setLoader(Loader)} has to be set for this to be called.
   * </ul>
   *
   * @param event is the load exception event
   */
  protected void onLoadError(LoadExceptionEvent<?> event) {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.severe("The ListView had a load exception");
    }
  }

  @Override
  protected void onFocus(Event event) {
    super.onFocus(event);
  }

  protected void onHighlightRow(int index, boolean highLight) {
    XElement e = getElement(index);
    if (e != null) {
      e.setClassName("x-view-highlightrow", highLight);
    }
  }

  protected void onLongPress(TouchData touchData) {
    // delegate to onTap
    onTap(touchData);
  }

  protected void onMouseDown(Event e) {
    int index = indexOf(e.getEventTarget().<Element>cast());
    if (index != -1) {
      fireEvent(new SelectEvent());
    }
  }

  protected void onMouseOut(Event ce) {
    if (overElement != null) {
      if (!ce.<XEvent>cast().within(overElement, true)) {
        appearance.onOver(overElement, false);
        overElement = null;
      }
    }
  }

  protected void onMouseOver(Event ce) {
    if (selectOnHover || trackMouseOver) {
      Element target = ce.getEventTarget().<Element>cast();
      target = findElement(target);
      if (target != null) {
        int index = indexOf(target);
        if (index != -1) {
          if (selectOnHover) {
            sm.select(index, false);
          } else {
            if (target != overElement) {
              appearance.onOver(target.<XElement>cast(), true);
              overElement = target.<XElement>cast();
            }
          }
        }
      }
    }
  }

  protected void onRemove(M data, int index) {
    if (all != null) {
      Element e = getElement(index);
      if (e != null) {
        appearance.onOver(e.<XElement>cast(), false);
        if (overElement == e) {
          overElement = null;
        }

        getSelectionModel().deselect(index);

        e.removeFromParent();
        all.remove(index);
        updateIndexes(index, -1);
      }
    }
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);
    constrainFocusElement();
  }

  protected void onSelectChange(M model, boolean select) {
    if (all != null) {
      int index = store.indexOf(model);
      if (index != -1 && index < all.getCount()) {
        XElement e = getElement(index);
        if (e != null) {
          appearance.onSelect(e, select);
          appearance.onOver(e.<XElement>cast(), false);
        }
      }
    }
  }

  protected void onTap(TouchData touch) {
    Event event = touch.getLastNativeEvent().cast();
    onMouseOut(event); // reset last selection

    // don't handle this touch if we don't have a selection model (because no selection needed)
    // or if the selection is locked or if the event is on an input
    if (sm == null || sm.isLocked() || sm.isInput(event.getEventTarget().<Element>cast())) {
      return;
    }
    getSelectionModel().onMouseDown(event);
    getSelectionModel().onMouseClick(event);
    onMouseOver(event);
    onMouseDown(event);
  }

  protected void onUpdate(M model, int index) {
    Element original = all.getElement(index);
    if (original != null) {
      List<M> list = Collections.singletonList(model);
      SafeHtmlBuilder sb = new SafeHtmlBuilder();
      bufferRender(list, sb);

      DomHelper.insertBefore(original, sb.toSafeHtml());

      all.replaceElement(original, Element.as(original.getPreviousSibling()));
      original.removeFromParent();

    }
    sm.onRowUpdated(model);
  }

  protected void updateIndexes(int startIndex, int endIndex) {
    List<Element> elems = all.getElements();
    endIndex = endIndex == -1 ? elems.size() - 1 : endIndex;
    for (int i = startIndex; i <= endIndex; i++) {
      elems.get(i).setPropertyInt("viewIndex", i);
    }
  }

  private void constrainFocusElement() {
    int scrollLeft = getElement().getScrollLeft();
    int scrollTop = getElement().getScrollTop();
    int left = getElement().getWidth(true) / 2 + scrollLeft;
    int top = getElement().getHeight(true) / 2 + scrollTop;
    focusEl.setLeftTop(left, top);
  }

  private void ensureFocusElement() {
    if (focusEl != null) {
      focusEl.removeFromParent();
    }
    focusEl = (XElement) getElement().appendChild(focusImpl.createFocusable());
    focusEl.addClassName(CommonStyles.get().noFocusOutline());
    if (focusEl.hasChildNodes()) {
      focusEl.getFirstChildElement().addClassName(CommonStyles.get().noFocusOutline());
      com.google.gwt.dom.client.Style focusElStyle = focusEl.getFirstChildElement().getStyle();
      focusElStyle.setBorderWidth(0, Unit.PX);
      focusElStyle.setFontSize(1, Unit.PX);
      focusElStyle.setPropertyPx("lineHeight", 1);
    }
    focusEl.setLeft(0);
    focusEl.setTop(0);
    focusEl.makePositionable(true);
    focusEl.addEventsSunk(Event.FOCUSEVENTS);

  }

}
