package edu.columbia.pm.dashboard;

import edu.columbia.pm.dashboard.calculator.HoldingCalculator;
import edu.columbia.pm.dashboard.web.WebServerVerticle;
import io.vertx.core.Vertx;

public class PortfolioManagerDashboard {

    public static void main(String[] args) throws Exception {
        System.setProperty("vertx.disableFileCaching", "true");

        Process prices = Runtime.getRuntime().exec(
                "python /Users/pat4520/Documents/WS/EECS6895/CS6895-FINAL/src/main/python/ystockspoll.py");
        Runtime.getRuntime().addShutdownHook( new Thread() {
            public void run() {
                if (prices.isAlive()) prices.destroy();
            }
        });

        Process predictions = Runtime.getRuntime().exec(
                "python /Users/pat4520/Documents/WS/EECS6895/CS6895-FINAL/src/main/python/google_finance_predictions.py");
        Runtime.getRuntime().addShutdownHook( new Thread() {
            public void run() {
                if (predictions.isAlive()) predictions.destroy();
            }
        });

        new Thread() {
            public void run() {
                HoldingCalculator.main(new String[]{
                        "localhost:2181",
                        "holdingsCalculator",
                        "test-ks-trades,test-ks-prices,test-ks-predictions",
                        "1"});
            }
        }.start();

        Vertx.vertx().deployVerticle(WebServerVerticle.class.getName());
    }

}
