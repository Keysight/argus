package com.argus.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DbResultsAggregated implements Serializable, DbSoftwareBuildsInterface {

	private static final long serialVersionUID = 4669118044879883942L;

	private Integer index;
	
	private Integer KEY;
	private Date run_start_date;
	private Date run_end_date;
	private String run_name;
	private Integer total_count_end;
	private Integer total_count_now;
	private Integer pass;
	private Integer fail;
	private Integer skip;
	private Double pass_percent;
	private Double fail_percent;
	private Double skip_percent;
	private String run_duration;
	private String run_comment;
	private HashMap<String,String> software_builds;

	public DbResultsAggregated() {}

	public DbResultsAggregated(
			Integer index,
			Integer KEY,
			Date run_start_date, 
			Date run_end_date, 
			String run_name, 
			Integer total_count_end, 
			Integer total_count_now, 
			Integer pass, 
			Integer fail, 
			Integer skip, 
			Double pass_percent, 
			Double fail_percent, 
			Double skip_percent, 
			String run_duration, 
			String run_comment, 
			HashMap<String,String> software_builds
			)
	{
		this.setIndex(index);
		this.KEY = KEY;
		this.run_start_date = run_start_date;
		this.run_end_date = run_end_date;
		this.run_name = run_name;
		this.total_count_end = total_count_end;
		this.total_count_now = total_count_now;
		this.pass = pass;
		this.fail = fail;
		this.skip = skip;
		this.pass_percent = pass_percent;
		this.fail_percent = fail_percent;
		this.skip_percent = skip_percent;
		this.run_duration = run_duration;
		this.run_comment = run_comment;
		this.software_builds = software_builds;
	}

	public String getRun_name() {
		return run_name;
	}

	public void setRun_name(String run_name) {
		this.run_name = run_name;
	}

	public Integer getPass() {
		return pass;
	}

	public Integer getFail() {
		return fail;
	}

	public Integer getSkip() {
		return skip;
	}

	public Double getPass_percent() {
		return pass_percent;
	}

	public Double getFail_percent() {
		return fail_percent;
	}

	public Double getSkip_percent() {
		return skip_percent;
	}

	public String getRun_duration() {
		return run_duration;
	}

	// a nasty way to carry errors from server to client
	public static List<DbResultsAggregated> getDemoData(String err) {
		List<DbResultsAggregated> Results = new ArrayList<DbResultsAggregated>();
		Results.add(new DbResultsAggregated(0,0, new Date(), new Date(), err, 0,0,0,0,0,0.0,0.0,0.0, "", "",  new HashMap<String,String>()));		
		return Results;
	}

	public String getProperty(String property) {
		if        (property.equals("KEY")) {
			return String.valueOf(getKEY());
		} else if (property.equals("index")) {
			return String.valueOf(getIndex());
		} else if (property.equals("run_start_date")) {
			return String.valueOf(getRun_start_date());
		} else if (property.equals("run_end_date")) {
			return String.valueOf(getRun_end_date());
		} else if (property.equals("run_name")) {
			return String.valueOf(getRun_name());
		} else if (property.equals("total_count_end")) {
			return String.valueOf(getTotal_count_end());
		} else if (property.equals("total_count_now")) {
			return String.valueOf(getTotal_count_now());
		} else if (property.equals("pass")) {
			return String.valueOf(getPass());
		} else if (property.equals("fail")) {
			return String.valueOf(getFail());
		} else if (property.equals("skip")) {
			return String.valueOf(getSkip());
		} else if (property.equals("pass_percent")) {
			return String.valueOf(getPass_percent());
		} else if (property.equals("fail_percent")) {
			return String.valueOf(getFail_percent());
		} else if (property.equals("skip_percent")) {
			return String.valueOf(getSkip_percent());
		} else if (property.equals("run_duration")) {
			return String.valueOf(getRun_duration());
		} else if (property.equals("run_comment")) {
			return String.valueOf(getRun_comment());
		}
		// TODO: software_builds
		return "";
	}

	public Integer getKEY() {
		return KEY;
	}

	public void setKEY(Integer KEY) {
		this.KEY = KEY;
	}

	public String getValueFromMap(String key) {
		return software_builds.get(key);
	}

	public void setValueFromMap(String key, String value) {
		this.software_builds.put(key, value);
	}

	public HashMap<String,String> getSoftware_builds() {
		return software_builds;
	}

	public void setSoftware_builds(HashMap<String,String> software_builds) {
		this.software_builds = software_builds;
	}

	public String getRun_comment() {
		return run_comment;
	}

	public void setRun_comment(String run_comment) {
		this.run_comment = run_comment;
	}

	public Integer getTotal_count_now() {
		return total_count_now;
	}

	public void setTotal_count_now(Integer total_count_now) {
		this.total_count_now = total_count_now;
	}

	public Integer getTotal_count_end() {
		return total_count_end;
	}

	public void setTotal_count_end(Integer total_count_end) {
		this.total_count_end = total_count_end;
	}

	public Date getRun_end_date() {
		return run_end_date;
	}

	public void setRun_end_date(Date run_end_date) {
		this.run_end_date = run_end_date;
	}

	public Date getRun_start_date() {
		return run_start_date;
	}

	public void setRun_start_date(Date run_start_date) {
		this.run_start_date = run_start_date;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}

