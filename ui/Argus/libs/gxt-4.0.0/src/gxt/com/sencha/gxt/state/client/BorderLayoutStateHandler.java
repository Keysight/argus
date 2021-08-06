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
package com.sencha.gxt.state.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.state.client.BorderLayoutStateHandler.BorderLayoutState;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.SplitBar;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.event.AddEvent;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent.CollapseItemHandler;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;
import com.sencha.gxt.widget.core.client.event.RemoveEvent;
import com.sencha.gxt.widget.core.client.event.RemoveEvent.RemoveHandler;
import com.sencha.gxt.widget.core.client.event.SplitBarDragEvent;
import com.sencha.gxt.widget.core.client.event.SplitBarDragEvent.SplitBarDragHandler;

public class BorderLayoutStateHandler extends ComponentStateHandler<BorderLayoutState, BorderLayoutContainer> {

  public interface BorderLayoutState {
    Set<String> getCollapsed();

    Map<String, Double> getSizes();

    void setCollapsed(Set<String> collapsed);

    void setSizes(Map<String, Double> sizes);
  }

  private class Handler implements AddHandler, RemoveHandler, CollapseItemHandler<ContentPanel>,
      ExpandItemHandler<ContentPanel> {

    @Override
    public void onAdd(AddEvent event) {
      handleAdd(event);
    }

    @Override
    public void onCollapse(CollapseItemEvent<ContentPanel> event) {
      handleCollapse(event);
    }

    @Override
    public void onExpand(ExpandItemEvent<ContentPanel> event) {
      handleExpand(event);
    }

    @Override
    public void onRemove(RemoveEvent event) {
      handleRemove(event);
    }

  }

  private SplitBarDragHandler splitBarHandler = new SplitBarDragHandler() {

    @Override
    public void onDragEvent(SplitBarDragEvent event) {
      handleResize(event);
    }
  };

  public BorderLayoutStateHandler(BorderLayoutContainer component) {
    super(BorderLayoutState.class, component);

    Handler h = new Handler();
    component.addAddHandler(h);
    component.addCollapseHandler(h);
    component.addExpandHandler(h);
  }

  public BorderLayoutStateHandler(BorderLayoutContainer component, String key) {
    super(BorderLayoutState.class, component, key);

    Handler h = new Handler();
    component.addAddHandler(h);
    component.addCollapseHandler(h);
    component.addExpandHandler(h);
  }

  @Override
  public void applyState() {

  }

  protected void handleAdd(AddEvent event) {
    if (!getObject().isStateful()) return;
    Widget w = event.getWidget();

    Object obj = w.getLayoutData();

    if (obj instanceof BorderLayoutData && w instanceof Component) {
      BorderLayoutData data = (BorderLayoutData) obj;
      Component c = (Component)w;
      String id = c.getId();
      
      Map<String, Double> sizes = getState().getSizes();
      if (sizes != null && sizes.containsKey(id)) {
        data.setSize(sizes.get(id));
      }

      if (c instanceof ContentPanel) {
        Set<String> collapsed = getState().getCollapsed();
        if (collapsed != null && collapsed.contains(id)) {
          data.setCollapsed(true);
        }
      }

      SplitBar bar = c.getData("splitBar");
      if (bar != null) {
        bar.addSplitBarDragHandler(splitBarHandler);
      }
    }

  }

  protected void handleCollapse(CollapseItemEvent<ContentPanel> event) {
    if (!getObject().isStateful()) return;
    ContentPanel panel = event.getItem();
    Set<String> collapsed = getState().getCollapsed();
    if (collapsed == null) {
      collapsed = new HashSet<String>();
    }
    collapsed.add(panel.getId());

    getState().setCollapsed(collapsed);
    saveState();
  }

  protected void handleExpand(ExpandItemEvent<ContentPanel> event) {
    if (!getObject().isStateful()) return;
    ContentPanel panel = event.getItem();
    Set<String> collapsed = getState().getCollapsed();
    if (collapsed != null) {
      collapsed.remove(panel.getId());
    }

    getState().setCollapsed(collapsed);
    saveState();
  }

  protected void handleRemove(RemoveEvent event) {
    if (!getObject().isStateful()) return;
    Component c = (Component) event.getWidget();
    SplitBar bar = c.getData("splitBar");
    if (bar != null) {
      ComponentHelper.removeHandler(bar, SplitBarDragEvent.getType(), splitBarHandler);
    }
  }

  protected void handleResize(SplitBarDragEvent event) {
    SplitBar bar = event.getSource();
    Component target = bar.getTargetWidget();

    BorderLayoutData data = (BorderLayoutData) target.getLayoutData();

    Map<String, Double> sizes = getState().getSizes();
    if (sizes == null) {
      sizes = new HashMap<String, Double>();
    }

    sizes.put(target.getId(), data.getSize());

    getState().setSizes(sizes);
    saveState();
  }
}
