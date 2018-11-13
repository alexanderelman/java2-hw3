package edu.acc.java2.taxreport;

/**
 *  @author alexanderelman
 */

public class ZipCodeReturn {
    private String state; // Two-digit State abbreviation code
    private String zipcode; // Five digit zip code
    /*
    size of adjusted gross income
     1 = $1 under $25,000
     2 = $25,000 under $50,000
     3 = $50,000 under $75,000
     4 = $75,000 under $100,000
     5 = $100,000 under $200,000
     6 = $200,000 or more
    */
    private Integer numReturns;
    private Double adjustedGrossIncome; // in thousands of dollars
    private Integer numReturnsTaxesPaid;
    private Integer numReturnsTaxesDueFiling;
    private Double taxesPaid; // in thousands of dollars
    private Double taxDueAtFilingTime; // in thousands of dollars

    public ZipCodeReturn() {

    }
    public ZipCodeReturn(String state, String zipcode) {
        this.state = state;
        this.zipcode = zipcode;
    }

    public ZipCodeReturn(String state, String zipcode, Integer numReturns, Double adjustedGrossIncome,
                         Integer numReturnsTaxesPaid, Integer numReturnsTaxesDueFiling, Double taxesPaid,
                         Double taxDueAtFilingTime) {
        this.state = state;
        this.zipcode = zipcode;
        this.numReturns = numReturns;
        this.adjustedGrossIncome = adjustedGrossIncome;
        this.numReturnsTaxesPaid = numReturnsTaxesPaid;
        this.numReturnsTaxesDueFiling = numReturnsTaxesDueFiling;
        this.taxesPaid = taxesPaid;
        this.taxDueAtFilingTime = taxDueAtFilingTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Integer getNumReturns() {
        return numReturns;
    }

    public void setNumReturns(Integer numReturns) {
        this.numReturns = numReturns;
    }

    public Double getAdjustedGrossIncome() {
        return adjustedGrossIncome;
    }

    public void setAdjustedGrossIncome(Double adjustedGrossIncome) {
        this.adjustedGrossIncome = adjustedGrossIncome;
    }

    public Integer getNumReturnsTaxesPaid() {
        return numReturnsTaxesPaid;
    }

    public void setNumTaxReturnsPaid(Integer numReturnsTaxesPaid) {
        this.numReturnsTaxesPaid = numReturnsTaxesPaid;
    }

    public Integer getNumTaxReturnsDueAtFiling() {
        return numReturnsTaxesDueFiling;
    }

    public Double getTaxesPaid() {
        return taxesPaid;
    }

    public void setTaxesPaid(Double taxesPaid) {
        this.taxesPaid = taxesPaid;
    }

    public void setNumTaxReturnsDueAtFiling(Integer numReturnsTaxesDueFiling) {
        this.numReturnsTaxesDueFiling = numReturnsTaxesDueFiling;
    }

    public Double getTaxesDueAtFiling() {
        return taxDueAtFilingTime;
    }

    public void setTaxesDueAtFiling(Double taxDueAtFilingTime) {
        this.taxDueAtFilingTime = taxDueAtFilingTime;
    }

    public Double getPerCapitaAgi() {
        return (this.adjustedGrossIncome * 1000.0 / this.numReturns);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "ZipCodeReturn{" +
                "state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}