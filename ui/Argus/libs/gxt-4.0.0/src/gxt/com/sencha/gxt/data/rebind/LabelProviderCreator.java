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
package com.sencha.gxt.data.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.dev.util.Name;
import com.google.gwt.editor.rebind.model.ModelUtils;
import com.google.gwt.thirdparty.guava.common.base.Joiner;
import com.google.gwt.user.rebind.SourceWriter;
import com.sencha.gxt.data.shared.LabelProvider;

public class LabelProviderCreator extends ValueProviderCreator {

  private final JClassType labelProviderInterface;
  public LabelProviderCreator(GeneratorContext ctx, TreeLogger l, JMethod labelProviderMethodDecl) {
    super(ctx, l, labelProviderMethodDecl);
    labelProviderInterface = ctx.getTypeOracle().findType(Name.getSourceNameForClass(LabelProvider.class));
    setReadability(RequiredReadability.GET);
  }

  @Override
  protected JClassType getObjectType() {
    JClassType[] params = ModelUtils.findParameterizationOf(labelProviderInterface, getSupertype());

    return params[0];
  }

  @Override
  protected void create(SourceWriter sw) throws UnableToCompleteException {
    sw.println("public static final %1$s INSTANCE = new %1$s();", getSimpleName());

    // @Override
    sw.println("public String getLabel(%1$s item) {", getObjectTypeName());

    appendGetterBody(sw, "item");

    sw.println("}");
  }

  @Override
  protected String getGetterExpression(String objectName) throws UnableToCompleteException {
    return "\"\" + " + super.getGetterExpression(objectName);
  }

  @Override
  protected String getSimpleName() {
    return getObjectType().getName().replace('.', '_') + "_" + Joiner.on("_").join(path) + "_ModelLabelProviderImpl";
  }

}
