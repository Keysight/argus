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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an RGB color.
 */
public class RGB extends Color {

  private int red;
  private int green;
  private int blue;

  /**
   * 0, 0, 0
   */
  public static final RGB BLACK = new RGB(0, 0, 0);

  /**
   * 0, 0, 255
   */
  public static final RGB BLUE = new RGB(0, 0, 255);

  /**
   * 0, 255, 255
   */
  public static final RGB CYAN = new RGB(0, 255, 255);

  /**
   * 169, 169, 169
   */
  public static final RGB DARKGRAY = new RGB(169, 169, 169);

  /**
   * 128, 128, 128
   */
  public static final RGB GRAY = new RGB(128, 128, 128);

  /**
   * 0, 128, 0
   */
  public static final RGB GREEN = new RGB(0, 128, 0);

  /**
   * 211, 211, 211
   */
  public static final RGB LIGHTGRAY = new RGB(211, 211, 211);

  /**
   * 255, 0, 255
   */
  public static final RGB MAGENTA = new RGB(255, 0, 255);

  /**
   * 255, 165, 0
   */
  public static final RGB ORANGE = new RGB(255, 165, 0);

  /**
   * 252, 192, 203
   */
  public static final RGB PINK = new RGB(252, 192, 203);

  /**
   * 128, 0, 128
   */
  public static final RGB PURPLE = new RGB(128, 0, 128);

  /**
   * 255, 0, 0
   */
  public static final RGB RED = new RGB(255, 0, 0);

  /**
   * 255, 255, 0
   */
  public static final RGB YELLOW = new RGB(255, 255, 0);

  /**
   * 255, 255, 255
   */
  public static final RGB WHITE = new RGB(255, 255, 255);

  /**
   * Creates an instance of RGB with default values.
   */
  public RGB() {
  }

  /**
   * Creates an RGB instance using the given {@link HSL} color.
   * 
   * @param hsl the HSL color
   */
  public RGB(HSL hsl) {
    double h = hsl.getHue();
    double s = hsl.getSaturation();
    double l = hsl.getLightness();

    if (s == 0 || l == 0 || l == 1) {
      // achromatic
      int value = (int) Math.round(l * 255.0);
      setRed(value);
      setGreen(value);
      setBlue(value);
    } else {
      // http://en.wikipedia.org/wiki/HSL_and_HSV#From_HSL
      // c is the chroma
      // m is the lightness adjustment
      double c = s * (1 - Math.abs(2 * l - 1));
      double m = l - c / 2;

      assignRgbFromChromaHueAndAdjustment(c, h, m);
    }
  }

  /**
   * Creates an RGB instance using the given {@link HSV} color.
   * 
   * @param hsv the HSV color
   */
  public RGB(HSV hsv) {
    double h = hsv.getHue();
    double s = hsv.getSaturation();
    double v = hsv.getValue();

    if (s == 0 || v == 0) {
      int value = (int) Math.round(v * 255.0);
      setRed(value);
      setGreen(value);
      setBlue(value);
    } else {
      // http://en.wikipedia.org/wiki/HSL_and_HSV#From_HSV
      // c is the chroma
      // m is the value adjustment
      double c = v * s;
      double m = v * (1.0 - s);

      assignRgbFromChromaHueAndAdjustment(c, h, m);
    }
  }
  /**
   * Helper method to assign rgb values from chrome, hue and a lightness/value adjustment. This does the ugly work of
   * deciding where on the RGB 'cube' the current hue maps to, and picking a color accordingly.
   */
  private void assignRgbFromChromaHueAndAdjustment(double c, double h, double m) {
    double h1 = h / 60;
    double x = c * (1.0 - Math.abs(h1 - 2.0 * Math.floor(h1 / 2.0) - 1.0));

    double r = 0;
    double g = 0;
    double b = 0;
    switch ((int) Math.floor(h1)) {
      case 0:
        r = c;
        g = x;
        b = 0;
        break;
      case 1:
        r = x;
        g = c;
        b = 0;
        break;
      case 2:
        r = 0;
        g = c;
        b = x;
        break;
      case 3:
        r = 0;
        g = x;
        b = c;
        break;
      case 4:
        r = x;
        g = 0;
        b = c;
        break;
      case 5:
        r = c;
        g = 0;
        b = x;
    }
    r += m;
    g += m;
    b += m;

    setRed((int) Math.round(r * 255.0));
    setGreen((int) Math.round(g * 255.0));
    setBlue((int) Math.round(b * 255.0));
  }

  /**
   * Creates an RGB instance using the given RGB values.
   * 
   * @param red the red value (0..255)
   * @param green the green value (0..255)
   * @param blue the blue value (0..255)
   */
  public RGB(int red, int green, int blue) {
    setRed(red);
    setGreen(green);
    setBlue(blue);
  }

  /**
   * Creates an RGB instance using the given RGB color.
   * 
   * @param rgb the RGB color
   */
  public RGB(RGB rgb) {
    this(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
  }

  /**
   * Creates an RGB instance using the given hexadecimal string.
   * 
   * @param hex supports "#xxx" or "#xxxxxx"
   */
  public RGB(String hex) {
    if (hex.length() > 0 && hex.charAt(0) == '#') {
      if (hex.length() == 7) {
        setRed(Integer.valueOf(hex.substring(1, 3), 16));
        setGreen(Integer.valueOf(hex.substring(3, 5), 16));
        setBlue(Integer.valueOf(hex.substring(5, 7), 16));
      }
      if (hex.length() == 4) {
        setRed(Integer.valueOf(hex.substring(1, 2) + hex.substring(1, 2), 16));
        setGreen(Integer.valueOf(hex.substring(2, 3) + hex.substring(2, 3), 16));
        setBlue(Integer.valueOf(hex.substring(3, 4) + hex.substring(3, 4), 16));
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof RGB)) return false;
    RGB other = (RGB) obj;
    if (blue != other.blue) return false;
    if (green != other.green) return false;
    if (red != other.red) return false;
    return true;
  }

  /**
   * Get the blue widget of the color, in the range 0..255.
   * 
   * @return the blue widget
   */
  public int getBlue() {
    return blue;
  }

  @Override
  public String getColor() {
    return toString();
  }

  /**
   * Return a new color that is darker than this color.
   * 
   * @param factor Darker factor (0..1), default to 0.2
   * @return darker color
   */
  public RGB getDarker(double factor) {
    return this.getLighter(-factor);
  }

  /**
   * Returns the gray value (0 to 255) of the color.
   * 
   * The gray value is calculated using the formula r*0.3 + g*0.59 + b*0.11.
   * 
   * @return the gray value of the color
   */
  public double getGrayScale() {
    // http://en.wikipedia.org/wiki/Grayscale#Converting_color_to_grayscale
    return this.red * 0.3 + this.green * 0.59 + this.blue * 0.11;
  }

  /**
   * Get the green widget of the color, in the range 0..255.
   * 
   * @return the green widget
   */
  public int getGreen() {
    return green;
  }

  /**
   * Return a new color that is lighter than this color.
   * 
   * @param factor Lighter factor (0..1), default to 0.2
   * @return lighter color
   */
  public RGB getLighter(double factor) {
    HSL hsl = new HSL(this);
    hsl.setLightness(Math.min(1.0, Math.max(0.0, hsl.getLightness() + factor)));
    return new RGB(hsl);
  }

  /**
   * Get the red widget of the color, in the range 0..255.
   * 
   * @return the red widget
   */
  public int getRed() {
    return red;
  }

  /**
   * Get the RGB values.
   * 
   * @return list of the rgb values
   */
  public List<Integer> getRGB() {
    List<Integer> rgb = new ArrayList<Integer>();
    rgb.add(red);
    rgb.add(green);
    rgb.add(blue);
    return rgb;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + blue;
    result = prime * result + green;
    result = prime * result + red;
    return result;
  }

  /**
   * Sets the blue value of the RGB color
   * 
   * @param blue the blue value of the RGB color
   */
  public void setBlue(int blue) {
    this.blue = Math.min(255, Math.max(0, blue));
    this.color = null;
  }

  /**
   * Sets the green value of the RGB color
   * 
   * @param green the green value of the RGB color
   */
  public void setGreen(int green) {
    this.green = Math.min(255, Math.max(0, green));
    this.color = null;
  }

  /**
   * Sets the red value of the RGB color
   * 
   * @param red the red value of the RGB color
   */
  public void setRed(int red) {
    this.red = Math.min(255, Math.max(0, red));
    this.color = null;
  }

  @Override
  public String toString() {
    if (color == null) {
      color = "rgb(" + red + ", " + green + ", " + blue + ")";
    }
    return color;
  }

}
