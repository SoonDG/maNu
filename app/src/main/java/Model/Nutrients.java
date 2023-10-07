package Model;

public class Nutrients {
    private String name;
    private String unit;
    private double amount;
    private  double rec_Max_Amount, rec_Min_Amount;

    public Nutrients(String name, String unit, double amount, double rec_Max_Amount){
        this.name = name;
        this.unit = unit;
        this.amount = amount;
        this.rec_Max_Amount = rec_Max_Amount;
    }

    public Nutrients(String name, String unit, double amount, double rec_Max_Amount, double rec_Min_Amount){
        this.name = name;
        this.unit = unit;
        this.amount = amount;
        this.rec_Max_Amount = rec_Max_Amount;
        this.rec_Min_Amount = rec_Min_Amount;
    }

    public double getAmount() {
        return amount;
    }

    public double getRec_Max_Amount() {
        return rec_Max_Amount;
    }

    public void setRec_Min_Amount(double rec_Min_Amount) {
        this.rec_Min_Amount = rec_Min_Amount;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRec_Min_Amount() {
        return rec_Min_Amount;
    }

    public void setRec_Max_Amount(double rec_Max_Amount) {
        this.rec_Max_Amount = rec_Max_Amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
