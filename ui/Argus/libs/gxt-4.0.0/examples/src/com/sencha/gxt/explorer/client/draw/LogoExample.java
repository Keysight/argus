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
package com.sencha.gxt.explorer.client.draw;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.Stop;
import com.sencha.gxt.chart.client.draw.path.ClosePath;
import com.sencha.gxt.chart.client.draw.path.CurveTo;
import com.sencha.gxt.chart.client.draw.path.CurveToQuadratic;
import com.sencha.gxt.chart.client.draw.path.CurveToQuadraticSmooth;
import com.sencha.gxt.chart.client.draw.path.CurveToSmooth;
import com.sencha.gxt.chart.client.draw.path.EllipticalArc;
import com.sencha.gxt.chart.client.draw.path.LineTo;
import com.sencha.gxt.chart.client.draw.path.LineToHorizontal;
import com.sencha.gxt.chart.client.draw.path.LineToVertical;
import com.sencha.gxt.chart.client.draw.path.MoveTo;
import com.sencha.gxt.chart.client.draw.path.PathCommand;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.explorer.client.app.ui.ExampleContainer;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;

@Detail(
    name = "Logos",
    category = "Draw",
    icon = "logos",
    minHeight = LogoExample.MIN_HEIGHT,
    minWidth = LogoExample.MIN_WIDTH
)
public class LogoExample implements IsWidget, EntryPoint {

  protected static final int MIN_HEIGHT = 350;
  protected static final int MIN_WIDTH = 350;

  private ContentPanel panel;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      Gradient senchaGrad1 = new Gradient(100);
      senchaGrad1.addStop(0, new Color("#AACE36"));
      senchaGrad1.addStop(100, new Color("#2FA042"));

      Gradient senchaGrad2 = new Gradient(21);
      senchaGrad2.addStop(0, new Color("#79A933"));
      senchaGrad2.addStop(13, new Color("#70A333"));
      senchaGrad2.addStop(34, new Color("#559332"));
      senchaGrad2.addStop(58, new Color("#277B2F"));
      senchaGrad2.addStop(86, new Color("#005F27"));
      senchaGrad2.addStop(100, new Color("#005020"));

      Gradient senchaGrad3 = new Gradient(55);
      senchaGrad3.addStop(0, new Color("#79AB35"));
      senchaGrad3.addStop(53, new Color("#7CBA3D"));
      senchaGrad3.addStop(100, new Color("#00AA4B"));

      PathSprite senchaPath = new PathSprite();
      senchaPath.setCommands(getCommandsFromString("M0,109.718c0-43.13,24.815-80.463,60.955-98.499L82.914,0C68.122,7.85,58.046,23.406,58.046,41.316 "
          + "c0,9.64,2.916,18.597,7.915,26.039c-7.44,18.621-11.77,37.728-13.228,56.742c-9.408,4.755-20.023,7.423-31.203,7.424 "
          + "c-1.074,0-2.151-0.025-3.235-0.075c-5.778-0.263-11.359-1.229-16.665-2.804L0,109.718z M157.473,285.498c0-0.015,0-0.031,0-0.047 "
          + "C157.473,285.467,157.473,285.482,157.473,285.498 M157.473,285.55c0-0.014,0-0.027,0-0.04 "
          + "C157.473,285.523,157.473,285.536,157.473,285.55 M157.472,285.604c0-0.015,0.001-0.031,0.001-0.046 "
          + "C157.473,285.574,157.472,285.588,157.472,285.604 M157.472,285.653c0-0.012,0-0.024,0-0.037 "
          + "C157.472,285.628,157.472,285.641,157.472,285.653 M157.472,285.708c0-0.015,0-0.028,0-0.045 "
          + "C157.472,285.68,157.472,285.694,157.472,285.708 M157.472,285.756c0-0.012,0-0.023,0-0.034 "
          + "C157.472,285.733,157.472,285.745,157.472,285.756 M157.471,285.814c0-0.014,0-0.028,0.001-0.042 "
          + "C157.471,285.785,157.471,285.8,157.471,285.814 M157.471,285.858c0-0.008,0-0.017,0-0.026 "
          + "C157.471,285.841,157.471,285.85,157.471,285.858 M157.47,285.907c0.001-0.008,0.001-0.018,0.001-0.026 "
          + "C157.471,285.889,157.471,285.898,157.47,285.907 M157.47,285.964c0-0.009,0-0.017,0-0.023 "
          + "C157.47,285.949,157.47,285.955,157.47,285.964 M157.469,286.01c0-0.008,0.001-0.016,0.001-0.022 "
          + "C157.47,285.995,157.469,286.002,157.469,286.01 M157.469,286.069c0-0.008,0-0.016,0-0.022 "
          + "C157.469,286.053,157.469,286.062,157.469,286.069 M157.468,286.112c0-0.005,0-0.011,0-0.017 "
          + "C157.468,286.101,157.468,286.107,157.468,286.112 M157.467,286.214c0-0.003,0-0.006,0-0.008 "
          + "C157.467,286.208,157.467,286.212,157.467,286.214"));
      senchaPath.setFill(new Color("#C5D83E"));

      PathSprite senchaPath1 = new PathSprite();
      senchaPath1.setCommands(getCommandsFromString("M66.218,210.846l-6.824-3.421c-0.016-0.009-0.033-0.018-0.048-0.025"
          + "c-0.006-0.003-0.013-0.007-0.019-0.01c-0.01-0.005-0.017-0.009-0.028-0.015c-0.009-0.005-0.016-0.008-0.025-0.013"
          + "c-0.008-0.005-0.012-0.007-0.021-0.011c-0.009-0.005-0.018-0.01-0.027-0.014c-0.007-0.005-0.013-0.008-0.02-0.012"
          + "c-0.009-0.005-0.02-0.01-0.029-0.015c-0.006-0.003-0.007-0.004-0.014-0.007c-0.038-0.021-0.074-0.039-0.113-0.06"
          + "c-0.002-0.001-0.006-0.003-0.008-0.005c-0.013-0.006-0.023-0.011-0.035-0.018c-0.005-0.002-0.007-0.003-0.011-0.006"
          + "c-0.011-0.005-0.025-0.014-0.036-0.02c-0.004-0.002-0.005-0.002-0.009-0.004c-0.013-0.007-0.025-0.014-0.038-0.02l-0.003-0.002"
          + "c-29.686-15.598-51.36-44.362-57.28-78.53c5.306,1.575,10.887,2.541,16.665,2.804c1.084,0.05,2.161,0.075,3.235,0.075"
          + "c11.18-0.001,21.795-2.669,31.203-7.424C50.44,154.002,55.248,183.676,66.218,210.846"));
      senchaPath1.setFill(senchaGrad1);

      PathSprite senchaPath2 = new PathSprite();
      senchaPath2.setCommands(getCommandsFromString("M88.093,85.247l-3.657-1.834c-0.214-0.103-0.426-0.208-0.638-0.315h-0.001"
          + "c-0.015-0.008-0.029-0.015-0.044-0.022l-0.001-0.001c-0.014-0.007-0.028-0.014-0.042-0.021c-0.001-0.001-0.003-0.002-0.004-0.002"
          + "c-0.014-0.007-0.027-0.014-0.04-0.02c-0.003-0.002-0.003-0.002-0.006-0.004c-0.013-0.006-0.025-0.012-0.037-0.018"
          + "c-0.003-0.002-0.006-0.004-0.009-0.005c-0.011-0.006-0.022-0.011-0.033-0.017c-0.004-0.002-0.008-0.004-0.013-0.006"
          + "c-0.009-0.005-0.018-0.01-0.027-0.014c-0.006-0.003-0.013-0.007-0.018-0.01c-0.006-0.003-0.013-0.006-0.019-0.009"
          + "c-0.01-0.005-0.018-0.009-0.027-0.014c-0.001-0.001-0.003-0.002-0.004-0.002c-7.075-3.631-13.103-9.016-17.512-15.578"
          + "c-7.44,18.621-11.77,37.728-13.228,56.742c12.607-6.37,23.053-16.485,29.815-28.949L88.093,85.247z M213.364,195.358"
          + "c-25.889,17.124-56.849,27.05-89.924,27.05c-2.519,0-5.05-0.057-7.591-0.174c-14.436-0.662-28.343-3.192-41.515-7.32l56.748,28.445"
          + "c15.615,7.571,26.39,23.571,26.39,42.092v0.107c0,0.015-0.001,0.031-0.001,0.046v0.168c-0.001,0.014-0.001,0.028-0.001,0.042v0.066"
          + "c0,0.009,0,0.019-0.001,0.026v0.081c0,0.007-0.001,0.015-0.001,0.022v0.059c0,0.009-0.001,0.019-0.001,0.026v0.017"
          + "c0,0.032-0.001,0.063-0.001,0.095v0.008c-0.192,12.063-4.956,23.016-12.633,31.202c-3.517,3.753-7.647,6.924-12.23,9.355"
          + "l14.101-7.202l7.859-4.011c36.137-18.041,60.955-55.376,60.955-98.509L213.364,195.358z"));
      senchaPath2.setFill(senchaGrad2);

      PathSprite senchaPath3 = new PathSprite();
      senchaPath3.setCommands(getCommandsFromString("M123.44,222.408c-2.519,0-5.05-0.057-7.591-0.174c-14.436-0.662-28.343-3.192-41.515-7.32"
          + "l-8.117-4.067c-10.97-27.17-15.778-56.844-13.485-86.749c12.607-6.37,23.053-16.485,29.815-28.949l5.545-9.901l68.032,34.101"
          + "c2.462,1.278,4.871,2.648,7.22,4.102c0.006,0.004,0.009,0.006,0.016,0.01c0.009,0.005,0.018,0.011,0.025,0.016"
          + "c0.009,0.005,0.02,0.011,0.028,0.017c0.002,0.001,0.008,0.005,0.01,0.006c25.392,15.756,43.88,41.564,49.94,71.859"
          + "C187.476,212.482,156.516,222.408,123.44,222.408"));
      senchaPath3.setFill(senchaGrad3);

      DrawComponent senchaComponent = new DrawComponent();
      senchaComponent.addGradient(senchaGrad1);
      senchaComponent.addGradient(senchaGrad2);
      senchaComponent.addGradient(senchaGrad3);
      senchaComponent.addSprite(senchaPath);
      senchaComponent.addSprite(senchaPath1);
      senchaComponent.addSprite(senchaPath2);
      senchaComponent.addSprite(senchaPath3);
      senchaComponent.setBackground(Color.NONE);
      senchaComponent.setViewBox(true);

      Gradient blackBlue = new Gradient(90);
      blackBlue.addStop(new Stop(0, new RGB(33, 33, 33)));
      blackBlue.addStop(new Stop(100, new RGB(156, 178, 248)));

      PathSprite logo = new PathSprite();
      logo.setFill(blackBlue);
      logo.setCommands(getCommandsFromString("M28.4,22.469c0.479-0.964,0.851-1.991,1.095-3.066c0.953-3.661,0.666-6.854,0.666-6.854l-0.327,2.104c0,0-0.469-3.896-1.044-5.353c-0.881-2.231-1.273-2.214-1.274-2.21c0.542,1.379,0.494,2.169,0.483,2.288c-0.01-0.016-0.019-0.032-0.027-0.047c-0.131-0.324-0.797-1.819-2.225-2.878c-2.502-2.481-5.943-4.014-9.745-4.015c-4.056,0-7.705,1.745-10.238,4.525C5.444,6.5,5.183,5.938,5.159,5.317c0,0-0.002,0.002-0.006,0.005c0-0.011-0.003-0.021-0.003-0.031c0,0-1.61,1.247-1.436,4.612c-0.299,0.574-0.56,1.172-0.777,1.791c-0.375,0.817-0.75,2.004-1.059,3.746c0,0,0.133-0.422,0.399-0.988c-0.064,0.482-0.103,0.971-0.116,1.467c-0.09,0.845-0.118,1.865-0.039,3.088c0,0,0.032-0.406,0.136-1.021c0.834,6.854,6.667,12.165,13.743,12.165l0,0c1.86,0,3.636-0.37,5.256-1.036C24.938,27.771,27.116,25.196,28.4,22.469zM16.002,3.356c2.446,0,4.73,0.68,6.68,1.86c-2.274-0.528-3.433-0.261-3.423-0.248c0.013,0.015,3.384,0.589,3.981,1.411c0,0-1.431,0-2.856,0.41c-0.065,0.019,5.242,0.663,6.327,5.966c0,0-0.582-1.213-1.301-1.42c0.473,1.439,0.351,4.17-0.1,5.528c-0.058,0.174-0.118-0.755-1.004-1.155c0.284,2.037-0.018,5.268-1.432,6.158c-0.109,0.07,0.887-3.189,0.201-1.93c-4.093,6.276-8.959,2.539-10.934,1.208c1.585,0.388,3.267,0.108,4.242-0.559c0.982-0.672,1.564-1.162,2.087-1.047c0.522,0.117,0.87-0.407,0.464-0.872c-0.405-0.466-1.392-1.105-2.725-0.757c-0.94,0.247-2.107,1.287-3.886,0.233c-1.518-0.899-1.507-1.63-1.507-2.095c0-0.366,0.257-0.88,0.734-1.028c0.58,0.062,1.044,0.214,1.537,0.466c0.005-0.135,0.006-0.315-0.001-0.519c0.039-0.077,0.015-0.311-0.047-0.596c-0.036-0.287-0.097-0.582-0.19-0.851c0.01-0.002,0.017-0.007,0.021-0.021c0.076-0.344,2.147-1.544,2.299-1.659c0.153-0.114,0.55-0.378,0.506-1.183c-0.015-0.265-0.058-0.294-2.232-0.286c-0.917,0.003-1.425-0.894-1.589-1.245c0.222-1.231,0.863-2.11,1.919-2.704c0.02-0.011,0.015-0.021-0.008-0.027c0.219-0.127-2.524-0.006-3.76,1.604C9.674,8.045,9.219,7.95,8.71,7.95c-0.638,0-1.139,0.07-1.603,0.187c-0.05,0.013-0.122,0.011-0.208-0.001C6.769,8.04,6.575,7.88,6.365,7.672c0.161-0.18,0.324-0.356,0.495-0.526C9.201,4.804,12.43,3.357,16.002,3.356z"));

      DrawComponent component1 = new DrawComponent();
      component1.addSprite(logo);
      component1.addGradient(blackBlue);
      component1.setViewBox(true);

      logo = new PathSprite();
      logo.setFill(blackBlue);
      logo.setCommands(getCommandsFromString("M27.998,2.266c-2.12-1.91-6.925,0.382-9.575,1.93c-0.76-0.12-1.557-0.185-2.388-0.185c-3.349,0-6.052,0.985-8.106,2.843c-2.336,2.139-3.631,4.94-3.631,8.177c0,0.028,0.001,0.056,0.001,0.084c3.287-5.15,8.342-7.79,9.682-8.487c0.212-0.099,0.338,0.155,0.141,0.253c-0.015,0.042-0.015,0,0,0c-2.254,1.35-6.434,5.259-9.146,10.886l-0.003-0.007c-1.717,3.547-3.167,8.529-0.267,10.358c2.197,1.382,6.13-0.248,9.295-2.318c0.764,0.108,1.567,0.165,2.415,0.165c5.84,0,9.937-3.223,11.399-7.924l-8.022-0.014c-0.337,1.661-1.464,2.548-3.223,2.548c-2.21,0-3.729-1.211-3.828-4.012l15.228-0.014c0.028-0.578-0.042-0.985-0.042-1.436c0-5.251-3.143-9.355-8.255-10.663c2.081-1.294,5.974-3.209,7.848-1.681c1.407,1.14,0.633,3.533,0.295,4.518c-0.056,0.254,0.24,0.296,0.296,0.057C28.814,5.573,29.026,3.194,27.998,2.266zM13.272,25.676c-2.469,1.475-5.873,2.539-7.539,1.289c-1.243-0.935-0.696-3.468,0.398-5.938c0.664,0.992,1.495,1.886,2.473,2.63C9.926,24.651,11.479,25.324,13.272,25.676zM12.714,13.046c0.042-2.435,1.787-3.49,3.617-3.49c1.928,0,3.49,1.112,3.49,3.49H12.714z"));

      DrawComponent component2 = new DrawComponent();
      component2.addSprite(logo);
      component2.addGradient(blackBlue);
      component2.setViewBox(true);

      logo = new PathSprite();
      logo.setFill(blackBlue);
      logo.setCommands(getCommandsFromString("M15.954,2.046c-7.489,0-12.872,5.432-12.872,13.581c0,7.25,5.234,13.835,12.873,13.835c7.712,0,12.974-6.583,12.974-13.835C28.929,7.413,23.375,2.046,15.954,2.046zM15.952,26.548L15.952,26.548c-2.289,0-3.49-1.611-4.121-3.796c-0.284-1.037-0.458-2.185-0.563-3.341c-0.114-1.374-0.129-2.773-0.129-4.028c0-0.993,0.018-1.979,0.074-2.926c0.124-1.728,0.386-3.431,0.89-4.833c0.694-1.718,1.871-2.822,3.849-2.822c2.5,0,3.763,1.782,4.385,4.322c0.429,1.894,0.56,4.124,0.56,6.274c0,2.299-0.103,5.153-0.763,7.442C19.473,24.979,18.242,26.548,15.952,26.548z"));

      DrawComponent component3 = new DrawComponent();
      component3.addSprite(logo);
      component3.addGradient(blackBlue);
      component3.setViewBox(true);

      logo = new PathSprite();
      logo.setFill(blackBlue);
      logo.setCommands(getCommandsFromString("M16.277,8.655c-2.879,0-5.227,2.181-5.227,4.854s2.348,4.854,5.227,4.854c2.879,0,5.227-2.181,5.227-4.854S19.156,8.655,16.277,8.655zM29.535,13.486c-0.369-1.819-1.068-3.052-1.727-3.995c0.05,0.129,0.09,0.259,0.138,0.388c-2.34-6.355-11.704-9.8-18.937-5.43c-0.056,0.27-0.073,0.538-0.073,0.804c0-0.051-0.006-0.098-0.004-0.15c-1.743-0.134-3.854,2.061-5.731,6.083c-0.953,2.277-1.298,4.77-0.414,7.693c0.516,1.706,1.328,3.456,2.499,4.814c3.471,4.027,8.788,5.67,11.884,4.835c0.004,0.001,0.009,0.003,0.014,0.004c5.969-0.125,10.494-4.228,12.125-9.569C29.896,17.035,29.934,15.457,29.535,13.486zM6.043,23.04c-0.96-1.112-1.755-2.651-2.299-4.452c-0.733-2.42-0.612-4.65,0.379-7.015C5.129,9.42,6.111,8.005,6.956,7.154c0.15,0.742,0.521,1.628,1.113,2.649c0.218,0.379,0.459,0.701,0.692,1.012c0.179,0.237,0.356,0.474,0.513,0.729c0.124,0.202,0.239,0.445,0.354,0.737c-0.239,2.754,0.892,5.138,3.148,6.679l-2.546,2.25l-0.202,0.171c-0.208,0.171-0.447,0.373-0.651,0.589c-1.36,1.444-0.25,2.831,0.286,3.498l0.068,0.087c0.237,0.297,0.513,0.62,0.815,0.938C8.963,25.725,7.375,24.585,6.043,23.04zM28.354,18.67c-1.6,5.232-5.937,8.7-11.07,8.859c-2.485-0.583-4.362-1.78-5.586-3.557c0.004-0.004,0.01-0.008,0.015-0.013l4.944-3.836c2.226-0.124,3.854-0.888,4.847-2.278c1.222-1.412,1.792-3.025,1.693-4.861c1.817,0.377,3.389,0.903,4.855,1.883l0.116,0.078l0.134,0.043c0.156,0.049,0.311,0.076,0.459,0.081C28.87,16.309,28.74,17.402,28.354,18.67zM28.609,14.037c-1.951-1.306-4.062-1.867-6.594-2.285c0.531,2.358-0.084,4.072-1.326,5.512c-0.882,1.235-2.382,1.822-4.394,1.875l-5.22,4.052c-0.497,0.409-0.591,0.819-0.282,1.229c0.849,1.277,1.929,2.202,3.122,2.878c-0.013,0-0.026,0.002-0.039,0.003c-0.001-0.001-0.004-0.002-0.006-0.004c-0.02,0.003-0.041,0.004-0.062,0.005c-0.08,0.001-0.16-0.001-0.239-0.01c-0.156-0.021-0.314-0.064-0.459-0.118c-0.898-0.333-1.89-1.352-2.597-2.239c-0.581-0.73-1.206-1.433-0.411-2.275c0.258-0.273,0.582-0.514,0.789-0.698l2.521-2.229c0.172-0.137,0.35-0.277,0.535-0.423c0.053-0.042,0.107-0.084,0.162-0.127c0.564-0.442,0.483-0.32-0.108-0.642c-2.419-1.32-3.677-3.614-3.354-6.389c-0.149-0.41-0.317-0.792-0.518-1.124c-0.363-0.6-0.834-1.102-1.194-1.723c-0.9-1.556-1.847-3.902,0.013-3.682c-0.005-0.053-0.002-0.11-0.005-0.164c0.094,2.001,1.526,3.823,1.742,4.888c0.078,0.382,0.294,0.705,0.612,0.28c2.538-3.395,6.069-3.053,8.328-1.312c0.443,0.34,0.684,0.755,1.084,1.11c0.154,0.138,0.328,0.259,0.535,0.351c0.743,0.332,1.807,0.312,2.607,0.434c1.371,0.208,2.707,0.464,3.971,0.812c0.25,0.03,0.424-0.004,0.521-0.101c0.211-0.208-0.002-0.887-0.121-1.263c0.277,0.805,0.536,1.609,0.773,2.415C29.176,13.701,29.133,14.208,28.609,14.037z"));

      DrawComponent component4 = new DrawComponent();
      component4.addSprite(logo);
      component4.addGradient(blackBlue);
      component4.setViewBox(true);

      logo = new PathSprite();
      logo.setFill(blackBlue);
      logo.setCommands(getCommandsFromString("M16.154,5.135c-0.504,0-1,0.031-1.488,0.089l-0.036-0.18c-0.021-0.104-0.06-0.198-0.112-0.283c0.381-0.308,0.625-0.778,0.625-1.306c0-0.927-0.751-1.678-1.678-1.678s-1.678,0.751-1.678,1.678c0,0.745,0.485,1.376,1.157,1.595c-0.021,0.105-0.021,0.216,0,0.328l0.033,0.167C7.645,6.95,3.712,11.804,3.712,17.578c0,6.871,5.571,12.441,12.442,12.441c6.871,0,12.441-5.57,12.441-12.441C28.596,10.706,23.025,5.135,16.154,5.135zM16.369,8.1c4.455,0,8.183,3.116,9.123,7.287l-0.576,0.234c-0.148-0.681-0.755-1.191-1.48-1.191c-0.837,0-1.516,0.679-1.516,1.516c0,0.075,0.008,0.148,0.018,0.221l-2.771-0.028c-0.054-0.115-0.114-0.226-0.182-0.333l3.399-5.11l0.055-0.083l-4.766,4.059c-0.352-0.157-0.74-0.248-1.148-0.256l0.086-0.018l-1.177-2.585c0.64-0.177,1.111-0.763,1.111-1.459c0-0.837-0.678-1.515-1.516-1.515c-0.075,0-0.147,0.007-0.219,0.018l0.058-0.634C15.357,8.141,15.858,8.1,16.369,8.1zM12.146,3.455c0-0.727,0.591-1.318,1.318-1.318c0.727,0,1.318,0.591,1.318,1.318c0,0.425-0.203,0.802-0.516,1.043c-0.183-0.123-0.413-0.176-0.647-0.13c-0.226,0.045-0.413,0.174-0.535,0.349C12.542,4.553,12.146,4.049,12.146,3.455zM7.017,17.452c0-4.443,3.098-8.163,7.252-9.116l0.297,0.573c-0.61,0.196-1.051,0.768-1.051,1.442c0,0.837,0.678,1.516,1.515,1.516c0.068,0,0.135-0.006,0.2-0.015l-0.058,2.845l0.052-0.011c-0.442,0.204-0.824,0.513-1.116,0.895l0.093-0.147l-1.574-0.603l1.172,1.239l0.026-0.042c-0.19,0.371-0.306,0.788-0.324,1.229l-0.003-0.016l-2.623,1.209c-0.199-0.604-0.767-1.041-1.438-1.041c-0.837,0-1.516,0.678-1.516,1.516c0,0.064,0.005,0.128,0.013,0.191l-0.783-0.076C7.063,18.524,7.017,17.994,7.017,17.452zM16.369,26.805c-4.429,0-8.138-3.078-9.106-7.211l0.691-0.353c0.146,0.686,0.753,1.2,1.482,1.2c0.837,0,1.515-0.679,1.515-1.516c0-0.105-0.011-0.207-0.031-0.307l2.858,0.03c0.045,0.095,0.096,0.187,0.15,0.276l-3.45,5.277l0.227-0.195l4.529-3.92c0.336,0.153,0.705,0.248,1.094,0.266l-0.019,0.004l1.226,2.627c-0.655,0.166-1.142,0.76-1.142,1.468c0,0.837,0.678,1.515,1.516,1.515c0.076,0,0.151-0.007,0.225-0.018l0.004,0.688C17.566,26.746,16.975,26.805,16.369,26.805zM18.662,26.521l-0.389-0.6c0.661-0.164,1.152-0.759,1.152-1.47c0-0.837-0.68-1.516-1.516-1.516c-0.066,0-0.13,0.005-0.193,0.014v-2.86l-0.025,0.004c0.409-0.185,0.77-0.459,1.055-0.798l1.516,0.659l-1.104-1.304c0.158-0.335,0.256-0.704,0.278-1.095l2.552-1.164c0.19,0.618,0.766,1.068,1.447,1.068c0.838,0,1.516-0.679,1.516-1.516c0-0.069-0.006-0.137-0.016-0.204l0.65,0.12c0.089,0.517,0.136,1.049,0.136,1.591C25.722,21.826,22.719,25.499,18.662,26.521z"));

      DrawComponent component5 = new DrawComponent();
      component5.addSprite(logo);
      component5.addGradient(blackBlue);
      component5.setViewBox(true);
      
      senchaComponent.setLayoutData(new MarginData(10));
      component1.setLayoutData(new MarginData(10));
      component2.setLayoutData(new MarginData(10));
      component3.setLayoutData(new MarginData(10));
      component4.setLayoutData(new MarginData(10));
      component5.setLayoutData(new MarginData(10));

      TabPanel tabPanel = new TabPanel();
      tabPanel.setBodyBorder(true);
      tabPanel.setTabScroll(true);
      tabPanel.add(senchaComponent, "Sencha");
      tabPanel.add(component1, "Firefox");
      tabPanel.add(component2, "IE");
      tabPanel.add(component3, "Opera");
      tabPanel.add(component4, "Chrome");
      tabPanel.add(component5, "Safari");

      panel = new ContentPanel();
      panel.setHeading("Logos");
      panel.add(tabPanel);
    }

    return panel;
  }

  @Override
  public void onModuleLoad() {
    new ExampleContainer(this)
        .setMinHeight(MIN_HEIGHT)
        .setMinWidth(MIN_WIDTH)
        .doStandalone();
  }

  private List<PathCommand> getCommandsFromString(String path) {
    String pathCommands = "AaCcHhLlMmQqSsTtVvZz";
    RegExp commandRegExp = RegExp.compile("(?=[" + pathCommands + "])");
    RegExp valueRegExp = RegExp.compile("(?=-)|[,\\s]");

    List<PathCommand> commands = new ArrayList<PathCommand>();
    SplitResult split = commandRegExp.split(path);
    for (int i = 0; i < split.length(); i++) {
      String rawCommand = split.get(i);
      SplitResult splitValues = valueRegExp.split(rawCommand.substring(1));
      char type = rawCommand.charAt(0);
      boolean relative = Character.isLowerCase(type);
      type = Character.toLowerCase(type);
      if (type == 'c') {
        commands.add(new CurveTo(Double.valueOf(splitValues.get(0)), Double.valueOf(splitValues.get(1)),
            Double.valueOf(splitValues.get(2)), Double.valueOf(splitValues.get(3)), Double.valueOf(splitValues.get(4)),
            Double.valueOf(splitValues.get(5)), relative));
      } else if (type == 'l') {
        commands.add(new LineTo(Double.valueOf(splitValues.get(0)), Double.valueOf(splitValues.get(1)), relative));
      } else if (type == 'm') {
        commands.add(new MoveTo(Double.valueOf(splitValues.get(0)), Double.valueOf(splitValues.get(1)), relative));
      } else if (type == 'z') {
        commands.add(new ClosePath(relative));
      } else if (type == 'a') {
        commands.add(new EllipticalArc(Double.valueOf(splitValues.get(0)), Double.valueOf(splitValues.get(1)),
            Double.valueOf(splitValues.get(2)), Integer.valueOf(splitValues.get(3)),
            Integer.valueOf(splitValues.get(4)), Double.valueOf(splitValues.get(5)),
            Double.valueOf(splitValues.get(6)), relative));
      } else if (type == 'h') {
        commands.add(new LineToHorizontal(Double.valueOf(splitValues.get(0)), relative));
      } else if (type == 'v') {
        commands.add(new LineToVertical(Double.valueOf(splitValues.get(0)), relative));
      } else if (type == 'q') {
        commands.add(new CurveToQuadratic(Double.valueOf(splitValues.get(0)), Double.valueOf(splitValues.get(1)),
            Double.valueOf(splitValues.get(2)), Double.valueOf(splitValues.get(3)), relative));
      } else if (type == 's') {
        commands.add(new CurveToSmooth(Double.valueOf(splitValues.get(0)), Double.valueOf(splitValues.get(1)),
            Double.valueOf(splitValues.get(2)), Double.valueOf(splitValues.get(3)), relative));
      } else if (type == 't') {
        commands.add(new CurveToQuadraticSmooth(Double.valueOf(splitValues.get(0)), Double.valueOf(splitValues.get(1)),
            relative));
      }
    }

    return commands;
  }

}
