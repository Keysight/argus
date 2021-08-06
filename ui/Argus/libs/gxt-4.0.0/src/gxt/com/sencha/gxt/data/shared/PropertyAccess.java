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
package com.sencha.gxt.data.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;

/**
 * Marker Interface implemented by types that provide access to properties of 
 * bean-like models. Methods can be of type {@link ValueProvider},
 * {@link ModelKeyProvider}, or {@link LabelProvider}, and in all cases, the type
 * T should match the generic type for PropertyAccess&lt;T>.
 * <p>
 * Method names should map to existing model properties, except for the get- and
 * set- prefix that is used to change those properties. Much as with the GWT
 * Editor functionality, nested properties or properties with names unrelated
 * to their method may also be defined using the {@link Path} annotation.
 * </p>
 * <p>
 * {@code PropertyAccess} instances are created by invoking {@link GWT#create(Class)}.
 * </p>
 * <p>
 * In the following example, an interface is declared with several getters and setters - it doesn't
 * matter to {@code PropertyAccess} how these are implemented - and generic access to these methods
 * are generated in the {@code MyDataProperties} implementation automatically.
 * </p>
 * <pre><code>
public interface MyData {
  String getName();
  void setName(String name);
  
  String getId();
  void setId(String id);
  
  MyData getMoreData();
  void setMoreData(MyData moreData);
}
public interface MyDataProperties extends PropertyAccess&lt;MyData> {
  ValueProvider&lt;MyData, String> name();// declaring a way to read/write the name property
  
  {@literal @}Path("name")
  LabelProvider&lt;MyData> nameLabel();   // if name() didn't exist, this could be called name and would
                                       // not need {@literal @}Path("name")
  ModelKeyProvider&lt;MyData> id();
  
  {@literal @}Path("moreData.name")
  ValueProvider&lt;MyData,String> moreDataName(); // provides access to .getMoreData().getName()
}
</code></pre>
 * 
 * @param <T> the target type
 */
public interface PropertyAccess<T> {

}
