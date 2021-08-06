package com.argus.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface DesktopImages extends ClientBundle {
	public DesktopImages INSTANCE = GWT.create(DesktopImages.class);

	ImageResource log_16();
	ImageResource user();
	ImageResource user_group();
	ImageResource results_detailed_16();
	ImageResource xls_16();
	ImageResource csv_16();
	ImageResource zip_16();
	ImageResource XLSX_20();
	ImageResource execution_16();
	ImageResource reload();
	ImageResource save_as20();
	ImageResource logger_16();
	ImageResource tcl();
	ImageResource python();
	ImageResource perl();
	ImageResource question_blue();
	ImageResource delete();
}

