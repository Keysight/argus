package com.argus.shared;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface DbResultsAggregatedProperties extends PropertyAccess<DbResultsAggregated> {

	@Path("index")
	ModelKeyProvider<DbResultsAggregated> id();
	
	ValueProvider<DbResultsAggregated, Integer> index();
	ValueProvider<DbResultsAggregated, Integer> KEY();
	ValueProvider<DbResultsAggregated, Date>    run_start_date();
	ValueProvider<DbResultsAggregated, Date>    run_end_date();
	ValueProvider<DbResultsAggregated, String>  run_name();
	ValueProvider<DbResultsAggregated, Integer> total_count_end();
	ValueProvider<DbResultsAggregated, Integer> total_count_now();
	ValueProvider<DbResultsAggregated, Integer> pass();
	ValueProvider<DbResultsAggregated, Integer> fail();
	ValueProvider<DbResultsAggregated, Integer> skip();
	ValueProvider<DbResultsAggregated, Double>  pass_percent();
	ValueProvider<DbResultsAggregated, Double>  fail_percent();
	ValueProvider<DbResultsAggregated, Double>  skip_percent();
	ValueProvider<DbResultsAggregated, String>  run_duration();
	ValueProvider<DbResultsAggregated, String>  run_comment();
	ValueProvider<DbResultsAggregated, HashMap<String, String>>  software_builds();
	
}
