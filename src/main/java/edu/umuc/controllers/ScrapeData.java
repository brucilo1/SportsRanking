package edu.umuc.controllers;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates the threads to pull the data for a certain sport and year 
 * from the Washington Post website.
 */
public class ScrapeData {
     private static final Logger LOG = LoggerFactory.getLogger(ScrapeData.class);

    public List<School> scrapeData(String year, String season, String sport, RankWeight rankWeight) throws InterruptedException, TimeoutException {
        /**
         * Retrieves the schools from the controller object
         */
        final List<School> schools = Controller.getSchools();
        
        /**
         * Limits the maximum number of threads
         */
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(300);

        /**
         * Create a thread for each school to scrape data and add the information to the school object
         */
        schools.forEach(school -> {
                final GetDataThread task = new GetDataThread(school, year, season, sport);
                executor.execute(task);
        });

        executor.shutdown();
        
        /**
         * Wait for threads to complete before continuing. 
         * threadsCompletedBeforeTimeout is set to true if all threads complete before the timeout
         */
        boolean threadsCompletedBeforeTimeout = executor.awaitTermination(5, TimeUnit.MINUTES);

        /**
         * For each opponent faced, add the opponent wins to the school object oppWins
         */
        if (threadsCompletedBeforeTimeout) {
            schools.forEach(school -> {
                school.getOpponents().forEach(opponent -> {
                    final School oppSchool = schools.stream()
                                    .filter(school1 -> school1.getUrlPath().equals(opponent))
                                    .findFirst()
                                    .orElse(null);

                    if (oppSchool == null) {
                        LOG.warn("Unable to find school path for opponent: " + opponent);
                    } else {
                        school.addOpponentWins(oppSchool.getWins());
                        }
                });

                if (school.getWins() != 0 || school.getLosses() != 0) {
                    school.getTotalPoints(rankWeight, Controller.getLeagueWeightForSchool(school.getSchoolName()));
                }
            });
        } else {
            throw new TimeoutException("Data scrape operation timed out. Check connection and Washington Post website for proper operation.");
        }

        return schools;
    }
}