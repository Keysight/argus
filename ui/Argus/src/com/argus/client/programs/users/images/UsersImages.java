/**
 * Sencha GXT 3.0.0 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.argus.client.programs.users.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface UsersImages extends ClientBundle {
  public UsersImages INSTANCE = GWT.create(UsersImages.class);

  ImageResource user_male_48();

}
