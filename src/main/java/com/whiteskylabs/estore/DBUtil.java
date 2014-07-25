package com.whiteskylabs.estore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.module.db.internal.domain.database.GenericDbConfig;

public class DBUtil implements Callable {

	public Connection connectionObj = null;
	public PreparedStatement psObj = null;
	private final static Logger log = Logger.getLogger(DBUtil.class.getName());

	public GenericDbConfig derbyConnector;

	public GenericDbConfig getDerbyConnector() {
		return derbyConnector;
	}

	public void setDerbyConnector(GenericDbConfig derbyConnector) {
		this.derbyConnector = derbyConnector;
	}

	/**
	 * This method deals with initialization of DataBase. following are the
	 * steps
	 * 
	 * Connects to DB using Global DB Connector
	 * 
	 * Collects Locations of SQL file(s)
	 * 
	 * Read and sort SQL queries by Create stmts, insert stmts and so on
	 * 
	 * Executes Create queries in the sequence defined in the 'Create-queries.sql' file
	 * 
	 * Check for Tables already created,if not then creates a new else wont.
	 * 
	 * Executes Insert queries in the sequence defined in the Insert-queries.sql' file
	 * 
	 * 
	 * 
	 */
	private void initializeSetup() {
		boolean isDBcreated = false;
		log.info("--------------DB Setup initialized--------------");
		try {
			connectionObj = derbyConnector.getDataSource().getConnection();
			DatabaseMetaData dbmd = connectionObj.getMetaData();

			ArrayList<String> SqlFiles = getSQLFilePaths();

			ArrayList<String> queries = assembleSQLQueries(SqlFiles);

			for (String query : queries) {

				
				String tblName = getTableName(query);
				
				//Get table from Database.
				ResultSet rs = dbmd.getTables(null, null, tblName, null);

				// If table doesn't exist
				if (!rs.next() ) {
					doExecute(query);
					log.info(tblName + " table is created/Updated");
					isDBcreated = true;
				}
				// Insert records after tables are created based on a flag 'isDBCreated' 
				else if(isDBcreated){
					doExecute(query);
					log.info(tblName + " have been inserted");
				}
				
			}
			log.info("DB Setup completed");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Could not setup database. Exceptions occured. ");
		}
	}


	/**
	 * Reads only DOT SQL files from the folder "db-queries"
	 * @returns List of SQL file Paths.
	 * @throws IOException
	 */
	private ArrayList<String> getSQLFilePaths() throws Exception {
		Properties prop = new Properties();
		ArrayList<String> sqlPaths = new ArrayList<String>();
		// Location of folder name "db-queries"
		
		String dirPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		//loading properties file				
		prop.load(new FileInputStream(dirPath+"info.properties"));
		
		String sqlDirLoc = prop.getProperty("sql.dir.location");
		// pointing to "db-queries"
		File dirObject = new File(dirPath+sqlDirLoc);
		// List of childs(i.e files and folders from db-queries directory)
		String[] child = dirObject.list();
		
		// check for 'db-queries' is directory or not
		if (dirObject.isDirectory()) {
			//digging into folder for sql files
			if (child.length == 0) {
				throw new Exception("No SQL files available to setup DB");
			} else {
				//Reading each file
				for (String file : child) {
					File fileObject = new File(dirPath + sqlDirLoc +"/"+ file);
					//Restricts to read only .sql files 
					if (fileObject.isFile()	&& fileObject.getName().endsWith(".sql")) {
						sqlPaths.add(fileObject.getPath());
					}
				}
			}
		}else{
			throw new Exception("no db directories are defined");
		}
		return sqlPaths;
	}

	/**
	 * Read input SqlFiles, collects SQL Queries and sort them by Create stmts, followed with Insert and so on 
	 * @param SqlFiles
	*/
	private ArrayList<String> assembleSQLQueries(ArrayList<String> SqlFiles) {

		SQLReader sqlread = new SQLReader();
		ArrayList<String> sqlQuries = new ArrayList<String>();
		ArrayList<String> qryList = new ArrayList<String>();
		ArrayList<String> tempInsrt = new ArrayList<String>();
		ArrayList<String> others = new ArrayList<String>();

		// ... get queries from all sql file(s) in sequence of create, insert
		// and so on .................
		for (String filePath : SqlFiles) {
			if (filePath.endsWith("Create-quries.sql")) {
				ArrayList<String> crList = sqlread.createQueries(filePath);
				qryList.addAll(crList);

			} else if (filePath.endsWith("Insert-quries.sql")) {
				tempInsrt.addAll(sqlread.createQueries(filePath));
			} else if (filePath.endsWith(".sql")) {
				others.addAll(sqlread.createQueries(filePath));
			}
		}
		//sorting queries
		sqlQuries.addAll(qryList);
		sqlQuries.addAll(tempInsrt);
		sqlQuries.addAll(others);

		return sqlQuries;
	}

	/**
	 * @param query
	 *            - this method executes valid database query
	 * @throws Exception
	 */
	private void doExecute(String query) throws Exception {
		psObj = connectionObj.prepareStatement(query);
		psObj.execute();
	}

	/**
	 * @param q
	 * Reads input query parameter
	 * and
	 * @return name of table in upper case
	 */
	private String getTableName(String q) {
		return q.substring(q.indexOf("CREATE TABLE") + 12, q.indexOf("("))
				.trim().toUpperCase();
	}

	/* (non-Javadoc)
	 * @see org.mule.api.lifecycle.Callable#onCall(org.mule.api.MuleEventContext)
	 */
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		initializeSetup();
		eventContext.getMessage().setPayload("DB Setup completed successfully");
		return eventContext.getMessage();
	}
}
