package com.argus.shared;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface DbResultsSuiteProperties extends PropertyAccess<DbResultsSuite> {

	@Path("id")
	ModelKeyProvider<DbResultsSuite> id();

	ValueProvider<DbResultsSuite, String>  suite();
	ValueProvider<DbResultsSuite, Integer> total();
	ValueProvider<DbResultsSuite, Integer> pass();
	ValueProvider<DbResultsSuite, Integer> fail();
	ValueProvider<DbResultsSuite, Integer> skip();
	ValueProvider<DbResultsSuite, Integer> pass_percent();
	ValueProvider<DbResultsSuite, Integer> fail_percent();
	ValueProvider<DbResultsSuite, Integer> skip_percent();
	ValueProvider<DbResultsSuite, String>  run_duration();

}
