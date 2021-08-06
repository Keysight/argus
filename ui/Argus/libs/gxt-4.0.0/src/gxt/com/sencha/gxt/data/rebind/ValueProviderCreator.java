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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JGenericType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.dev.util.Name;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.editor.rebind.model.ModelUtils;
import com.google.gwt.thirdparty.guava.common.base.Joiner;
import com.google.gwt.user.rebind.SourceWriter;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.rebind.AbstractCreator;

public class ValueProviderCreator extends AbstractCreator {
  public enum RequiredReadability {
    BOTH, SET, GET, NEITHER
  }

  protected static String cap(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  private static List<String> generatePath(Path annotation, String name) {
    if (annotation == null) {
      return Collections.singletonList(name);
    }
    return Arrays.asList(annotation.value().split("\\."));
  }

  private final JClassType supertypeToImplement;

  protected final List<String> path;

  private final JGenericType valueProviderInterface = getContext().getTypeOracle().findType(
      Name.getSourceNameForClass(ValueProvider.class)).isGenericType();

  private RequiredReadability readability = RequiredReadability.NEITHER;

  public ValueProviderCreator(GeneratorContext ctx, TreeLogger l, JMethod valueProviderMethodDecl) {
    super(ctx, l);
    this.supertypeToImplement = valueProviderMethodDecl.getReturnType().isClassOrInterface();
    this.path = generatePath(valueProviderMethodDecl.getAnnotation(Path.class), valueProviderMethodDecl.getName());
  }

  public ValueProviderCreator(GeneratorContext ctx, TreeLogger l, List<String> path, JClassType baseType,
      JClassType valueType) {
    super(ctx, l);
    this.path = path;
    supertypeToImplement = getContext().getTypeOracle().getParameterizedType(valueProviderInterface,
        new JClassType[] {baseType, valueType});
  }

  @Override
  public String getInstanceExpression() {
    return getPackageName() + "." + getSimpleName() + ".INSTANCE";
  }

  public void setReadability(RequiredReadability readability) {
    this.readability = readability;
  }

  public RequiredReadability getReadability() {
    return readability;
  }

  @Override
  protected void create(SourceWriter sw) throws UnableToCompleteException {
    sw.println("public static final %1$s INSTANCE = new %1$s();", getSimpleName());
    // @Override
    sw.println("public %1$s getValue(%2$s object) {", getValueTypeName(), getObjectTypeName());
    appendGetterBody(sw, "object");
    sw.println("}");

    // @Override
    sw.println("public void setValue(%1$s object, %2$s value) {", getObjectTypeName(), getValueTypeName());
    appendSetterBody(sw, "object", "value");
    sw.println("}");

    // @Override
    sw.println("public String getPath() {");
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String p : path) {
      if (!first) {
        sb.append(".");
      }
      sb.append(p);

      first = false;
    }
    sw.indentln("return \"%1$s\";", sb.toString());
    sw.println("}");
  }

  protected void appendGetterBody(SourceWriter sw, String objectName)
      throws UnableToCompleteException {
    String getter = getGetterExpression(objectName);
    if (getter == null) {
      if (readability == RequiredReadability.NEITHER || readability == RequiredReadability.SET) {
        // getter is not required, but log it if someone tries to call it
        getLogger().log(
            Type.DEBUG,
            "Getter could not be found (and apparently not needed), writting a log message to indicate that this is probably an error.");
        sw.indentln("com.google.gwt.core.client.GWT.log(\"Getter was called on " + supertypeToImplement.getName()
            + ", but no getter exists.\", new RuntimeException());");
        sw.indentln("return null;");
      } else {
        getLogger().log(Type.ERROR, "No getter can be found, unable to proceed");
        throw new UnableToCompleteException();
      }
    } else {
      sw.indentln("return %1$s;", getter);
    }
  }

  protected void appendSetterBody(SourceWriter sw, String objectName, String valueName) throws UnableToCompleteException {
    String setter = getSetterExpression(objectName, valueName);
    if (setter == null) {
      if (readability == RequiredReadability.NEITHER || readability == RequiredReadability.GET) {
        // setter is not required, but log it if someone tries to call it
        getLogger().log(
            Type.DEBUG,
            "Setter could not be found (and apparently not needed), writing a log message to indicate that this is probably an error.");
        sw.indentln("com.google.gwt.core.client.GWT.log(\"Setter was called on " + supertypeToImplement.getName()
            + ", but no setter exists.\", new RuntimeException());");
      } else {
        getLogger().log(Type.ERROR, "No setter can be found, unable to proceed");
        throw new UnableToCompleteException();
      }
    } else {
      sw.indentln("%1$s;", setter);
    }
  }

  protected String getGetterExpression(String objectName) throws UnableToCompleteException {
    StringBuilder sb = new StringBuilder(objectName);
    try {
      getGetterHelper(path, sb);
    } catch (NoSuchMethodException ex) {
      return null;
    }
    return sb.toString();
  }

  protected JMethod getMethod(JClassType type, String methodName) {
    JMethod[] methods = type.getInheritableMethods();
    for (JMethod m : methods) {
      if (m.getName().equals(methodName)) {
        return m;
      }
    }
    return null;
  }

  /**
   * Gets the type of the Model this object should provide data for.
   * 
   * @return the parameterized type of the model
   */
  protected JClassType getObjectType() {
    JClassType[] params = ModelUtils.findParameterizationOf(valueProviderInterface, supertypeToImplement);

    if (params[0].isTypeParameter() != null) {
      return params[0].isTypeParameter().getBaseType();
    } else {
      return params[0];
    }
  }

  /**
   * Evaluated when used so this can be subclassed, and used for generating
   * similar types
   * 
   * @return the parameterized qualified name
   */
  protected final String getObjectTypeName() {
    return getObjectType().getParameterizedQualifiedSourceName();
  }

  @Override
  protected String getPackageName() {
    String pak = getObjectType().getPackage().getName();
    if (!pak.startsWith("java.") && !pak.startsWith("javax.")) {
      return pak;
    }
    //if this does start with java, we can just drop this anywhere, since java isn't likely
    //to not be on the classpath...
    return "com.sencha.gxt.core.client." + pak;
  }

  @Override
  protected String getSimpleName() {
    return getObjectType().getName().replace('.', '_') + "_" + Joiner.on("_").join(path) + "_ValueProviderImpl";
  }

  @Override
  protected JClassType getSupertype() {
    return supertypeToImplement;
  }

  /**
   * Helper method for building up chained getter expressions
   * 
   * @param path the path to follow to find the next getter
   * @param sb stringbuild to append the methods to
   * @return the type returned of the last method
   * @throws NoSuchMethodException if a method cannot be found
   */
  private JClassType getGetterHelper(List<String> path, StringBuilder sb) throws NoSuchMethodException, UnableToCompleteException {
    JClassType type = getObjectType();
    for (String p : path) {
      if (type == null) {
        getLogger().log(Type.WARN, "Trying to find a method in a non-class, non-interface type.");
        throw new UnableToCompleteException();
      }

      // Pick a method to use
      // Try getter first
      JMethod method = getMethod(type, p);
      if (method == null) {
        method = getMethod(type, "get" + cap(p));
      }
      if (method == null) {
        method = getMethod(type, "is" + cap(p));
      }
      if (method == null) {
        method = getMethod(type, "has" + cap(p));
      }

      // Fall back to field
      JField field = getField(type, p);

      if (method != null) {
        sb.append(".").append(method.getName()).append("()");
        type = method.getReturnType().isClassOrInterface();
      } else if (field != null) {
        sb.append(".").append(p);
        type = field.getType().isClassOrInterface();
      } else {
        getLogger().log(Type.WARN, "Method get" + cap(p) + " could not be found");
        throw new NoSuchMethodException();
      }

    }
    return type;
  }

  private JField getField(JClassType type, String p) {
    for (JClassType superType : type.getFlattenedSupertypeHierarchy()) {
      JField field = superType.findField(p);
      if (field != null && field.isPublic()){
        return field;
      }
    }
    return null;
  }

  private String getSetterExpression(String objectName, String valueName) throws UnableToCompleteException {
    StringBuilder sb = new StringBuilder(objectName);

    // find the getter from the start of the path
    List<String> getterPath = this.path.subList(0, this.path.size() - 1);
    String lastPathElement = path.get(path.size() - 1);

    JClassType type = null;
    try {
      type = getGetterHelper(getterPath, sb);
    } catch (NoSuchMethodException ex) {
      return null;
    }
    if (type == null) {
      getLogger().log(Type.WARN, "Trying to find setter method on a non-class, non-interface type.");
      return null;
    }

    String methodName = "set" + cap(lastPathElement);

    JMethod method = getMethod(type, methodName);
    JField field = getField(type, lastPathElement);
    if (method != null && typesMatch(getValueType(), method.getParameterTypes()[0])) {
      sb.append(".").append(methodName).append("(").append(valueName).append(")");
    } else if (field != null && typesMatch(getValueType(), field.getType())) {
      sb.append(".").append(lastPathElement).append(" = ").append(valueName);
    } else {
      getLogger().log(Type.DEBUG, "Method " + methodName + " of type " + getValueType() + " could not be found.");
      return null;
    }

    return sb.toString();
  }

  private boolean typesMatch(JClassType a, JType b) {
    if (b.isPrimitive() != null) {
      return a.getQualifiedSourceName().equals(b.isPrimitive().getQualifiedBoxedSourceName());
    } else {
      assert b.isClassOrInterface() != null;

      return b.isClassOrInterface().isAssignableTo(a);
    }
  }

  private JClassType getValueType() {
    JClassType[] params = ModelUtils.findParameterizationOf(valueProviderInterface, supertypeToImplement);

    return params[1];
  }

  private String getValueTypeName() {
    return getValueType().getParameterizedQualifiedSourceName();
  }
}
