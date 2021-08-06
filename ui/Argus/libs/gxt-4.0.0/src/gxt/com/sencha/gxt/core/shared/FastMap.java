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
package com.sencha.gxt.core.shared;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

public class FastMap<V> extends AbstractMap<String, V> implements Serializable {
  private static class FastMapEntry<V> implements Map.Entry<String, V> {

    private String key;
    private V value;

    FastMapEntry(String key, V value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public boolean equals(Object a) {
      if (a instanceof Map.Entry<?, ?>) {
        Map.Entry<?, ?> s = (Map.Entry<?, ?>) a;
        if (equalsWithNullCheck(key, s.getKey()) && equalsWithNullCheck(value, s.getValue())) {
          return true;
        }
      }
      return false;
    }

    public String getKey() {
      return key;
    }

    public V getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      int keyHash = 0;
      int valueHash = 0;
      if (key != null) {
        keyHash = key.hashCode();
      }
      if (value != null) {
        valueHash = value.hashCode();
      }
      return keyHash ^ valueHash;
    }

    public V setValue(V object) {
      V old = value;
      value = object;
      return old;
    }

    private boolean equalsWithNullCheck(Object obj1, Object obj2) {
      return equalWithNull(obj1, obj2);
    }
  }
  
  private static boolean equalWithNull(Object obj1, Object obj2) {
    if (obj1 == obj2) {
      return true;
    } else if (obj1 == null) {
      return false;
    } else {
      return obj1.equals(obj2);
    }
  }

  private static class JsMap<V> extends JavaScriptObject {

    public static FastMap.JsMap<?> create() {
      return JavaScriptObject.createObject().cast();
    }

    protected JsMap() {
    }

    public final native boolean containsKey(String key)/*-{
      return this.hasOwnProperty(key);
    }-*/;

    public final native V get(String key) /*-{
      return this[key];
    }-*/;

    public final native List<String> keySet() /*-{
      var s = @java.util.ArrayList::new()();
      for(var key in this) {
      if (!this.hasOwnProperty(key)) continue;
      s.@java.util.ArrayList::add(Ljava/lang/Object;)(key);
      }
      return s;
    }-*/;

    public final native V put(String key, V value) /*-{
      var previous = this[key];
      this[key] = value;
      return previous;
    }-*/;

    public final native V remove(String key) /*-{
      var previous = this[key];
      delete this[key];
      return previous;
    }-*/;

    public final native int size() /*-{
      var count = 0;
      for(var key in this) {
      if (this.hasOwnProperty(key)) ++count;
      }
      return count;
    }-*/;

    public final native List<V> values() /*-{
      var s = @java.util.ArrayList::new()();
      for(var key in this) {
      if (!this.hasOwnProperty(key)) continue;
      s.@java.util.ArrayList::add(Ljava/lang/Object;)(this[key]);
      }
      return s;
    }-*/;
  }

  private transient HashMap<String, V> javaMap;
  private transient FastMap.JsMap<V> map;

  public FastMap() {
    if (GWT.isScript()) {
      map = JsMap.create().cast();
    } else {
      javaMap = new HashMap<String, V>();
    }
  }

  @Override
  public void clear() {
    if (GWT.isScript()) {
      map = JsMap.create().cast();
    } else {
      javaMap.clear();
    }
  }

  @Override
  public boolean containsKey(Object key) {
    if (GWT.isScript()) {
      return map.containsKey(String.valueOf(key));
    } else {
      return javaMap.containsKey(key);
    }
  }

  @Override
  public boolean containsValue(Object value) {
    return values().contains(value);
  }

  @Override
  public Set<java.util.Map.Entry<String, V>> entrySet() {
    if (GWT.isScript()) {
      return new AbstractSet<Map.Entry<String, V>>() {

        @Override
        public boolean contains(Object key) {
          Map.Entry<?, ?> s = (Map.Entry<?, ?>) key;
          Object value = get(s.getKey());
          if (value == null) {
            return value == s.getValue();
          } else {
            return value.equals(s.getValue());
          }
        }

        @Override
        public Iterator<Map.Entry<String, V>> iterator() {

          Iterator<Map.Entry<String, V>> custom = new Iterator<Map.Entry<String, V>>() {
            Iterator<String> keys = keySet().iterator();

            public boolean hasNext() {
              return keys.hasNext();
            }

            public Map.Entry<String, V> next() {
              String key = keys.next();
              return new FastMapEntry<V>(key, get(key));
            }

            public void remove() {
              keys.remove();
            }
          };
          return custom;
        }

        @Override
        public int size() {
          return FastMap.this.size();
        }

      };
    } else {
      return javaMap.entrySet();
    }
  }

  @Override
  public V get(Object key) {
    if (GWT.isScript()) {
      return map.get(String.valueOf(key));
    } else {
      return javaMap.get(key);
    }
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public Set<String> keySet() {
    if (GWT.isScript()) {
      return new AbstractSet<String>() {
        @Override
        public boolean contains(Object key) {
          return FastMap.this.containsKey(key);
        }

        @Override
        public Iterator<String> iterator() {
          return map.keySet().iterator();
        }

        @Override
        public int size() {
          return FastMap.this.size();
        }
      };
    } else {
      return javaMap.keySet();
    }
  }

  @Override
  public V put(String key, V value) {
    if (GWT.isScript()) {
      return map.put(key, value);
    } else {
      return javaMap.put(key, value);
    }
  }

  @Override
  public void putAll(Map<? extends String, ? extends V> m) {
    if (GWT.isScript()) {
      for (String s : m.keySet()) {
        map.put(s, m.get(s));
      }
    } else {
      javaMap.putAll(m);
    }
  }

  @Override
  public V remove(Object key) {
    if (GWT.isScript()) {
      return map.remove((String) key);
    } else {
      return javaMap.remove(key);
    }
  }

  @Override
  public int size() {
    if (GWT.isScript()) {
      return map.size();
    } else {
      return javaMap.size();
    }
  }

  @Override
  public Collection<V> values() {
    if (GWT.isScript()) {
      return map.values();
    } else {
      return javaMap.values();
    }
  }
}
