package edu.umuc.controllers;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScrapeData {
	public List<School> scrapeData(String year, String season, String sport, RankWeight rankWeight) throws InterruptedException, TimeoutException {

		final SportRankingUIManager sportRankingUIManager = SportRankingUIManager.getSingletonInstance();
		final List<School> schools = Controller.getSchools();

		final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(300);

		schools.forEach(school -> {
			final GetDataThread task = new GetDataThread(school, year, season, sport);
			System.out.println("A new task has been added : " + school.getUrlPath() + ", School #: " + schools.indexOf(school));
			executor.execute(task);
		});

		executor.shutdown();
		boolean threadsCompletedBeforeTimeout = executor.awaitTermination(5, TimeUnit.MINUTES);

		if (threadsCompletedBeforeTimeout) {
			schools.forEach(school -> {
				school.getOpponents().forEach(opponent -> {
					final School oppSchool = schools.stream()
							.filter(school1 -> school1.getUrlPath().equals(opponent))
							.findFirst()
							.orElse(null);

					if (oppSchool == null) {
						System.out.println("Unable to find school path for opponent: " + opponent);
					} else {
						System.out.println("Processing school: " + school.getSchoolName());
						school.addOpponentWins(oppSchool.getWins());
					}
				});

				if (school.getWins() != 0 || school.getLosses() != 0) {
					school.calculateRankPoints(rankWeight, Controller.getLeagueWeightForSchool(school.getSchoolName()));
				}
			});
		} else {
			throw new TimeoutException("Data scrape operation timed out. Check connection and Washington Post website for proper operation.");
		}

		return schools;
	}
}

