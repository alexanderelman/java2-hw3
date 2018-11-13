package edu.acc.java2.taxreport;

import edu.acc.java2.taxreport.ZipCodeReturn;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.stream.*;

/**
 *  @author alexanderelman
 */

public class Main {
    public static void main( String[] args ) {
        final String USAGE = "Usage: java -jar taxes.jar {path-to-tax-dataset.csv}";

        if (args.length != 1) {
            System.out.println(USAGE);
            return;
        }

        // Load data from file into a Map
        Map<String,ZipCodeReturn> zipCodeMap = loadTaxDataFromFile(args[0]);
        if (zipCodeMap.isEmpty()) {
            System.out.println("Couldn't load tax data!");
        }

        // What was the total number of income tax returns filed in the US in 2014?
        printTotalNumIncomeTaxReturns(zipCodeMap);

        // What was the total amount of income tax collected in the US in 2014?
        printTotalIncomeTaxCollected(zipCodeMap);

        List<ZipCodeReturn> zipCodeList = new ArrayList<>(zipCodeMap.values());

        // What zip code had the lowest per capita adjusted gross income in the US?
        // What was the average income per capita in this zip code?
        printLowestPerCapitaAgiZip(zipCodeList);

        // What zip code had the highest per capita adjusted gross income in the US?
        // What was the average income per capita in this zip code?
        printHighestPerCapitaAgiZip(zipCodeList);

        // What zip code paid the most federal tax in the US?
        printHighestTaxes(zipCodeList);

        // What was the average per capita income in your zip code?
        System.out.printf("Average per capita income in 78702 is $%.2f.\n", zipCodeMap.get("78702").getPerCapitaAgi());

        return;
    }

    public static String splitAtPeriod(String data) {
        return data.split("\\.")[0];
    }

    /**
     * Prints the total number of income tax returns in 2014 by iterating through
     * all zip codes and their agi classes and summing the number of returns.
     * @param zipCodeMap A map of each zip code
     * @return none
     */
    public static void printTotalNumIncomeTaxReturns(Map<String, ZipCodeReturn> zipCodeMap) {
        Integer totalNumIncomeTaxReturns = 0;
        Set<String> keys = zipCodeMap.keySet();
        for (String key: keys) {
            totalNumIncomeTaxReturns += zipCodeMap.get(key).getNumReturns();
        }
        System.out.printf("Total number of income tax returns in the US this year was %d.\n", totalNumIncomeTaxReturns);
    }

    /**
     * prints the total taxes paid in 2014 by iterating through all zip codes and their agi
     * classes and summing the total taxes paid.
     * @param zipCodeMap A map of each zip code object
     * @return none
     */
    public static void printTotalIncomeTaxCollected(Map<String, ZipCodeReturn> zipCodeMap) {
        Double totalIncomeTaxCollected = 0.0;
        Set<String> keys = zipCodeMap.keySet();
        for (String key: keys) {
            totalIncomeTaxCollected += zipCodeMap.get(key).getTaxesPaid();
        }
        totalIncomeTaxCollected *= 1000.0; // scale value by 1000
        System.out.printf("Total income tax collected in the US this year was $%.2f \n", totalIncomeTaxCollected);
    }

    /**
     * Prints the zip code with the lowest per capita agi and average income per capita
     * @param zipCodeList A list of each zip code object
     * @return none
     */
    public static void printLowestPerCapitaAgiZip(List<ZipCodeReturn> zipCodeList) {
        ZipCodeReturn zip = zipCodeList.stream()
                .sorted(Comparator.comparing(ZipCodeReturn::getPerCapitaAgi))
                .findFirst()
                .get();

        System.out.printf("The zip with the lowest per capita AGI in the US this year was %s at $%.2f. \n",
                zip.getZipcode(), zip.getPerCapitaAgi());
    }

    /**
     * Prints the zip code with the highest per capita agi and average income per capita
     * @param zipCodeList A list of each zip code object
     * @return none
     */
    public static void printHighestPerCapitaAgiZip(List<ZipCodeReturn> zipCodeList) {
        ZipCodeReturn zip = zipCodeList.stream()
                .sorted(Comparator.comparing(ZipCodeReturn::getPerCapitaAgi).reversed())
                .findFirst()
                .get();

        System.out.printf("The zip with the highest per capita AGI in the US this year was %s at $%.2f. \n",
                zip.getZipcode(), zip.getPerCapitaAgi());
    }

    public static void printHighestTaxes(List<ZipCodeReturn> zipCodeList) {
        ZipCodeReturn zip = zipCodeList.stream()
                .sorted(Comparator.comparing(ZipCodeReturn::getTaxesPaid).reversed())
                .findFirst()
                .get();
        System.out.printf("The zip that paid the most taxes is %s with $%.2f. \n",
                zip.getZipcode(), zip.getTaxesPaid()*1000);
    }

    public static Map<String,ZipCodeReturn> loadTaxDataFromFile(String fileName) {
        Map<String, ZipCodeReturn> zipCodeMap = new HashMap<String, ZipCodeReturn>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read the column headers from the first line
            String[] columns = reader.readLine().split(",");

            // Get the positions of the columns we care about
            int stateIndex=-1;
            int zipIndex=-1;
            int numReturnsIndex=-1;
            int agiIndex=-1;
            int numReturnsTaxesPaidIndex=-1;
            int numReturnsTaxesDueAtFilingIndex=-1;
            int taxesPaidIndex=-1;
            int taxDueAtFilingIndex=-1;

            String state;
            String zipcode;
            Integer numReturns;
            Double agi;
            Integer numReturnsTaxesPaid;
            Integer numReturnsTaxesDueAtFiling;
            Double taxesPaid;
            Double taxDueAtFiling;

            String[] la; // line array
            String line;

            line = reader.readLine();

            for (int i=0; i<columns.length; i++) {
                if (columns[i].equals("STATE")) {
                    stateIndex = i;
                }
                else if (columns[i].equals("zipcode")) {
                    zipIndex = i;
                }
                else if (columns[i].equals("N1")) {
                    numReturnsIndex = i;
                }
                else if (columns[i].equals("A00100")) {
                    agiIndex = i;
                }
                else if (columns[i].equals("A18300")) {
                    taxesPaidIndex = i;
                }
                else if (columns[i].equals("N18300")) {
                    numReturnsTaxesPaidIndex = i;
                }
                else if (columns[i].equals("N11901")) {
                    numReturnsTaxesDueAtFilingIndex = i;
                }
                else if (columns[i].equals("A11901")) {
                    taxDueAtFilingIndex = i;
                }
                else {
                    continue;
                }
            }

            while (line != null) {
                la = line.split(",");
                state = la[stateIndex];
                zipcode = la[zipIndex];
                numReturns = Integer.parseInt(splitAtPeriod(la[numReturnsIndex]));
                agi = Double.parseDouble(splitAtPeriod(la[agiIndex]));
                numReturnsTaxesPaid = Integer.parseInt(splitAtPeriod(la[numReturnsTaxesPaidIndex]));
                numReturnsTaxesDueAtFiling = Integer.parseInt(splitAtPeriod(la[numReturnsTaxesDueAtFilingIndex]));
                taxesPaid = Double.parseDouble(splitAtPeriod(la[taxesPaidIndex]));
                taxDueAtFiling = Double.parseDouble(splitAtPeriod(la[taxDueAtFilingIndex]));

                // Skip zip code 00000 as it is an old mining town and appears over 300 times in the data
                // http://www.washingtonpost.com/wp-dyn/content/article/2003/04/02/AR2005033108150.html
                // Skip zip code 99999 ZIP codes with less than 100 returns and those identified as a single building
                // or nonresidential ZIP code were categorized as “other” (99999).

                if (zipcode.equals("00000") || zipcode.equals("99999")) {
                    line = reader.readLine();
                    continue;
                }

                if (zipCodeMap.containsKey(zipcode)) {
                    ZipCodeReturn zcr = zipCodeMap.get(zipcode);
                    zcr.setNumReturns(zcr.getNumReturns() + numReturns);
                    zcr.setAdjustedGrossIncome(zcr.getAdjustedGrossIncome() + agi);
                    zcr.setNumTaxReturnsPaid(zcr.getNumReturnsTaxesPaid() + numReturnsTaxesPaid);
                    zcr.setNumTaxReturnsDueAtFiling(zcr.getNumTaxReturnsDueAtFiling() + numReturnsTaxesDueAtFiling);
                    zcr.setTaxesPaid(zcr.getTaxesPaid() + taxesPaid);
                    zcr.setTaxesDueAtFiling(zcr.getTaxesDueAtFiling() + taxDueAtFiling);
                }
                else {
                    zipCodeMap.put(zipcode,new ZipCodeReturn(state,zipcode,numReturns,agi,numReturnsTaxesPaid,
                            numReturnsTaxesDueAtFiling,taxesPaid,taxDueAtFiling));
                }
                line = reader.readLine();
            }
        }
        catch (FileNotFoundException filenotfoundexception) {
            System.out.printf("File %s could not be found. \n\n%s\n", fileName, filenotfoundexception);
        }
        catch (IOException ioexception) {
            System.out.printf("IOException trying to access file %s" +
                    "\n\n %s\n", fileName, ioexception);
        }
        return zipCodeMap;
    }
}