package classes;

import java.util.ArrayList;
import java.util.List;

public class Query {

	private String lang;
	private String domain;
	private List<String> query;

	public Query(){
		query=new ArrayList();
	}
	
	

	public Query(String lang, String domain, List<String> query) {
		super();
		this.lang = lang;
		this.domain = domain;
		this.query = query;
	}
	public Query(String lang, String domain) {
		super();
		this.lang = lang;
		this.domain = domain;
		query=new ArrayList();
	}
	

	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public List<String> getQuery() {
		return query;
	}
	public void setQuery(List<String> query) {
		this.query = query;
	}
	public void addQuery(String q) {
		query.add(q);
	}
	public String getFirstQuery(){
		return query.get(0);
	}
	public void clearAndReplaceFirstQuery(String newText){
		query.clear();
		query.add(newText);
	}
	
}
