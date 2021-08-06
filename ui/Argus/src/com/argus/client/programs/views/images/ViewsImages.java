package com.argus.client.programs.views.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ViewsImages extends ClientBundle {
	public ViewsImages INSTANCE = GWT.create(ViewsImages.class);

	ImageResource bar_chart_16();
	ImageResource compare_16();
	ImageResource delete_16();
	ImageResource hide_16();
	ImageResource xml_16();
	ImageResource ixtracker_16();
	ImageResource layers_16();
}
