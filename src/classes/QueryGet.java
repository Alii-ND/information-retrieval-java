package classes;

import java.util.ArrayList;
import java.util.List;

public class QueryGet {
	String lang;
	String domain;
	String query;
	List<URL> urls;
	
	
	public QueryGet() {
		urls = new ArrayList<URL>();
	}
	
	
	public QueryGet(String lang, String domain, String query) {
		this.lang = lang;
		this.domain = domain;
		this.query = query;
		urls = new ArrayList<URL>();
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
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public List<URL> getUrls() {
		return urls;
	}
	
	public void addURL(URL url){
		urls.add(url);
	}
	
	public void removeURL(int urlId){
		for (URL url : urls) {
			if(url.getId() ==urlId){
				urls.remove(url);
				break;
			}
		}
	}
	public void removeURL(URL url){
		urls.remove(url);
	}
	public boolean EqualsTo(String dom,String lang,String query){
		if(getDomain().equals(dom) && getLang().equals(lang) && getQuery().equals(query)){
			return true;
		}
		return false;
	}
}
