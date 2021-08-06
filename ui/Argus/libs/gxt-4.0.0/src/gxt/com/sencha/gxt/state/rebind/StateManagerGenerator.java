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
package com.sencha.gxt.state.rebind;

import java.io.PrintWriter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.util.Name;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sencha.gxt.state.client.StateManager;

public class StateManagerGenerator extends Generator {
  public static final String STATE_MANAGER_ABF = "GXT.state.autoBeanFactory";
  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
    TypeOracle oracle = context.getTypeOracle();

    JClassType type = oracle.findType(typeName);
    JClassType stateMangerType = oracle.findType(Name.getSourceNameForClass(StateManager.class));
    if (type == null || type.isClass() == null || !type.isAssignableTo(stateMangerType)) {
      logger.log(Type.ERROR, "This generator only can function on StateManager subtypes");
      throw new UnableToCompleteException();
    }

    String abf;
    try {
      abf = context.getPropertyOracle().getConfigurationProperty(STATE_MANAGER_ABF).getValues().get(0);
    } catch (BadPropertyValueException ex) {
      logger.log(Type.ERROR, "Could not read property for " + STATE_MANAGER_ABF, ex);
      throw new UnableToCompleteException();
    }

    JClassType abfType = oracle.findType(abf);
    if (abfType == null) {
      logger.log(Type.ERROR, "Cannot find type " + abf + " in gwt classpath");
      throw new UnableToCompleteException();
    }

    String packageName = abfType.getPackage().getName();
    String simpleSourceName = "StateManagerImpl_" + abfType.getName().replace('.', '_');
    PrintWriter pw = context.tryCreate(logger, packageName, simpleSourceName);
    if (pw == null) {
      return packageName + "." + simpleSourceName;
    }

    ClassSourceFileComposerFactory factory = new ClassSourceFileComposerFactory(packageName, simpleSourceName);
    factory.setSuperclass(typeName);
    factory.addImport(Name.getSourceNameForClass(GWT.class));

    SourceWriter sw = factory.createSourceWriter(context, pw);

    sw.println("public %1$s getStateBeanFactory() {", abf);
    sw.indentln("return GWT.create(%1$s.class);", abf);
    sw.println("}");

    sw.commit(logger);

    return factory.getCreatedClassName();
  }

}
