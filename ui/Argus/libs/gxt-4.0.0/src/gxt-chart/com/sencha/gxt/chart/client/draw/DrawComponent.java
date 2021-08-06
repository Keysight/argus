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
package com.sencha.gxt.chart.client.draw;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.chart.client.draw.engine.VML;
import com.sencha.gxt.chart.client.draw.sprite.CircleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.SpriteHandler;
import com.sencha.gxt.chart.client.draw.sprite.SpriteHandler.HasSpriteHandlers;
import com.sencha.gxt.chart.client.draw.sprite.SpriteList;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOutEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOutEvent.HasSpriteOutHandlers;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOutEvent.SpriteOutHandler;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOverEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOverEvent.HasSpriteOverHandlers;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOverEvent.SpriteOverHandler;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.HasSpriteSelectionHandlers;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.chart.client.draw.sprite.SpriteUpEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteUpEvent.HasSpriteUpHandlers;
import com.sencha.gxt.chart.client.draw.sprite.SpriteUpEvent.SpriteUpHandler;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.PrecisePoint;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.XEvent;

/**
 * The Draw Component is a surface in which {@link Sprite}s can be rendered. The
 * Draw Component manages and holds a {@link Surface} instance: an interface
 * that has an SVG or VML implementation depending on the browser capabilities
 * and where Sprites can be appended.
 * <p/>
 * Here is an example of creating a draw component:
 * <p/>
 * <pre>
 * DrawComponent component = new DrawComponent();
 * CircleSprite circle = new CircleSprite();
 * circle.setCenterX(120);
 * circle.setCenterY(100);
 * circle.setRadius(25);
 * circle.setFill(RGB.GREEN);
 * circle.setStroke(new RGB(#999));
 * component.add(circle);
 *
 * ContentPanel panel = new ContentPanel();
 * panel.setHeading("Basic Draw");
 * panel.setPixelSize(320, 300);
 * panel.add(component);
 * </pre>
 * <p/>
 * The type of the sprite is a {@link CircleSprite} so if you run this code
 * you'll see a green circle with gray stroke in a {@link ContentPanel}.
 */
public class DrawComponent extends Component implements HasSpriteHandlers, HasSpriteOutHandlers, HasSpriteOverHandlers,
    HasSpriteSelectionHandlers, HasSpriteUpHandlers {

  protected Surface surface;
  private boolean viewBox = false;
  private Color background = RGB.WHITE;
  private boolean deferred = false;
  private HandlerManager handlerManager;
  private Sprite lastSprite;

  /**
   * Creates a draw widget at the default size.
   */
  public DrawComponent() {
    this(500, 500);
  }

  /**
   * Creates a draw widget using the given width and height.
   *
   * @param width  the width of the draw widget
   * @param height the height of the draw widget
   */
  public DrawComponent(int width, int height) {
    setElement(Document.get().createDivElement());
    sinkEvents(Event.ONMOUSEMOVE | Event.ONMOUSEDOWN | Event.ONMOUSEOUT | Event.ONMOUSEUP);
    createSurface(width, height);
    setPixelSize(width, height);
    addGestureRecognizer(new TapGestureRecognizer() {
      @Override
      protected void onTap(TouchData touchData) {
        super.onTap(touchData);
        DrawComponent.this.onTap(touchData);
      }
    });
  }

  /**
   * Adds a {@link Gradient} to the draw widget's {@link Surface}.
   *
   * @param gradient the gradient to be added
   */
  public void addGradient(Gradient gradient) {
    surface.addGradient(gradient);
  }

  /**
   * Adds a {@link Sprite} to the draw widget's {@link Surface}.
   *
   * @param sprite the sprite to be added
   */
  public void addSprite(Sprite sprite) {
    sprite.setComponent(this);
    surface.add(sprite);
  }

  public HandlerRegistration addSpriteHandler(SpriteHandler handler) {
    GroupingHandlerRegistration reg = new GroupingHandlerRegistration();
    reg.add(ensureHandler().addHandler(SpriteSelectionEvent.getType(), handler));
    reg.add(ensureHandler().addHandler(SpriteOutEvent.getType(), handler));
    reg.add(ensureHandler().addHandler(SpriteOverEvent.getType(), handler));
    reg.add(ensureHandler().addHandler(SpriteUpEvent.getType(), handler));
    return reg;
  }

  @Override
  public HandlerRegistration addSpriteOutHandler(SpriteOutHandler handler) {
    return ensureHandler().addHandler(SpriteOutEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addSpriteOverHandler(SpriteOverHandler handler) {
    return ensureHandler().addHandler(SpriteOverEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addSpriteSelectionHandler(SpriteSelectionHandler handler) {
    return ensureHandler().addHandler(SpriteSelectionEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addSpriteUpHandler(SpriteUpHandler handler) {
    return ensureHandler().addHandler(SpriteUpEvent.getType(), handler);
  }

  /**
   * Clears all of the sprites from the draw surface.
   */
  public void clearSurface() {
    surface.clear();
  }

  /**
   * Returns the background color of the component.
   *
   * @return the background color of the component
   */
  public Color getBackground() {
    return background;
  }

  /**
   * Returns the {@link Surface}.
   *
   * @return the surface
   */
  public Surface getSurface() {
    return surface;
  }

  /**
   * Returns true if the widget is to have a view box.
   *
   * @return true if the widget is to have a view box
   */
  public boolean isViewBox() {
    return viewBox;
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);

    switch (event.getTypeInt()) {
      case Event.ONMOUSEMOVE:
        onMouseMove(event);
        break;
      case Event.ONMOUSEDOWN:
        onMouseDown(event);
        break;
      case Event.ONMOUSEOUT:
        onMouseOut(event);
        break;
      case Event.ONMOUSEUP:
        onMouseUp(event);
        break;
    }
  }

  /**
   * Method used when the chart is clicked.
   *
   * @param event the mouse event
   */
  public void onMouseDown(Event event) {
    if (handlerManager != null && handlerManager.getHandlerCount(SpriteSelectionEvent.getType()) > 0) {
      PrecisePoint point = getEventXY(event);
      SpriteList<Sprite> sprites = surface.sprites;
      for (int i = sprites.size() - 1; i >= 0; i--) {
        Sprite sprite = sprites.get(i);
        if (!sprite.isHidden() && sprite.getBBox().contains(point)) {
          ensureHandler().fireEvent(new SpriteSelectionEvent(sprite, event));
          return;
        }
      }
    }
  }

  /**
   * Method used when the mouse moves over the chart.
   *
   * @param event the mouse event
   */
  public void onMouseMove(Event event) {
    if (handlerManager != null && handlerManager.getHandlerCount(SpriteOverEvent.getType()) > 0) {
      PrecisePoint point = getEventXY(event);
      SpriteList<Sprite> sprites = surface.sprites;
      for (int i = sprites.size() - 1; i >= 0; i--) {
        Sprite sprite = sprites.get(i);
        if (!sprite.isHidden() && sprite.getBBox().contains(point)) {
          ensureHandler().fireEvent(new SpriteOverEvent(sprite, event));
          lastSprite = sprite;
          return;
        }
      }
    }
    if (lastSprite != null) {
      ensureHandler().fireEvent(new SpriteOutEvent(lastSprite, event));
      lastSprite = null;
    }
  }

  /**
   * Method used when the mouse leaves the chart.
   *
   * @param event the mouse event
   */
  public void onMouseOut(Event event) {
    if (lastSprite != null) {
      ensureHandler().fireEvent(new SpriteOutEvent(lastSprite, event));
      lastSprite = null;
    }
  }

  /**
   * Method used when the mouse is released over the chart.
   *
   * @param event the mouse event
   */
  public void onMouseUp(Event event) {
    if (handlerManager != null && handlerManager.getHandlerCount(SpriteUpEvent.getType()) > 0) {
      PrecisePoint point = getEventXY(event);
      SpriteList<Sprite> sprites = surface.sprites;
      for (int i = sprites.size() - 1; i >= 0; i--) {
        Sprite sprite = sprites.get(i);
        if (!sprite.isHidden() && sprite.getBBox().contains(point)) {
          ensureHandler().fireEvent(new SpriteUpEvent(sprite, event));
          return;
        }
      }
    }
  }

  public void onTap(TouchData touchData) {
    // delegate to mouse events for tap.  Order matters
    // onMouseOut cleans up from previous tap
    // onMouseMove handles any animations that occur on mouse move/over
    // onMouseDown is mainly for item selection as well as legend select/deselect
    Event event = touchData.getLastNativeEvent().cast();
    onMouseOut(event);
    onMouseMove(event);
    onMouseDown(event);
  }

  /**
   * Calls the {@link Surface} to render.
   */
  public void redrawSurface() {
    if (!deferred) {
      deferred = true;
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
        public void execute() {
          render();
        }
      });
    }
  }

  /**
   * Redraws the surface immediately.
   */
  public void redrawSurfaceForced() {
    render();
  }

  /**
   * Removes a {@link Sprite} from the draw widget's {@link Surface}.
   *
   * @param sprite the sprite to be removed
   */
  public void remove(Sprite sprite) {
    surface.deleteSprite(sprite);
  }

  /**
   * Renders the given sprite to the surface.
   *
   * @param sprite the sprite to be rendered
   */
  public void renderSprite(Sprite sprite) {
    surface.renderSprite(sprite);
  }

  /**
   * Sets the background color of the chart. If set to Color.NONE there will be
   * no background color.
   *
   * @param background the background color of the chart
   */
  public void setBackground(Color background) {
    this.background = background;
    surface.setBackground(background);
  }

  @Override
  public void setPixelSize(int width, int height) {
    super.setPixelSize(width, height);
    setSurfaceSize(width, height);
  }

  /**
   * Sets whether the {@link Surface} has a view box. The view box is determined
   * by the bounding box of all the {@link Sprite}s in the surface.
   *
   * @param viewBox true for the surface to have a view box
   */
  public void setViewBox(boolean viewBox) {
    this.viewBox = viewBox;
  }

  protected HandlerManager ensureHandler() {
    if (handlerManager == null) {
      handlerManager = new HandlerManager(this);
    }
    return handlerManager;
  }

  /**
   * Returns the point of the event adjusted for the position of the chart.
   *
   * @param event the event
   * @return the adjusted point
   */
  protected PrecisePoint getEventXY(Event event) {
    Point eventLocation = event.<XEvent>cast().getXY();
    return new PrecisePoint(eventLocation.getX() - this.getAbsoluteLeft() + XDOM.getBodyScrollLeft(), eventLocation.getY() - this.getAbsoluteTop() + XDOM.getBodyScrollTop());
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    if (surface instanceof VML) {
      ((VML) surface).drawIgnoreOptimizations();
    }
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);

    setSurfaceSize(width, height);
    redrawSurface();
  }

  /**
   * Creates a surface using the given width and height. This surface is
   * attached to the draw widget.
   *
   * @param width  the width of the surface
   * @param height the height of the surface
   */
  private void createSurface(int width, int height) {
    surface = Surface.create(this, width, height);
  }

  private void render() {
    surface.draw();
    deferred = false;
  }

  private void setSurfaceSize(int width, int height) {
    Size frameWidth = getElement().getFrameSize();
    height -= frameWidth.getHeight();
    width -= frameWidth.getWidth();

    if (width != -1) {
      surface.setWidth(width);
    }
    if (height != -1) {
      surface.setHeight(height);
    }
  }

}
