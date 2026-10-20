/**
 * Write a description of class Tester here.
 * 
 * @author D. K.
 * @version 2026-10-06
 */

import java.util.*;

public class Tester
{
    public void testLogEntry() {
        LogEntry le = new LogEntry("1.2.3.4", new Date(), "example request", 200, 500);
        System.out.println(le);
        LogEntry le2 = new LogEntry("1.2.100.4", new Date(), "example request 2", 300, 400);
        System.out.println(le2);
    }
    
    public void testLogAnalyzer() {
        LogAnalyzer la = new LogAnalyzer();
        la.readFile("weblog2_log");
        
        System.out.println("Q4: Unique IPs = " + la.countUniqueIPs());
        System.out.println("Q5: Size of unique IP visits on Sep 24 = " + la.uniqueIPVisitsOnDay("Sep 24").size());
        System.out.println("Q6: Unique IPs in range 200-299 = " + la.countUniqueIPsInRange(200, 299));
        
        HashMap<String, Integer> counts = la.countVisitsPerIP();
        System.out.println("Q7: Most number visits by IP = " + la.mostNumberVisitsByIP(counts));
        System.out.println("Q8: IPs with most visits = " + la.iPsMostVisits(counts));
        
        HashMap<String, ArrayList<String>> days = la.iPsForDays();
        System.out.println("Q9: Day with most IP visits = " + la.dayWithMostIPVisits(days));
        System.out.println("Q10: IPs with most visits on Sep 30 = " + la.iPsWithMostVisitsOnDay(days, "Sep 30"));
    }

    public static void main(String[] args) {
        Tester t = new Tester();
        t.testLogAnalyzer();
    }
}
