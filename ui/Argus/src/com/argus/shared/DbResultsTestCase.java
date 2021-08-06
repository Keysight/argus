package com.argus.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DbResultsTestCase implements IsSerializable, DbSoftwareBuildsInterface {

	private Integer index;

	private Integer KEY;
	private Integer run_key;
	private Date run_start_date;
	private String test_location;
	private String test_name;
	private String test_result;
	private String test_error;
	private String setup_address;
	private String setup_ports;
	private String test_duration;
	private String test_client_address;
	private String test_client_port;
	private String test_topology;
	private String test_hash_code;
	private String suite;
	private String ports_type;
	private String setup_type;
	private Date test_start_time;
	private String setup_sn;
	private String tc_management;
	private String test_aggregated_area = "None";
	private int id;
	private HashMap<String,String> software_builds;
	private String run_owner;



	public DbResultsTestCase() {}

	public DbResultsTestCase(
			Integer index,
			Integer KEY, 
			Integer run_key, 
			Date run_start_date, 
			String test_location, 
			String test_name, 
			String test_result, 
			String test_error,
			//String bug_id, String bug_state, 
			String setup_address, 
			String setup_ports, 
			String test_duration, 
			String test_client_address, 
			String test_client_port, 
			String test_topology,
			String test_hash_code, 
			String suite, 
			String ports_type, 
			String setup_type, 
			Date test_start_time, 
			String setup_sn, 
			String tc_management, 
			HashMap<String,String> software_builds,
			String run_owner
			) 
	{
		this.setIndex(index);
		this.setKEY(KEY);
		this.setRun_key(run_key);
		this.setRun_start_date(run_start_date);
		this.setTest_location(test_location);
		this.setTest_name(test_name);
		this.setTest_result(test_result);
		this.setTest_error(test_error);
		//this.setBug_id(bug_id);
		//this.setBug_state(bug_state);
		this.setSetup_address(setup_address);
		this.setSetup_ports(setup_ports);
		this.setTest_duration(test_duration);
		this.setTest_client_address(test_client_address);
		this.setTest_topology(test_topology);
		this.setTest_client_port(test_client_port);
		this.setTest_hash_code(test_hash_code);
		this.setSuite(suite);
		this.setPorts_type(ports_type);
		this.setSetup_type(setup_type);
		this.setTest_start_time(test_start_time);
		this.setSetup_sn(setup_sn);
		this.setTc_management(tc_management);
		this.setSoftware_builds(software_builds);
		this.setRun_owner(run_owner);
	}


	public DbResultsTestCase(String test_location, String test_name) {
		this();
		this.setTest_location(test_location);
		this.setTest_name(test_name);
	}
	

	public DbResultsTestCase(String test_location, String test_name, String test_result) {
		this();
		this.setTest_location(test_location);
		this.setTest_name(test_name);
		this.setTest_result(test_result);
	}

	public Collection<String> getPropertyNames() {

		Collection<String> propertyNames =  new ArrayList<String>();

		propertyNames.add("KEY");
		propertyNames.add("run_key");
		propertyNames.add("run_start_date");
		propertyNames.add("test_location");
		propertyNames.add("test_name");
		propertyNames.add("test_result");
		propertyNames.add("test_error");
		//String bug_id, String bug_state, 
		propertyNames.add("setup_address");
		propertyNames.add("setup_ports");
		propertyNames.add("test_duration");
		propertyNames.add("test_client_address");
		propertyNames.add("test_client_port");
		propertyNames.add("test_topology");
		propertyNames.add("test_hash_code");
		propertyNames.add("suite");
		propertyNames.add("ports_type");
		propertyNames.add("setup_type");
		propertyNames.add("test_start_time");
		propertyNames.add("setup_sn");
		propertyNames.add("tc_management");
		propertyNames.add("software_builds");
		propertyNames.add("run_owner");


		return propertyNames;
	}

	public String getProperty(String aPropertyName) {
		if        (aPropertyName.equals("KEY")) {
			return Objects.toString(this.getKEY());
		} else if (aPropertyName.equals("run_key")) {
			return Objects.toString(this.getRun_key());
		} else if (aPropertyName.equals("run_start_date")) {
			return Objects.toString(this.getRun_start_date());
		} else if (aPropertyName.equals("test_location")) {
			return Objects.toString(this.getTest_location());
		} else if (aPropertyName.equals("test_name")) {
			return Objects.toString(this.getTest_name());
		} else if (aPropertyName.equals("test_result")) {
			return Objects.toString(this.getTest_result());
		} else if (aPropertyName.equals("test_error")) {
			return Objects.toString(this.getTest_error());
		} else if (aPropertyName.equals("bug_id")) {
			//return Objects.toString(this.getBug_id());
		} else if (aPropertyName.equals("bug_state")) {
			//return Objects.toString(this.getBug_state());
		} else if (aPropertyName.equals("setup_address")) {
			return Objects.toString(this.getSetup_address());
		} else if (aPropertyName.equals("setup_ports")) {
			return Objects.toString(this.getSetup_ports());
		} else if (aPropertyName.equals("test_duration")) {
			return Objects.toString(this.getTest_duration());
		} else if (aPropertyName.equals("test_client_address")) {
			return Objects.toString(this.getTest_client_address());
		} else if (aPropertyName.equals("test_client_port")) {
			return Objects.toString(this.getTest_client_port());
		} else if (aPropertyName.equals("test_topology")) {
			return Objects.toString(this.getTest_topology());
		} else if (aPropertyName.equals("test_hash_code")) {
			return Objects.toString(this.getTest_hash_code());
		} else if (aPropertyName.equals("suite")) {
			return Objects.toString(this.getSuite());
		} else if (aPropertyName.equals("ports_type")) {
			return Objects.toString(this.getPorts_type());
		} else if (aPropertyName.equals("setup_type")) {
			return Objects.toString(this.getSetup_type());
		} else if (aPropertyName.equals("test_start_time")) {
			return Objects.toString(this.getTest_start_time());
		} else if (aPropertyName.equals("setup_sn")) {
			return Objects.toString(this.getSetup_sn());
		} else if (aPropertyName.equals("tc_management")) {
			return Objects.toString(this.getTc_management());
		} else if (aPropertyName.equals("test_aggregated_area")) {
			return Objects.toString(this.getTest_aggregated_area());
		} else if (aPropertyName.equals("run_owner")) {
			return Objects.toString(this.getRun_owner());
		}

		return "ERR - " + aPropertyName;
	}

	public static List<DbResultsTestCase> getDemoFailData1(String err) {
		List<DbResultsTestCase> Results = new ArrayList<DbResultsTestCase>();
		Results.add(new DbResultsTestCase(0,1,2,new Date(),"","",err,"","","","","","","","","","","",new Date(),"","",new HashMap<String,String>(),""));	
		return Results;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getKEY() {
		return KEY;
	}

	public void setKEY(Integer kEY) {
		KEY = kEY;
	}

	public String getTest_location() {
		return test_location;
	}

	public void setTest_location(String test_location) {
		this.test_location = test_location;
	}

	public String getTest_name() {
		return test_name;
	}

	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}

	public String getTest_result() {
		return test_result;
	}

	public void setTest_result(String test_result) {
		this.test_result = test_result;
	}

	public String getTest_error() {
		return test_error;
	}

	public void setTest_error(String test_error) {
		this.test_error = test_error;
	}

	public String getSetup_address() {
		return setup_address;
	}

	public void setSetup_address(String setup_address) {
		this.setup_address = setup_address;
	}

	public String getSetup_ports() {
		return setup_ports;
	}

	public void setSetup_ports(String setup_ports) {
		this.setup_ports = setup_ports;
	}

	public String getTest_duration() {
		return test_duration;
	}

	public void setTest_duration(String test_duration) {
		this.test_duration = test_duration;
	}

	public String getTest_client_address() {
		return test_client_address;
	}

	public void setTest_client_address(String test_client_address) {
		this.test_client_address = test_client_address;
	}

	public String getTest_topology() {
		return test_topology;
	}

	public void setTest_topology(String test_topology) {
		this.test_topology = test_topology;
	}

	public String getTest_client_port() {
		return test_client_port;
	}

	public void setTest_client_port(String test_client_port) {
		this.test_client_port = test_client_port;
	}

	public String getTest_hash_code() {
		return test_hash_code;
	}

	public void setTest_hash_code(String test_hash_code) {
		this.test_hash_code = test_hash_code;
	}

	public String getSuite() {
		return suite;
	}

	public void setSuite(String suite) {
		this.suite = suite;
	}

	public String getPorts_type() {
		return ports_type;
	}

	public void setPorts_type(String ports_type) {
		this.ports_type = ports_type;
	}

	public String getSetup_type() {
		return setup_type;
	}

	public void setSetup_type(String setup_type) {
		this.setup_type = setup_type;
	}
	//
	//	public String getBug_id() {
	//		return bug_id;
	//	}
	//
	//	public void setBug_id(String bug_id) {
	//		this.bug_id = bug_id;
	//	}
	//
	//	public String getBug_state() {
	//		return bug_state;
	//	}
	//
	//	public void setBug_state(String bug_state) {
	//		this.bug_state = bug_state;
	//	}

	public String getSetup_sn() {
		return setup_sn;
	}

	public void setSetup_sn(String setup_sn) {
		this.setup_sn = setup_sn;
	}

	public String getTest_aggregated_area() {
		return test_aggregated_area;
	}

	public void setTest_aggregated_area(String area) {
		this.test_aggregated_area = area;
	}

	public Date getTest_start_time() {
		return test_start_time;
	}

	public void setTest_start_time(Date test_start_time) {
		this.test_start_time = test_start_time;
	}

	public String getTc_management() {
		return tc_management;
	}

	public void setTc_management(String tc_management) {
		this.tc_management = tc_management;
	}

	public Integer getRun_key() {
		return run_key;
	}

	public void setRun_key(Integer run_key) {
		this.run_key = run_key;
	}

	public Date getRun_start_date() {
		return run_start_date;
	}

	public void setRun_start_date(Date run_start_date) {
		this.run_start_date = run_start_date;
	}

	public HashMap<String,String> getSoftware_builds() {
		return software_builds;
	}

	public void setSoftware_builds(HashMap<String,String> software_builds) {
		this.software_builds = software_builds;
	}

	public String getRun_owner() {
		return run_owner;
	}

	public void setRun_owner(String run_owner) {
		this.run_owner = run_owner;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}




}