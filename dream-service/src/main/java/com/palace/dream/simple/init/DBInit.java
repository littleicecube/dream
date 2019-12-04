package com.palace.dream.simple.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.sql.DataSource;

import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import com.alibaba.druid.pool.DruidDataSource;
import com.palace.dream.simple.db.SimpleTemplate;

public class DBInit {

	public static String TARGET_CLASSES = "/target/classes/";

	public static void main(String[] args) throws Exception {

	}

	public void sim() {
		String fileName = "pom.xml";
		String filePath = getBasePath() + File.pathSeparator + fileName;
		Model model = getPomModel(fileName);
		SimpleTemplate simpleTemplate = null;
		for (Profile profile : model.getProfiles()) {
			String strID = profile.getId();
			if (strID.equals("test")) {
				simpleTemplate = geTemplate(getDataSource(profile));
				break;
			}
		}
		String sql = getSql("init.sql");
		simpleTemplate.execute(sql);
	}

	public String getSql(String fileName) {
		String filePath = getBasePath() + File.pathSeparator + fileName;
		System.err.println("sqlFilePath:" + fileName);
		File file = new File(filePath);
		FileInputStream is = null;
		StringBuilder stringBuilder = null;
		try {
			is = new FileInputStream(file);
			InputStreamReader streamReader = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(streamReader);
			String line;
			stringBuilder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				// stringBuilder.append(line);
				stringBuilder.append(line);
			}
			reader.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	public SimpleTemplate geTemplate(DataSource ds) {
		return new SimpleTemplate(ds);
	}

	public DataSource getDataSource(Profile profile) {
		String urlKey = "mysql0.url";
		String userKey = "mysql0.username";
		String passwordKey = "mysql0.password";
		DruidDataSource ds = new DruidDataSource();
		ds.setUrl(profile.getProperties().getProperty(urlKey));
		ds.setUsername(profile.getProperties().getProperty(userKey));
		ds.setPassword(profile.getProperties().getProperty(passwordKey));
		return ds;
	}

	public static Model getPomModel(String path) {
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model = reader.read(fis);
			return model;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getBasePath() {
		try {
			URL url = DBInit.class.getClassLoader().getResource(".");
			String basePath = url.toURI().getPath();
			if (basePath.endsWith(TARGET_CLASSES)) {
				basePath = basePath.substring(0, basePath.indexOf(TARGET_CLASSES));
			}
			System.err.println("basePath:" + basePath);
			return basePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
