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
package com.sencha.gxt.core.rebind;

import java.io.PrintWriter;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.SelectionProperty;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.util.Name;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sencha.gxt.core.client.BindingPropertySet.PropertyName;
import com.sencha.gxt.core.client.BindingPropertySet.PropertyValue;

public class BindingPropertyGenerator extends Generator {

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
    TypeOracle oracle = context.getTypeOracle();

    JClassType toGenerate = oracle.findType(typeName).isInterface();
    if (toGenerate == null) {
      logger.log(Type.ERROR, typeName + " is not an interface");
      throw new UnableToCompleteException();
    }

    PropertyName annotation = toGenerate.getAnnotation(PropertyName.class);
    if (annotation == null) {
      logger.log(Type.ERROR, "Cannot generate with a @PropertyName anntation on the type");
      throw new UnableToCompleteException();
    }

    String propertyName = annotation.value();
    SelectionProperty property;
    String value;
    try {
      property = context.getPropertyOracle().getSelectionProperty(logger, propertyName);
      value = property.getCurrentValue();
    } catch (BadPropertyValueException e) {
      logger.log(Type.ERROR, "Error occured loading property: ", e);
      throw new UnableToCompleteException();
    }
    String packageName = toGenerate.getPackage().getName();
    String simpleSourceName = toGenerate.getName().replace('.', '_') + "_" + value;
    PrintWriter pw = context.tryCreate(logger, packageName, simpleSourceName);
    if (pw == null) {
      return packageName + "." + simpleSourceName;
    }

    ClassSourceFileComposerFactory factory = new ClassSourceFileComposerFactory(packageName, simpleSourceName);
    factory.addImplementedInterface(typeName);
    SourceWriter sw = factory.createSourceWriter(context, pw);

    for (JMethod method : toGenerate.getMethods()) {
      if (method.getReturnType().isPrimitive() != JPrimitiveType.BOOLEAN
          && !method.getReturnType().isClass().getQualifiedSourceName().equals(
              Name.getSourceNameForClass(Boolean.class))) {
        logger.log(Type.ERROR, "Methods must return boolean or Boolean");
        throw new UnableToCompleteException();
      }
      sw.println("%1$s {", method.getReadableDeclaration(false, true, true, true, true));

      PropertyValue val = method.getAnnotation(PropertyValue.class);
      if (val == null) {
        logger.log(Type.ERROR, "Method must have a @PropertyValue annotation");
        throw new UnableToCompleteException();
      }

      if (!property.getPossibleValues().contains(val.value()) && val.warn()) {
        logger.log(Type.WARN, "Value '" + val
            + "' is not present in the current set of possible values for selection property " + propertyName);
      }
      sw.indentln("return %1$b;", val.value().equals(value));

      sw.println("}");
    }

    sw.commit(logger);

    return factory.getCreatedClassName();
  }

}
