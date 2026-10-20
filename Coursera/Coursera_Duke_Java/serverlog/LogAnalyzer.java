/**
 * Write a description of class LogAnalyzer here.
 * 
 * @author D. K. 
 * @version 2026-10-06
 */

import java.util.*;
import edu.duke.*;

public class LogAnalyzer {
    private ArrayList<LogEntry> records;
     
    public LogAnalyzer() {
        records = new ArrayList<LogEntry>();
    }
        
    public void readFile(String filename) {
        FileResource fr = new FileResource(filename);
        for(String line:fr.lines()) {
            records.add(WebLogParser.parseEntry(line));
        }
    }
        
    public void printAll() {
        for(LogEntry le: records) {
            System.out.println(le);
        }
    }

    public int countUniqueIPs() {
        ArrayList<String> uniqueIPs = new ArrayList<String>();
        for(LogEntry le: records) {
            String ip = le.getIpAddress();
            if(!uniqueIPs.contains(ip)) {
                uniqueIPs.add(ip);
            }
        }
        return uniqueIPs.size();
    }

    public ArrayList<String> uniqueIPVisitsOnDay(String someday) {
        ArrayList<String> uniqueIPs = new ArrayList<String>();
        for(LogEntry le: records) {
            String dateStr = le.getAccessTime().toString();
            String ip = le.getIpAddress();
            if(dateStr.contains(someday) && !uniqueIPs.contains(ip)) {
                uniqueIPs.add(ip);
            }
        }
        return uniqueIPs;
    }

    public int countUniqueIPsInRange(int low, int high) {
        ArrayList<String> uniqueIPs = new ArrayList<String>();
        for(LogEntry le: records) {
            int status = le.getStatusCode();
            String ip = le.getIpAddress();
            if(status >= low && status <= high) {
                if(!uniqueIPs.contains(ip)) {
                    uniqueIPs.add(ip);
                }
            }
        }
        return uniqueIPs.size();
    }

    public HashMap<String, Integer> countVisitsPerIP() {
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        for(LogEntry le: records) {
            String ip = le.getIpAddress();
            if(!counts.containsKey(ip)) {
                counts.put(ip, 1);
            }
            else {
                counts.put(ip, counts.get(ip) + 1);
            }
        }
        return counts;
    }

    public int mostNumberVisitsByIP(HashMap<String, Integer> counts) {
        int max = 0;
        for(String ip: counts.keySet()) {
            if(counts.get(ip) > max) {
                max = counts.get(ip);
            }
        }
        return max;
    }

    public ArrayList<String> iPsMostVisits(HashMap<String, Integer> counts) {
        ArrayList<String> maxIPs = new ArrayList<String>();
        int max = mostNumberVisitsByIP(counts);
        for(String ip: counts.keySet()) {
            if(counts.get(ip) == max) {
                maxIPs.add(ip);
            }
        }
        return maxIPs;
    }

    public HashMap<String, ArrayList<String>> iPsForDays() {
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        for(LogEntry le: records) {
            String dateStr = le.getAccessTime().toString();
            String day = dateStr.substring(4, 10); 
            String ip = le.getIpAddress();
            if(!map.containsKey(day)) {
                ArrayList<String> ips = new ArrayList<String>();
                ips.add(ip);
                map.put(day, ips);
            }
            else {
                map.get(day).add(ip);
            }
        }
        return map;
    }

    public String dayWithMostIPVisits(HashMap<String, ArrayList<String>> daysIPs) {
        int max = 0;
        String maxDay = "";
        for(String day: daysIPs.keySet()) {
            if(daysIPs.get(day).size() > max) {
                max = daysIPs.get(day).size();
                maxDay = day;
            }
        }
        return maxDay;
    }

    public ArrayList<String> iPsWithMostVisitsOnDay(HashMap<String, ArrayList<String>> daysIPs, String someday) {
        ArrayList<String> ips = daysIPs.get(someday);
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        for(String ip: ips) {
            if(!counts.containsKey(ip)) {
                counts.put(ip, 1);
            }
            else {
                counts.put(ip, counts.get(ip) + 1);
            }
        }
        return iPsMostVisits(counts);
    }
}
