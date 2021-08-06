package com.argus.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DbResultsSuite implements Serializable {

	private static final long serialVersionUID = 6146457959477295449L;

	private static int ID = 0;

	private String  suite;
	private Integer total;
	private Integer pass;
	private Integer fail;
	private Integer skip;
	private Integer pass_percent;
	private Integer fail_percent;
	private Integer skip_percent;
	private String  run_duration;
	private int id;


	public DbResultsSuite() {
		this.id = ID++;
	}

	public DbResultsSuite(String suite, Integer total, Integer pass, Integer fail,
			Integer skip, Integer pass_percent, Integer fail_percent, Integer skip_percent,
			String run_duration) {
		this();
		this.suite = suite;
		this.total = total;
		this.pass = pass;
		this.fail = fail;
		this.skip = skip;
		this.pass_percent = pass_percent;
		this.fail_percent = fail_percent;
		this.skip_percent = skip_percent;
		this.run_duration = run_duration;
	}

	public String getSuite() {
		return suite;
	}
	
	public void setSuite(String suite) {
		this.suite = suite;
	}

	public Integer getTotal() {
		return total;
	}
	
	public void setTotal(Integer total) {
		this.total = total;
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

	public Integer getPass_percent() {
		return pass_percent;
	}

	public Integer getFail_percent() {
		return fail_percent;
	}

	public Integer getSkip_percent() {
		return skip_percent;
	}

	public String getRun_duration() {
		return run_duration;
	}

	// a nasty way to carry errors from server to client
	public static List<DbResultsSuite> getDemoFailData1(String err) {
		List<DbResultsSuite> Results = new ArrayList<DbResultsSuite>();
		Results.add(new DbResultsSuite(    err, 0,0,0,0,0,0,0,""));		
		return Results;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
