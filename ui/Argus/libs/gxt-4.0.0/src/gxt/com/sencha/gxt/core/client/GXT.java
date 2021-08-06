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
package com.sencha.gxt.core.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.core.client.BindingPropertySet.PropertyName;

/**
 * GXT core utilities and functions.
 */
public class GXT {
  @PropertyName("gxt.device")
  interface Device extends BindingPropertySet {
    @PropertyValue("desktop")
    boolean isDesktop();

    @PropertyValue("tablet")
    boolean isTablet();


  }
  
  @PropertyName("gxt.user.agent")
  interface UserAgent extends BindingPropertySet {
    @PropertyValue("safari3")
    boolean isSafari3();

    @PropertyValue("safari4")
    boolean isSafari4();

    @PropertyValue("safari5")
    boolean isSafari5();

    @PropertyValue("ie8")
    boolean isIE8();

    @PropertyValue("ie9")
    boolean isIE9();

    @PropertyValue("ie10")
    boolean isIE10();

    @PropertyValue("ie11")
    boolean isIE11();

    @PropertyValue("edge")
    boolean isMSEdge();

    @PropertyValue("gecko1_8")
    boolean isGecko1_8();

    @PropertyValue("gecko1_9")
    boolean isGecko1_9();

    @PropertyValue("chrome")
    boolean isChrome();

    @PropertyValue("air")
    boolean isAir();
  }

  @PropertyName("user.agent.os")
  interface OS extends BindingPropertySet {
    @PropertyValue("mac")
    boolean isMac();

    @PropertyValue("linux")
    boolean isLinux();

    @PropertyValue("windows")
    boolean isWindows();
  }

  private static final boolean isSecure;
  private static boolean useShims;

  private static final String sslSecureUrl = GWT.getModuleBaseURL() + "blank.html";
  private static String blankImageUrl;

  private static final OS platform() {
    return GWT.<OS> create(OS.class);
  }

  private static final UserAgent userAgent() {
    return GWT.<UserAgent> create(UserAgent.class);
  }

  private static final Device device() {
    return GWT.<Device>create(Device.class);
  }

  /**
   * URL to a 1x1 transparent GIF image used by GXT to create inline icons with
   * CSS background images.
   * 
   * @return the blank image url
   */
  public static String getBlankImageUrl() {
    return blankImageUrl;
  }

  /**
   * URL to a blank file used by GXT when in secure mode for iframe src to
   * prevent the IE insecure content.
   * 
   * @return the secure url
   */
  public static String getSslSecureUrl() {
    return sslSecureUrl;
  }

  /**
   * Returns the version information.
   * 
   * @return the version information
   */
  public static Version getVersion() {
    return GWT.create(Version.class);
  }

  /**
   * Initializes GXT.
   */
  static {
    // don't override if set to true
    if (!useShims) {
      useShims = (isMac() && isGecko1_8());
    }

    isSecure = Window.Location.getProtocol().toLowerCase().startsWith("https");
    if (blankImageUrl == null) {
      if (isIE8() || (isGecko() && !isSecure)) {
        blankImageUrl = "data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
      } else {
        blankImageUrl = GWT.getModuleBaseURL() + "clear.gif";
      }
    }
  }

  public static boolean isTablet() {
    return device().isTablet();
  }

  /**
   * Returns true if device is touch enabled.
   *
   * @return true if touch-enabled
   */
  public static boolean isTouch() {
    return isTablet() || isMSEdge();
  }

  /**
   * Returns true if the device is a desktop
   *
   * @return true if desktop
   */
  public static boolean isDesktop() {
    return device().isDesktop();
  }

  /**
   * Returns true if the browser is Adobe Air.
   * 
   * @return true if air
   */
  public static boolean isAir() {
    return userAgent().isAir();
  }

  /**
   * Returns true if the browser is Chrome.
   * 
   * @return true if Chrome
   */
  public static boolean isChrome() {
    return userAgent().isChrome();
  }

  /**
   * Returns true if the browser is Gecko.
   * 
   * @return true if Gecko
   */
  public static boolean isGecko() {
    return isGecko1_8() || isGecko1_9();
  }

  /**
   * Returns true if the browser is Gecko 2.
   * 
   * @return true if Gecko 2
   */
  public static boolean isGecko1_8() {
    return userAgent().isGecko1_8();
  }

  /**
   * Returns true if the browser is Gecko 3.
   * 
   * @return true if Gecko 3
   */
  public static boolean isGecko1_9() {
    return userAgent().isGecko1_9();
  }

  /**
   * Returns true if the browser is IE.
   * 
   * @return true if IE
   */
  public static boolean isIE() {
    return isIE8() || isIE9() || isIE10() || isIE11();
  }

  /**
   * Returns true if the browser is IE 8.
   * 
   * @return true if IE 8
   */
  public static boolean isIE8() {
    return userAgent().isIE8();
  }

  /**
   * Returns true if the browser is IE 9.
   * 
   * @return true if IE 9
   */
  public static boolean isIE9() {
    return userAgent().isIE9();
  }

  /**
   * Returns true if the browser is IE 10.
   * 
   * @return true if IE 10
   */
  public static boolean isIE10() {
    return userAgent().isIE10();
  }

  /**
   * Returns true if the browser is IE 11.
   *
   * @return true if IE 11
   */
  public static boolean isIE11() {
    return userAgent().isIE11();
  }

  /**
   * Returns true if the browser is MS Edge.
   *
   * @return true if MS Edge
   */
  public static boolean isMSEdge() {
    return userAgent().isMSEdge();
  }

  /**
   * Returns true if the OS is Linux.
   * 
   * @return true if windows
   */
  public static boolean isLinux() {
    return platform().isLinux();
  }

  /**
   * Returns true if the OS is Mac.
   * 
   * @return true if mac
   */
  public static boolean isMac() {
    return platform().isMac();
  }

  /**
   * Returns true if the browser is Safari.
   * 
   * @return true for Safari
   */
  public static boolean isSafari() {
    return isSafari3() || isSafari4() || isSafari5();
  }

  /**
   * Returns true if the browser is Safari 3.
   * 
   * @return true if Safari 3
   */
  public static boolean isSafari3() {
    return userAgent().isSafari3();
  }

  /**
   * Returns true if the browser is Safari 4.
   * 
   * @return true if Safari 4
   */
  public static boolean isSafari4() {
    return userAgent().isSafari4();
  }

  /**
   * Returns true if the browser is Safari 5.
   * 
   * @return true if Safari 5
   */
  public static boolean isSafari5() {
    return userAgent().isSafari5();
  }

  /**
   * Returns true if using HTTPS.
   * 
   * @return true if HTTPS
   */
  public static boolean isSecure() {
    return isSecure;
  }

  /**
   * Returns true if the browser uses shims. By default, GXT intelligently
   * decides whether floating elements should be shimmed. If you are using
   * flash, you may want to set this to true {@link #setUseShims(boolean)}.
   * 
   * @return true for shims
   */
  public static boolean isUseShims() {
    return useShims;
  }

  /**
   * Returns true if the browser uses the webkit engine.
   * 
   * @return true for webkit
   */
  public static boolean isWebKit() {
    return isChrome() || isSafari() || isAir();
  }

  /**
   * Returns true if the OS is Windows.
   * 
   * @return true if windows
   */
  public static boolean isWindows() {
    return platform().isWindows();
  }

  /**
   * True to force the use of shims.
   * 
   * @param useShims true to use shims
   */
  public static void setUseShims(boolean useShims) {
    GXT.useShims = useShims;
  }
}
