package informationRetrievel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import classes.QueryGet;
import classes.URL;

public class DatabaseHandler {

	private Connection conn;
	private final String ConnectionURl = "jdbc:mysql://localhost/google_information_retrieval";

	private final String tableQueries_contentWithoutAPI = "queries_content_table";
	private final String tablefiltered_queriesWithoutAPI = "filtered_queries";
	private final String tableSimilarityURLWithoutAPI = "similarityURL";

	private final String tableQueries_contentWithAPI = "queries_content_table_with_api";
	private final String tablefiltered_queriesWithAPI = "filtered_queries_with_api";
	private final String tableSimilarityURLWithAPI = "similarityURL_with_api";

	private final String columnId = "id";
	private final String columnDomain = "domain";
	private final String columnLang = "lang";
	private final String columnQuery = "query";
	private final String columnUrl = "url";
	private final String columnOld_body = "old_body";
	private final String columnUrlResultBodyContent = "urlResultBodyContent";
	private final String columnFiltered_body = "filtered_body";
	private final String columnIdURL1 = "idURL1";
	private final String columnIdURL2 = "idURL2";
	private final String columnTechnique = "technique";
	private final String columnTechniqueType = "techniqueType";
	private final String columnScore = "score";
	private final String columnTimeExec = "timeExec";

	public DatabaseHandler() {
		String myDriver = "org.gjt.mm.mysql.Driver";
		String myUrl = ConnectionURl;
		try {
			Class.forName(myDriver);

			String unicode = "?useUnicode=yes&characterEncoding=UTF-8"; // to
			// text
			conn = DriverManager.getConnection(myUrl + unicode, "root", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------

	// ##################################
	// WITHOUT API
	// ##################################
	public void saveCollectDataWithoutAPI(String domain, String lang,
			String query, String url, String scriptResult) {
		System.out.println("################################");
		System.out.println("saveCollectDataWithoutAPI function started");

		try {

			// the mysql insert statement
			String insert_query = " insert into "
					+ tableQueries_contentWithoutAPI + "(" + columnDomain
					+ ", " + columnLang + ", " + columnQuery + ", " + columnUrl
					+ ", " + columnUrlResultBodyContent + ")"
					+ " values (?, ?, ?, ?, ?)";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = conn
					.prepareStatement(insert_query);
			preparedStmt.setString(1, domain);
			preparedStmt.setString(2, lang);
			preparedStmt.setString(3, query);
			preparedStmt.setString(4, url);
			preparedStmt.setString(5, scriptResult);

			// execute the preparedstatement
			preparedStmt.execute();

			// conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(domain);
			System.err.println(lang);
			System.err.println(query);
			System.err.println(url);
			System.err.println(e.getMessage());
		}

		System.out.println("saveCollectDataWithoutAPI function finished");
		System.out.println("################################");

	}

	private void deleteEmptyDataFiltered_queriesWithoutAPI() {
		System.out.println("################################");
		System.out.println("deleteEmptyDataFiltered_queries function started");
		// create the java statement
		Statement st;
		try {
			st = conn.createStatement();

			String delete_empty_query = "DELETE from "
					+ tablefiltered_queriesWithoutAPI + " WHERE trim("
					+ columnFiltered_body + ")=\"\"";
			st.executeQuery(delete_empty_query);
			st.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("deleteEmptyDataFiltered_queries function finished");
		System.out.println("################################");
	}

	private void saveInseretDataWithoutApi(String domain, String lang,
			String query, String url, String urlResultBodyContent,
			String filtered) {
		System.out.println("################################");
		System.out.println("saveInseretDataWithoutApi function started");

		String insert_query = " insert into " + tablefiltered_queriesWithoutAPI
				+ " (" + columnDomain + ", " + columnLang + ", " + columnQuery
				+ ", " + columnUrl + ", " + columnOld_body + ", "
				+ columnFiltered_body + ")" + " values (?, ?, ?, ?, ?, ?)";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(insert_query);

			preparedStmt.setString(1, domain);
			preparedStmt.setString(2, lang);
			preparedStmt.setString(3, query);
			preparedStmt.setString(4, url);
			preparedStmt.setString(5, urlResultBodyContent);
			preparedStmt.setString(6, filtered);

			// execute the preparedstatement
			preparedStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("saveInseretDataWithoutApi function finished");
		System.out.println("################################");
	}

	public void filterDataWithoutAPI(int threshold) {
		System.out.println("################################");
		System.out.println("filterDataWithoutAPI function started");

		try {

			// Select data from table queries_content_table
			String select_query = "SELECT * FROM "
					+ tableQueries_contentWithoutAPI + " GROUP BY " + columnUrl;

			// create the java statement
			Statement st = conn.createStatement();

			// execute the query, and get a java ResultSet
			ResultSet rs = st.executeQuery(select_query);

			int i = 1;

			// iterate through the java ResultSet
			while (rs.next()) {
				String domain = rs.getString(columnDomain);
				String lang = rs.getString(columnLang);
				String query = rs.getString(columnQuery);
				String url = rs.getString(columnUrl);
				String urlResultBodyContent = rs
						.getString(columnUrlResultBodyContent);

				String urlResultBodyContent_removeNoiseSymbol = Main
						.removeNoiseSymbol(urlResultBodyContent, lang);
				String urlResultBodyContent_removeNoiseSentence = Main
						.removeNoiseSentence(
								urlResultBodyContent_removeNoiseSymbol,
								threshold);

				// the mysql insert statement
				saveInseretDataWithoutApi(domain, lang, query, url,
						urlResultBodyContent,
						urlResultBodyContent_removeNoiseSentence);

				System.out.println(i);
				i++;

			}
			deleteEmptyDataFiltered_queriesWithoutAPI();

		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
		}

		System.out.println("filterDataWithoutAPI function finished");
		System.out.println("################################");
	}

	public List<QueryGet> getFilteredQueriesWithoutAPI() {
		List<QueryGet> querys = new ArrayList<QueryGet>();

		try {
			// Select data from table queries_content_table
			String select_query = "SELECT " + columnId + "," + columnLang + ","
					+ columnDomain + "," + columnQuery + "," + columnUrl + ","
					+ columnFiltered_body + " FROM "
					+ tablefiltered_queriesWithoutAPI 
					+" "
					+" ORDER BY " +  columnLang + "," + columnDomain + "," + columnQuery;

			// create the java statement
			Statement st = conn.createStatement();

			// execute the query, and get a java ResultSet
			ResultSet rs = st.executeQuery(select_query);

			// int i = 1;

			// iterate through the java ResultSet
			while (rs.next()) {
				int id = rs.getInt(columnId);
				String domain = rs.getString(columnDomain);
				String lang = rs.getString(columnLang);
				String query = rs.getString(columnQuery);
				String url = rs.getString(columnUrl);
				String urlResultBodyContent = rs.getString(columnFiltered_body);
				QueryGet selectedQuery = null;
				for (QueryGet queryGet : querys) {
					if (queryGet.EqualsTo(domain, lang, query)) {
						selectedQuery = queryGet;
						break;
					}
				}
				if (selectedQuery == null) {
					selectedQuery = new QueryGet(lang, domain, query);

					selectedQuery
							.addURL(new URL(id, url, urlResultBodyContent));
					querys.add(selectedQuery);
				} else {
					selectedQuery
							.addURL(new URL(id, url, urlResultBodyContent));
				}
				// System.out.println(i);
				// i++;
			}
		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(e.toString());
		}
		return querys;

	}

	public void similarityAddWithoutAPI(int idURL1, int idURL2,
			String techniqueName, String techniqueType, double distance,
			double execTime) {

		// the mysql insert statement
		String insert_query = " insert into " + tableSimilarityURLWithoutAPI
				+ " (" + columnIdURL1 + ", " + columnIdURL2 + ", "
				+ columnTechnique + ", " + columnTechniqueType + ", "
				+ columnScore + "," + columnTimeExec + ")"
				+ " values (?, ?, ?,?, ?, ?)";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(insert_query);

			preparedStmt.setInt(1, idURL1);
			preparedStmt.setInt(2, idURL2);
			preparedStmt.setString(3, techniqueName);
			preparedStmt.setString(4, techniqueType);
			if (Double.isNaN(distance))
				preparedStmt.setNull(5, Types.NULL);
			else
				preparedStmt.setDouble(5, distance);
			preparedStmt.setDouble(6, execTime);
			// execute the preparedstatement
			preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------

	// ##################################
	// WITH API
	// ##################################
	public void saveCollectDataWithAPI(String domain, String lang,
			String query, String url, String scriptResult) {
		System.out.println("################################");
		System.out.println("saveCollectDataWithAPI function started");

		try {

			// the mysql insert statement
			String insert_query = " insert into " + tableQueries_contentWithAPI
					+ "(" + columnDomain + ", " + columnLang + ", "
					+ columnQuery + ", " + columnUrl + ", "
					+ columnUrlResultBodyContent + ")"
					+ " values (?, ?, ?, ?, ?)";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = conn
					.prepareStatement(insert_query);
			preparedStmt.setString(1, domain);
			preparedStmt.setString(2, lang);
			preparedStmt.setString(3, query);
			preparedStmt.setString(4, url);
			preparedStmt.setString(5, scriptResult);

			// execute the preparedstatement
			preparedStmt.execute();

			// conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(domain);
			System.err.println(lang);
			System.err.println(query);
			System.err.println(url);
			System.err.println(e.getMessage());
		}

		System.out.println("saveCollectDataWithAPI function finished");
		System.out.println("################################");

	}

	private void deleteEmptyDataFiltered_queriesWithAPI() {
		System.out.println("################################");
		System.out.println("deleteEmptyDataFiltered_queries function started");
		// create the java statement
		Statement st;
		try {
			st = conn.createStatement();

			String delete_empty_query = "DELETE from "
					+ tablefiltered_queriesWithAPI + " WHERE trim("
					+ columnFiltered_body + ")=\"\"";
			st.executeQuery(delete_empty_query);
			st.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("deleteEmptyDataFiltered_queries function finished");
		System.out.println("################################");
	}

	private void saveInseretDataWithApi(String domain, String lang,
			String query, String url, String urlResultBodyContent,
			String filtered) {
		System.out.println("################################");
		System.out.println("saveInseretDataWithApi function started");

		String insert_query = " insert into " + tablefiltered_queriesWithAPI
				+ " (" + columnDomain + ", " + columnLang + ", " + columnQuery
				+ ", " + columnUrl + ", " + columnOld_body + ", "
				+ columnFiltered_body + ")" + " values (?, ?, ?, ?, ?, ?)";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(insert_query);

			preparedStmt.setString(1, domain);
			preparedStmt.setString(2, lang);
			preparedStmt.setString(3, query);
			preparedStmt.setString(4, url);
			preparedStmt.setString(5, urlResultBodyContent);
			preparedStmt.setString(6, filtered);

			// execute the preparedstatement
			preparedStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("saveInseretDataWithApi function finished");
		System.out.println("################################");
	}

	private String UnicodeTOString(String myString) {
		String text = "";
		try {
			text = org.apache.commons.lang3.StringEscapeUtils
					.unescapeJava(myString);
		} catch (Exception e) {

			text = org.apache.commons.lang3.StringEscapeUtils
					.unescapeJava(myString.substring(0,
							myString.lastIndexOf("\\u")));
		}
		return text;
	}

	public void filterDataWithAPI() {
		System.out.println("################################");
		System.out.println("filterDataWithAPI function started");

		try {

			// Select data from table queries_content_table
			String select_query = "SELECT * FROM "
					+ tableQueries_contentWithAPI + " Where "
					+ columnUrlResultBodyContent + " <> \"\" and "
					+ columnUrlResultBodyContent + " <> \"ul\" and "
					+ columnUrlResultBodyContent + " <>\"\\\\n\""
					+ " GROUP BY " + columnUrl;

			// create the java statement
			Statement st = conn.createStatement();

			// execute the query, and get a java ResultSet
			ResultSet rs = st.executeQuery(select_query);

			int i = 1;

			// iterate through the java ResultSet
			while (rs.next()) {
				String domain = rs.getString(columnDomain);
				String lang = rs.getString(columnLang);
				String query = rs.getString(columnQuery);
				String url = rs.getString(columnUrl);
				String urlResultBodyContent = rs
						.getString(columnUrlResultBodyContent);
				if (lang.equals("ar")) {
					urlResultBodyContent = UnicodeTOString(urlResultBodyContent);
					if (urlResultBodyContent.trim() == "") {
						continue;
					}
				}

				String urlResultBodyContent_removeNoiseSymbol = Main
						.removeNoiseSymbol(urlResultBodyContent, lang);

				// the mysql insert statement
				saveInseretDataWithApi(domain, lang, query, url,
						urlResultBodyContent,
						urlResultBodyContent_removeNoiseSymbol);

				System.out.println(i);
				i++;

			}
			deleteEmptyDataFiltered_queriesWithAPI();

		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
		}

		System.out.println("filterDataWithAPI function finished");
		System.out.println("################################");
	}

	public List<QueryGet> getFilteredQueriesWithAPI() {
		List<QueryGet> querys = new ArrayList<QueryGet>();

		try {
			// Select data from table queries_content_table
			String select_query = "SELECT " + columnId + "," + columnLang + ","
					+ columnDomain + "," + columnQuery + "," + columnUrl + ","
					+ columnFiltered_body + " FROM "
					+ tablefiltered_queriesWithAPI + " ORDER BY " + columnLang
					+ "," + columnDomain + "," + columnQuery;

			// create the java statement
			Statement st = conn.createStatement();

			// execute the query, and get a java ResultSet
			ResultSet rs = st.executeQuery(select_query);

			// int i = 1;

			// iterate through the java ResultSet
			while (rs.next()) {
				int id = rs.getInt(columnId);
				String domain = rs.getString(columnDomain);
				String lang = rs.getString(columnLang);
				String query = rs.getString(columnQuery);
				String url = rs.getString(columnUrl);
				String urlResultBodyContent = rs.getString(columnFiltered_body);
				QueryGet selectedQuery = null;
				for (QueryGet queryGet : querys) {
					if (queryGet.EqualsTo(domain, lang, query)) {
						selectedQuery = queryGet;
						break;
					}
				}
				if (selectedQuery == null) {
					selectedQuery = new QueryGet(lang, domain, query);

					selectedQuery
							.addURL(new URL(id, url, urlResultBodyContent));
					querys.add(selectedQuery);
				} else {
					selectedQuery
							.addURL(new URL(id, url, urlResultBodyContent));
				}
				// System.out.println(i);
				// i++;
			}
		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(e.toString());
		}
		return querys;
	}

	public void similarityAddWithAPI(int idURL1, int idURL2,
			String techniqueName, String techniqueType, double distance,
			double execTime) {

		// the mysql insert statement
		String insert_query = " insert into " + tableSimilarityURLWithAPI
				+ " (" + columnIdURL1 + ", " + columnIdURL2 + ", "
				+ columnTechnique + ", " + columnTechniqueType + ", "
				+ columnScore + "," + columnTimeExec + ")"
				+ " values (?, ?, ?,?, ?, ?)";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(insert_query);

			preparedStmt.setInt(1, idURL1);
			preparedStmt.setInt(2, idURL2);
			preparedStmt.setString(3, techniqueName);
			preparedStmt.setString(4, techniqueType);
			if (Double.isNaN(distance))
				preparedStmt.setNull(5, Types.NULL);
			else
				preparedStmt.setDouble(5, distance);
			preparedStmt.setDouble(6, execTime);
			// execute the preparedstatement
			preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
}
