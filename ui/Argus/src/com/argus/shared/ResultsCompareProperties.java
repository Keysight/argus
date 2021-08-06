package com.argus.shared;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ResultsCompareProperties extends PropertyAccess<ResultsCompare> {
	
	@Path("id")
	ModelKeyProvider<ResultsCompare> id();
	ValueProvider<ResultsCompare, String> test_name();
	ValueProvider<ResultsCompare, String> test_location();
}