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
package com.sencha.gxt.explorer.rebind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.resource.Resource;
import com.google.gwt.dev.util.Name;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.util.tools.Utility;
import com.sencha.gxt.explorer.client.model.Category;
import com.sencha.gxt.explorer.client.model.Example;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.explorer.client.model.Source;
import com.sencha.gxt.explorer.rebind.model.ExampleDetailModel;
import com.sencha.gxt.explorer.rebind.model.SourceModel;
import com.sencha.gxt.explorer.rebind.model.SourceModel.FileType;

public class SampleGenerator extends Generator {

  private String javaHeader;
  // cssheader, xmlheader, etc
  private String footer;

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
    // Get access to metadata about the type to be generated
    TypeOracle oracle = context.getTypeOracle();
    JClassType toGenerate = oracle.findType(typeName).isClass();

    // Get the name of the new type
    String packageName = toGenerate.getPackage().getName();
    String simpleSourceName = toGenerate.getName().replace('.', '_') + "Impl";
    PrintWriter pw = context.tryCreate(logger, packageName, simpleSourceName);
    if (pw == null) {
      return packageName + "." + simpleSourceName;
    }

    // Generate an HTML file resource for every example and write the source
    JClassType[] types = oracle.getTypes();

    // Load the header and footer HTML content
    try {
      String slashyPackageName = getClass().getPackage().getName().replace('.', '/');
      javaHeader = Utility.getFileFromClassPath(slashyPackageName + "/header.html");
      footer = Utility.getFileFromClassPath(slashyPackageName + "/footer.html");
    } catch (IOException e) {
      logger.log(Type.ERROR, "Header or Footer failed to be read", e);
      throw new UnableToCompleteException();
    }

    // Find all examples, annotated with @Detail
    Set<ExampleDetailModel> examples = new HashSet<ExampleDetailModel>();
    Map<String, List<ExampleDetailModel>> hierarchy = new HashMap<String, List<ExampleDetailModel>>();

    JClassType isWidget = oracle.findType(IsWidget.class.getName());
    JClassType entryPoint = oracle.findType(EntryPoint.class.getName());

    Set<SourceModel> exampleSources = new HashSet<SourceModel>();
    for (JClassType type : types) {
      Example.Detail detail = type.getAnnotation(Example.Detail.class);
      if (detail != null) {
        if (!type.isAssignableTo(isWidget)) {
          logger.log(Type.ERROR, "Example " + type + " is not an IsWidget");
          throw new UnableToCompleteException();
        }
        if (!type.isAssignableTo(entryPoint)) {
          logger.log(Type.ERROR, "Example " + type + " is not an EntryPoint");
          throw new UnableToCompleteException();
        }

        ExampleDetailModel example = new ExampleDetailModel(logger, context, type, detail);

        // Collect sources to be built into html
        exampleSources.addAll(example.getAllSources());

        List<ExampleDetailModel> exampleList = hierarchy.get(detail.category());
        if (exampleList == null) {
          exampleList = new ArrayList<ExampleDetailModel>();
          hierarchy.put(detail.category(), exampleList);
        }
        examples.add(example);
        exampleList.add(example);
      }
    }

    // Sort folders, sort within those folders
    List<String> folders = new ArrayList<String>(hierarchy.keySet());
    Collections.sort(folders);
    for (List<ExampleDetailModel> contents : hierarchy.values()) {
      Collections.sort(contents);
    }

    // Actually build source for each type
    for (SourceModel type : exampleSources) {
      TreeLogger l = logger.branch(Type.DEBUG, "Writing HTML file for " + type.getName());

      // attempt to create the output file
      if (type.getType() == FileType.JAVA) {
        writeTypeToHtml(l, context, type.getJClassType());
      } else {
        writeFileToHtml(l, context, type.getPath());
      }
    }

    // Start making the class, with basic imports
    ClassSourceFileComposerFactory factory = new ClassSourceFileComposerFactory(packageName, simpleSourceName);
    factory.setSuperclass(typeName);
    factory.addImport(Name.getSourceNameForClass(Category.class));
    factory.addImport(Name.getSourceNameForClass(ImageResource.class));
    factory.addImport(Name.getSourceNameForClass(GWT.class));
    factory.addImport(Name.getSourceNameForClass(Example.class));
    factory.addImport(Name.getSourceNameForClass(Source.class));
    factory.addImport(Name.getSourceNameForClass(Source.FileType.class));
    SourceWriter sw = factory.createSourceWriter(context, pw);

    // Write the ctor
    sw.println("public %1$s() {", simpleSourceName);
    sw.indent();
    // Declare variables that will be used
    sw.println("Category c;");
    sw.println("ImageResource icon;");
    sw.println("Example e;");
    sw.println("Source dir;");
    Set<String> names = new HashSet<String>();
    Map<JClassType, String> bundles = new HashMap<JClassType, String>();
    for (String folder : folders) {
      sw.println("c = new Category(\"%1$s\");", escape(folder));
      for (ExampleDetailModel example : hierarchy.get(folder)) {
        // make sure the bundle to be used exists
        if (!bundles.containsKey(example.getClientBundleType())) {
          String bundleName = getNextName("bundle", names);
          sw.println("%1$s %2$s = GWT.create(%1$s.class);", example.getClientBundleType().getQualifiedSourceName(),
              bundleName);

          bundles.put(example.getClientBundleType(), bundleName);
        }

        // write out the example, adding it to the current category
        writeExample(sw, bundles.get(example.getClientBundleType()), example);
      }
      sw.println("categories.add(c);");
    }
    sw.outdent();
    sw.println("}");// end ctor

    sw.commit(logger);

    return factory.getCreatedClassName();
  }



  protected void writeExample(SourceWriter sw, String bundleName, ExampleDetailModel example) {
    sw.println("icon = %1$s.%2$s();", bundleName, example.getIconMethodName());

    Detail detail = example.getExampleDetail();
    sw.println("e = new Example(\"%1$s\", icon, %2$f, %3$f, %4$f, %5$f, %6$f, %7$f, %8$f) {",
        escape(detail.name()),
        detail.minWidth(), detail.minHeight(),
        detail.maxWidth(), detail.maxHeight(),
        detail.preferredWidth(), detail.preferredHeight(), detail.preferredMargin());
    sw.indent();
    sw.println("protected %1$s createExample() { ", IsWidget.class.getName());
    sw.indentln("return new %1$s();", example.getExampleType().getQualifiedSourceName());
    sw.println(" }");
    sw.outdent();
    sw.println("};");

    sw.println("dir = new Source(\"Java\");");
    sw.println("e.getSources().add(dir);");
    for (SourceModel src : example.getJavaSources()) {
      sw.println("dir.addChild(new Source(\"%1$s\", GWT.getModuleBaseURL() + \"%2$s\", FileType.%3$s));",
              escape(src.getName()), escape(src.getUrl()), src.getType().name());
    }
    for (FileType type : FileType.values()) {
      if (type == FileType.JAVA || type == FileType.FOLDER) {
        continue;
      }
      List<SourceModel> srcs = example.getOtherSources(type);
      if (srcs.size() != 0) {
        sw.println("dir = new Source(\"%1$s\");", escape(type.name()));
        sw.println("e.getSources().add(dir);");

        for (SourceModel src : srcs) {
          sw.println("dir.addChild(new Source(\"%1$s\", GWT.getModuleBaseURL() + \"%2$s\", FileType.%3$s));",
                  escape(src.getName()), escape(src.getUrl()), src.getType().name());
        }
      }
    }
    sw.println("c.addExample(e);");
  }



  private String getNextName(String prefix, Set<String> names) {
    String name = prefix;
    if (names.contains(name)) {
      return name;
    }
    int suffix = 1;
    do {
      name = prefix + "_" + suffix;
    } while (names.contains(name));
    return name;
  }

  private void writeFileToHtml(TreeLogger l, GeneratorContext ctx, String path) throws UnableToCompleteException {
    Resource file = ctx.getResourcesOracle().getResource(path);
    if (file == null) {
      l.log(Type.ERROR, "File cannot be found.");
      throw new UnableToCompleteException();
    }
    OutputStream stream = ctx.tryCreateResource(l, "code/" + path.replace('/', '.') + ".html");
    if (stream == null) {
      // file already exists for this compile
      return;
    }
    try {
      InputStream input = file.openContents();
      byte[] bytes = new byte[input.available()];
      input.read(bytes);
      input.close();

      // Write out the HTML file
      // TODO change this header
      stream.write(javaHeader.getBytes());
      stream.write(bytes);
      stream.write(footer.getBytes());

      stream.close();

    } catch (Exception e) {
      l.log(Type.ERROR, "An error occured writing out a file into html", e);
      throw new UnableToCompleteException();
    }

    ctx.commitResource(l, stream);
  }

  /**
   * Writes out the given class/interface to an HTML file, using the current
   * header/footer strings
   *
   * @param l
   * @param ctx
   * @param type
   * @throws UnableToCompleteException
   */
  private void writeTypeToHtml(TreeLogger l, GeneratorContext ctx, JClassType type) throws UnableToCompleteException {
    assert type.isClassOrInterface() != null : "Can only generate source for classes or interfaces";
    OutputStream stream = ctx.tryCreateResource(l, "code/" + type.getQualifiedSourceName() + ".html");
    if (stream == null) {
      // file already exists for this compile
      return;
    }

    try {
      String name = type.getQualifiedSourceName().replace('.', '/') + ".java";
      l.log(Type.DEBUG, "Reading from " + name);

      InputStream input = getClass().getClassLoader().getResourceAsStream(name); //sourceOracle.getResourceMap().get(name).openContents();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));

      // Write out the HTML file
      stream.write(javaHeader.getBytes());
      String line;
      boolean skip = false;
      while (null != (line = reader.readLine())) {
        if (line.startsWith("@Detail") || line.startsWith("@Example.Detail")) {
          skip = true;
        } else if (skip) {
          if (line.startsWith("public")) {
            skip = false;
          }
        }
        if (!skip) {
          stream.write(line.getBytes());
          stream.write('\n');
        }
      }
      stream.write(footer.getBytes());

      stream.close();
    } catch (Exception e) {
      l.log(Type.ERROR, "Error occured writing out a java file into html", e);
      throw new UnableToCompleteException();
    }

    ctx.commitResource(l, stream);
  }
}
