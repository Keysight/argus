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
package com.sencha.gxt.explorer.rebind.model;

import com.google.gwt.core.ext.typeinfo.JClassType;

public class SourceModel {
  public static enum FileType {
    JAVA, CSS, XML, JSON, HTML, FOLDER
  }

  private final String name;
  private final FileType type;
  private final String url;

  private String path;
  private JClassType jclassType;

  public SourceModel(JClassType type) {
    this.name = type.getSimpleSourceName() + ".java";
    this.type = FileType.JAVA;
    this.url = "code/" + type.getQualifiedSourceName() + ".html";

    this.jclassType = type;
  }

  public SourceModel(String path) {
    this.name = path.substring(path.lastIndexOf('/') + 1);
    this.type = path.endsWith("css") ? FileType.CSS : (path.endsWith("xml") ? FileType.XML : (path.endsWith("html") ? FileType.HTML: FileType.JSON));
    this.url = "code/" + path.replace('/', '.') + ".html";

    this.path = path;
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public FileType getType() {
    return type;
  }

  public String getPath() {
    assert type != FileType.JAVA;
    return path;
  }

  public JClassType getJClassType() {
    assert type == FileType.JAVA;
    return jclassType;
  }
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SourceModel) {
      SourceModel other = (SourceModel) obj;
      if (other.type == type && url.equals(other.url)) {
        return true;
      }
    }
    return false;
  }
  @Override
  public int hashCode() {
    return url.hashCode();
  }
}
