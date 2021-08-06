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

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Assists a {@link Generator} in building common types that may be shared
 * across several generation runs.
 * 
 * 
 */
public abstract class AbstractCreator {
  private final GeneratorContext context;
  private final TreeLogger logger;

  public AbstractCreator(GeneratorContext ctx, TreeLogger l) {
    this.context = ctx;
    this.logger = l.branch(Type.DEBUG, "Running " + this.getClass());
  }

  public final String create() throws UnableToCompleteException {

    PrintWriter pw = context.tryCreate(getLogger(), getPackageName(), getSimpleName());
    if (pw == null) {
      // someone else already generated type, no need to change it
      return getPackageName() + "." + getSimpleName();
    }

    ClassSourceFileComposerFactory factory = new ClassSourceFileComposerFactory(getPackageName(), getSimpleName());
    configureFactory(factory);

    SourceWriter sw = factory.createSourceWriter(getContext(), pw);
    create(sw);

    sw.commit(getLogger());
    return factory.getCreatedClassName();
  }

  public GeneratorContext getContext() {
    return context;
  }

  /**
   * Gets a Java expression to create an instance of this type. May be a
   * singleton or a new instance, depending on the implementation.
   * 
   * @return the instance expression
   */
  public abstract String getInstanceExpression();

  public TreeLogger getLogger() {
    return logger;
  }

  /**
   * Builds up the basics of the factory that will generate source. Should be
   * overridden to add imports.
   * 
   * @param factory the factory to configure
   */
  protected void configureFactory(ClassSourceFileComposerFactory factory) throws UnableToCompleteException {
    JClassType t = getSupertype();
    if (t.isInterface() != null) {
      factory.addImplementedInterface(getSupertype().getParameterizedQualifiedSourceName());
    } else {
      if (t.isClass() == null) {
        logger.log(Type.ERROR, "Cannot create a subtype of a non-class and non-interface type: " + t.getName());
        throw new UnableToCompleteException();
      }
      if (t.isFinal()) {
        logger.log(Type.ERROR, "Cannot create a subtype of a final class");
        throw new UnableToCompleteException();
      }
      factory.setSuperclass(t.getQualifiedSourceName());
    }
  }

  /**
   * Writes the body of the created class to the given source writer.
   * 
   * @param sw the source writer
   * @throws UnableToCompleteException if class cannot be written
   */
  protected abstract void create(SourceWriter sw) throws UnableToCompleteException;

  /**
   * Gets the name of the package to create. Subclasses are responsible for ensuring
   * that the package they use is allowed to be written to.
   */
  protected abstract String getPackageName();

  protected abstract String getSimpleName();

  /**
   * Gets the declared type that this is providing, typically made available as
   * the return type of an interface method.
   * 
   * @return the declared type
   */
  protected abstract JClassType getSupertype();

}
