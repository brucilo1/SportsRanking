/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umuc.controllers;

/**
 *
 * @author jhender1
 */
import edu.umuc.models.League;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ScrapeData {

	//	private HashMap<String, League> leagues = new HashMap<String, League>();

	public ArrayList<School> scrapeData(String year, String season, String sport, RankWeight rankWeight) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, InterruptedException, TimeoutException {

		// TODO: move the next few sections to loadProperties
		File file = new File("C:\\Users\\jhender1\\Desktop\\hssrs-properties-v2.xml");
		FileInputStream fileIS = new FileInputStream(file);
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document xmlDocument = builder.parse(fileIS);
		XPath xPath = XPathFactory.newInstance().newXPath();

		// Load Leagues
		HashMap<String, League> leagues = new HashMap<String, League>();
		String leagueExpression = "/hssrs/leagues/league";
		NodeList leagueNodeList = (NodeList) xPath.compile(leagueExpression).evaluate(xmlDocument, XPathConstants.NODESET);

		for (int i = 0; i < leagueNodeList.getLength(); i++) {
			Node nNode = leagueNodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				String leagueId = element.getElementsByTagName("leagueId").item(0).getTextContent();
				String name = element.getElementsByTagName("name").item(0).getTextContent();
				float weight = Float.parseFloat(element.getElementsByTagName("weight").item(0).getTextContent());
				System.out.println("Name: " + leagueId + ", Path: " + name + ", " + weight);
				if (!leagueId.isEmpty()) {
					leagues.put(leagueId, new League(leagueId, name, weight));
				}

			}
		}

		// Load Schools
		HashMap<String, School> schools = new HashMap<String, School>();
		String schoolExpression = "/hssrs/schools/school";
		NodeList schoolNodeList = (NodeList) xPath.compile(schoolExpression).evaluate(xmlDocument, XPathConstants.NODESET);

		for (int i = 0; i < schoolNodeList.getLength(); i++) {
			Node nNode = schoolNodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				String schoolName = element.getElementsByTagName("name").item(0).getTextContent();
				String schoolPath = element.getElementsByTagName("path").item(0).getTextContent();
				String leagueId = element.getElementsByTagName("leagueId").item(0).getTextContent();
				System.out.println("Name: " + schoolName + ", Path: " + schoolPath + ", " + leagueId);
				if (!schoolPath.isEmpty()) {
					schools.put(schoolPath, new School(schoolName, schoolPath, leagues.get(leagueId)));
				}

			}
		}

		// TODO: End move to LoadProperties section

		int schoolCount = 1;
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(300);
		for (Map.Entry<String, School> school : schools.entrySet()) {
			GetDataThread task = new GetDataThread(school.getValue(), year, season, sport);
			System.out.println("A new task has been added : " + school.getValue().getUrlPath() + ", School #: " + schoolCount++);
			executor.execute(task);
		}
		executor.shutdown();
		boolean threadsCompletedbeforeTimeout = executor.awaitTermination(5, TimeUnit.MINUTES);

		if (threadsCompletedbeforeTimeout) {
			for (Map.Entry<String, School> school : schools.entrySet()) {
				for (String schoolPath : school.getValue().getOpponents()) {
					School opponent = schools.get(schoolPath);
					if (opponent != null) {
						school.getValue().addOpponentWins(opponent.getWins());
					} 
				}
				if (school.getValue().getWins() != 0 || school.getValue().getLosses() != 0) {
					school.getValue().calculateRankPoints(rankWeight);	
				}
			}
		} else {
			throw new TimeoutException("Data scrape operation timed out. Check connection and Washington Post website for proper operation.");
		}

		return new ArrayList<School>(schools.values());
	}
}

