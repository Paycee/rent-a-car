import java.util.ArrayList;
import java.time.LocalDate;

public class Car {
    private String brand;
    private String model;
    private int year;
    private ArrayList<LocalDate> dates = new ArrayList();
    private double price;

    public Car(){};

    public Car(String brand, String model, int year, double price, ArrayList<LocalDate> dates) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.dates = dates;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<LocalDate> getDates() {
        return dates;
    }

    public void setDates(ArrayList<LocalDate> dates) {
        this.dates = dates;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void ispis()
    {
        System.out.print( "Automobil{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", dates=[" );
        for(int i = 0;i< dates.size();i++)
        {
            System.out.print(dates.get(i)+" ");
        }
        System.out.println("] }");
    }



}
