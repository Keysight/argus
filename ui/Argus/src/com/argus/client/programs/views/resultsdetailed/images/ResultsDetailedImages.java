package com.argus.client.programs.views.resultsdetailed.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ResultsDetailedImages extends ClientBundle {
	public ResultsDetailedImages INSTANCE = GWT.create(ResultsDetailedImages.class);

	ImageResource bar_chart_16();
	ImageResource xml_16();
	ImageResource download_16();
}
