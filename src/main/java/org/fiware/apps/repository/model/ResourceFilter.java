package org.fiware.apps.repository.model;

import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;

public class ResourceFilter {
	private int offset;
	private int limit;
	private String filter="";

	public ResourceFilter(int offset, int limit, String filter) {
		super();
		this.offset = offset;
		this.limit = limit;
		this.filter = filter == null ? "" : filter;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public BasicDBObject parseFilter(){

		BasicDBObject query = new BasicDBObject();
		String[] filterStrings = this.filter.split(",");
		boolean added = false;

		for (int i=0; i<filterStrings.length; i++) {


			String[] filter = filterStrings[i].split(":");
			if(filter.length==2){
				Pattern p = Pattern.compile(filter[1], Pattern.CASE_INSENSITIVE);
				query.append(filter[0], p);
				added=true;
			}
		}


		if(!added){
			Pattern pat = Pattern.compile("");
			query.append("id", pat);
		}
		return query;
	}

}
