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
package com.sencha.gxt.core.client.gestures;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.util.Point;

public class PinchAndRotateGestureRecognizer extends AbstractTwoTouchGestureRecognizer {
  private double initialDistance;
  private double initialAngle;
  private Point initialAvgPosition;


  @Override
  protected void bothTouchStart(TouchData first, TouchData second) {
    initialDistance = Math.sqrt(Math.pow(first.getStartPosition().getX() - second.getStartPosition().getX(), 2) +
                                Math.pow(first.getStartPosition().getY() - second.getStartPosition().getY(), 2));

    initialAngle = Math.atan2(first.getStartPosition().getY() - second.getStartPosition().getY(),
                              first.getStartPosition().getX() - second.getStartPosition().getX());

    initialAvgPosition = new Point((first.getStartPosition().getX() + second.getStartPosition().getX()) / 2,
            (first.getStartPosition().getY() + second.getStartPosition().getY()) / 2);
  }

  @Override
  protected void bothTouchUpdate(TouchData first, TouchData second) {
    // measure change, fire update event(s?)
    double newDistance = Math.sqrt(Math.pow(first.getLastPosition().getX() - second.getLastPosition().getX(), 2) +
            Math.pow(first.getLastPosition().getY() - second.getLastPosition().getY(), 2));
    double newAngle = Math.atan2(first.getLastPosition().getY() - second.getLastPosition().getY(),
            first.getLastPosition().getX() - second.getLastPosition().getX());

    Point newAvgPosition = new Point((first.getLastPosition().getX() + second.getLastPosition().getX()) / 2,
            (first.getLastPosition().getY() + second.getLastPosition().getY()) / 2);


    fireEvent(new RotateGestureMoveEvent(this,
            newAngle, initialAngle,
            newAvgPosition, initialAvgPosition,
            newDistance, initialDistance));
  }


  @Override
  protected void fireEndEvent(List<TouchData> touches) {
    //TODO fire end event, finished
  }

  @Override
  protected void fireCancelEvent(List<TouchData> touches) {
    //TODO fire cancel event, stopped early
  }


  //TODO just one event for both rotate/scale? also include position? or three events?
  public static class RotateGestureMoveEvent extends AbstractGestureEvent<RotateGestureMoveEvent.RotateGestureMoveHandler> {
    private static final Type<RotateGestureMoveHandler> TYPE = new Type<RotateGestureMoveHandler>();
    private final PinchAndRotateGestureRecognizer gesture;
    private final double newAngle;
    private final double initialAngle;
    private final Point newAvgPosition;
    private final Point initialAvgPosition;
    private final double newDistance;
    private final double initialDistance;

    public RotateGestureMoveEvent(PinchAndRotateGestureRecognizer gesture, double newAngle, double initialAngle, Point newAvgPosition, Point initialAvgPosition, double newDistance, double initialDistance) {
      super(gesture);
      this.gesture = gesture;
      this.newAngle = newAngle;
      this.initialAngle = initialAngle;
      this.newAvgPosition = newAvgPosition;
      this.initialAvgPosition = initialAvgPosition;
      this.newDistance = newDistance;
      this.initialDistance = initialDistance;
    }

    public PinchAndRotateGestureRecognizer getGesture() {
      return gesture;
    }

    public double getNewAngle() {
      return newAngle;
    }

    public double getInitialAngle() {
      return initialAngle;
    }

    public Point getNewAvgPosition() {
      return newAvgPosition;
    }

    public Point getInitialAvgPosition() {
      return initialAvgPosition;
    }

    public double getNewDistance() {
      return newDistance;
    }

    public double getInitialDistance() {
      return initialDistance;
    }

    public static Type<RotateGestureMoveHandler> getType() {
      return TYPE;
    }

    @Override
    public Type<RotateGestureMoveHandler> getAssociatedType() {
      return TYPE;
    }

    @Override
    public void dispatch(RotateGestureMoveHandler handler) {
      handler.onRotateGesture(this);
    }

    public interface RotateGestureMoveHandler extends EventHandler {
      void onRotateGesture(RotateGestureMoveEvent event);
    }

    public interface HasRotateGestureMoveHandlers {
      HandlerRegistration addRotateGestureHandler(RotateGestureMoveHandler handler);
    }
  }
}
