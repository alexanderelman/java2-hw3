package edu.acc.java2.taxreport;

import edu.acc.java2.taxreport.ZipCodeReturn;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Collection;;

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

        // Load data from file into a List
        List<ZipCodeReturn> taxPayerList = loadTaxDataFromFile(args[0]);
        if (taxPayerList.isEmpty()) {
            System.out.println("Couldn't load tax data!");
        }

        // What was the total number of income tax returns filed in the US in 2014?
        Integer totalNumIncomeTaxReturns = getTotalNumIncomeTaxReturns(taxPayerList);
        System.out.printf("Total number of income tax returns this year was %d.\n", totalNumIncomeTaxReturns);

        return;
    }



    public static String splitAtPeriod(String data) {
        return data.split("\\.")[0];
    }

    public static Integer getTotalNumIncomeTaxReturns(List<ZipCodeReturn> taxPayerList) {
        Integer totalNumIncomeTaxReturns = 0;
        ListIterator<ZipCodeReturn> li = taxPayerList.listIterator();
        while(li.hasNext()) {
            totalNumIncomeTaxReturns += li.next().getNumReturns();
        }
        return totalNumIncomeTaxReturns;
    }

    public static List<ZipCodeReturn> loadTaxDataFromFile(String fileName) {
        List<ZipCodeReturn> taxPayerList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read the column headers from the first line
            String[] columns = reader.readLine().split(",");

            // Get the positions of the columns we care about
            int stateIndex=-1;
            int zipIndex=-1;
            int agiSizeIndex=-1;
            int numReturnsIndex=-1;
            int agiIndex=-1;
            int numReturnsTaxesPaidIndex=-1;
            int numReturnsTaxesDueAtFilingIndex=-1;
            int taxesPaidIndex=-1;
            int taxDueAtFilingIndex=-1;

            String state;
            String zipcode;
            Integer agiSize;
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
                else if (columns[i].equals("agi_stub")){
                    agiSizeIndex = i;
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
                agiSize = Integer.parseInt(la[agiSizeIndex]);
                numReturns = Integer.parseInt(splitAtPeriod(la[numReturnsIndex]));
                agi = Double.parseDouble(splitAtPeriod(la[agiIndex]));
                numReturnsTaxesPaid = Integer.parseInt(splitAtPeriod(la[numReturnsTaxesPaidIndex]));
                numReturnsTaxesDueAtFiling = Integer.parseInt(splitAtPeriod(la[numReturnsTaxesDueAtFilingIndex]));
                taxesPaid = Double.parseDouble(splitAtPeriod(la[taxesPaidIndex]));
                taxDueAtFiling = Double.parseDouble(splitAtPeriod(la[taxDueAtFilingIndex]));

                taxPayerList.add(new ZipCodeReturn(state,zipcode,agiSize,numReturns,agi,numReturnsTaxesPaid,
                        numReturnsTaxesDueAtFiling,taxesPaid,taxDueAtFiling));
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
        return taxPayerList;
    }
}