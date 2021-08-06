package com.argus.shared;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface DbResultsTestCaseProperties extends PropertyAccess<DbResultsTestCase> {

	@Path("index")
	ModelKeyProvider<DbResultsTestCase> id();

	ValueProvider<DbResultsTestCase, Integer> index();
	ValueProvider<DbResultsTestCase, Integer> KEY();
	ValueProvider<DbResultsTestCase, Integer> run_key();
	ValueProvider<DbResultsTestCase, Date> run_start_date();
	ValueProvider<DbResultsTestCase, String> test_location();
	ValueProvider<DbResultsTestCase, String> test_name();
	ValueProvider<DbResultsTestCase, String> test_result();
	ValueProvider<DbResultsTestCase, String> test_error();
	//ValueProvider<DbResultsTestCase, String> bug_id();
	//ValueProvider<DbResultsTestCase, String> bug_state();
	ValueProvider<DbResultsTestCase, String> setup_address();
	ValueProvider<DbResultsTestCase, String> setup_ports();
	ValueProvider<DbResultsTestCase, String> test_duration();
	ValueProvider<DbResultsTestCase, String> test_client_address();
	ValueProvider<DbResultsTestCase, String> test_client_port();
	ValueProvider<DbResultsTestCase, String> test_topology();
	ValueProvider<DbResultsTestCase, String> test_hash_code();
	ValueProvider<DbResultsTestCase, String> suite();
	ValueProvider<DbResultsTestCase, String> ports_type();
	ValueProvider<DbResultsTestCase, String> setup_type();
	ValueProvider<DbResultsTestCase, Date> test_start_time();
	ValueProvider<DbResultsTestCase, String> setup_sn();
	ValueProvider<DbResultsTestCase, String> tc_management();
	ValueProvider<DbResultsTestCase, String> test_aggregated_area();
	ValueProvider<DbResultsTestCase, String> run_owner();
	ValueProvider<DbResultsTestCase, HashMap<String,String>> software_builds();

}
