import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class RentACar extends JFrame {

    private JPanel qwerty;
    private JLabel labelHead;
    private JLabel labelDays;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
    private JLabel labelPrice;
    private JTextField textDays;
    private JTextField dateOfText;
    private JButton rentButton;
    private JLabel dateOfLabel;
    private JButton deleteButton;
    private JPanel unAvailableDatesPanel;
    private JPanel carPanel;
    private JPanel rentPanel;
    private JTextArea textDates;

    private ArrayList<Car> cars;

    public RentACar(String title) {
        super(title);
        this.setSize(1050, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(qwerty);


        textDates.setEditable(false);
        textDates.setCursor(null);
        textDates.setOpaque(false);
        textDates.setFocusable(false);

        // Inicijalizacija liste automobila
        cars = new ArrayList<>();
        loadCarsFromFile();
        populateComboBoxes();
        setupListeners();
    }

    // Metoda za učitavanje automobila iz fajla
    private void loadCarsFromFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("cars.txt"));
            String red;
            while ((red = reader.readLine()) != null) {
                String[] delovi = red.split("\\s+");
                String brand = delovi[0];
                String model = delovi[1];
                int year = Integer.parseInt(delovi[2]);
                double price = Double.parseDouble(delovi[3]);

                ArrayList<LocalDate> dates = new ArrayList<>();
                for (int i = 4; i < delovi.length; i++) {
                    LocalDate datum = LocalDate.parse(delovi[i], formatter);
                    dates.add(datum);
                }

                Car auto = new Car(brand, model, year, price, dates);
                cars.add(auto);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Popunjavanje ComboBox-ova
    private void populateComboBoxes() {
        HashSet<String> brands = new HashSet<>();
        for (Car car : cars) {
            brands.add(car.getBrand());
        }
        for (String brand : brands) {
            comboBox1.addItem(brand);
        }
    }

    // Definisanje listener-a
    private void setupListeners() {
        comboBox1.addActionListener(e -> updateModels());
        comboBox2.addActionListener(e -> updateYears());
        comboBox3.addActionListener(e -> updatePrice());
        textDays.addActionListener(e -> displayUnAvailableDates());
        deleteButton.addActionListener(e -> resetFields());
        rentButton.addActionListener(e -> rentACar());
    }

    // Ažuriranje modela
    private void updateModels() {
        comboBox2.removeAllItems();
        String brand = (String) comboBox1.getSelectedItem();
        if (brand != null) {
            cars.stream()
                    .filter(car -> car.getBrand().equals(brand))
                    .map(Car::getModel)
                    .distinct()
                    .forEach(comboBox2::addItem);
        }
    }

    // Ažuriranje godina
    private void updateYears() {
        comboBox3.removeAllItems();
        String model = (String) comboBox2.getSelectedItem();
        if (model != null) {
            cars.stream()
                    .filter(car -> car.getModel().equals(model))
                    .map(car -> String.valueOf(car.getYear()))
                    .distinct()
                    .forEach(comboBox3::addItem);
        }
    }

    // Prikaz cene
    private void updatePrice() {
        String brand = (String) comboBox1.getSelectedItem();
        String model = (String) comboBox2.getSelectedItem();
        String year = (String) comboBox3.getSelectedItem();

        if (brand != null && model != null && year != null) {
            cars.stream()
                    .filter(car -> car.getBrand().equals(brand) && car.getModel().equals(model) && String.valueOf(car.getYear()).equals(year))
                    .findFirst()
                    .ifPresent(car -> labelPrice.setText("Cena za odabrani automobil po danu je " + car.getPrice() + "€"));
        }
    }

    // Metoda za prikaz datuma
    private void displayUnAvailableDates() {

        String brand = comboBox1.getSelectedItem().toString();
        String model = comboBox2.getSelectedItem().toString();
        int year = Integer.parseInt(comboBox3.getSelectedItem().toString());


        textDates.setText("Zauzeti datumi su:");
        for (Car car : cars) {
            if (car.getBrand().equals(brand) && car.getModel().equals(model) && car.getYear() == year) {
                ArrayList<LocalDate> localDates = car.getDates();
                ArrayList<String> dates = new ArrayList<>();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                String now = LocalDate.now().format(formatter);

                for(int l = 0; l < localDates.size();l++)
                {

                    dates.add(localDates.get(l).format(formatter));
                }


                if(dates.size() % 2 == 1)
                    dates.add(0, now);

                for(int j = 0; j<dates.size();j+=2)
                    textDates.setText(textDates.getText()+"\n"+dates.get(j)+" - " + dates.get(j + 1));
            }
        }

    }

    private void rentACar()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy.");

        try {
            LocalDate dateOf = LocalDate.parse(dateOfText.getText(), formatter);
            LocalDate dateTo = dateOf.plusDays(Integer.parseInt(textDays.getText()));

            if (textDays.getText().isEmpty())
                throw new IllegalArgumentException();

            String brand = comboBox1.getSelectedItem().toString();
            String model = comboBox2.getSelectedItem().toString();
            int year = Integer.parseInt(comboBox3.getSelectedItem().toString());
            double price = 0;

            boolean x = false;
            if(dateOf.isAfter(LocalDate.now()) || dateOf.equals(LocalDate.now()))
                for (Car car : cars) {
                    if (car.getBrand().equals(brand) && car.getModel().equals(model) && car.getYear() == year)
                    {
                        ArrayList<LocalDate> dates = car.getDates();
                        price = car.getPrice();

                        if(dates.size() % 2 == 1)
                            dates.add(0, LocalDate.now());

                        for(int j = 0; j<dates.size();j+=2)
                        {
                            if((j==0 && dates.get(j).isAfter(dateTo)) ||
                                    (j == dates.size()-2 && dates.get(j+1).isBefore(dateOf)))
                            {x = true; break;}
                            else if(j!=0 && (dates.get(j-1).isBefore(dateOf) && dates.get(j).isAfter(dateTo)))
                            {x = true; break;}
                        }
                    }
                }

            if(x)
            {

                try {
                    List<String> lines = Files.readAllLines(Paths.get("cars.txt"));

                    List<String> updatedLines = lines.stream()
                            .map(line -> {
                                if (line.startsWith(brand + " " + model + " " + year)) {
                                    return line + " " + dateOf + " " + dateTo;
                                }
                                return line;
                            })
                            .collect(Collectors.toList());

                    Files.write(Paths.get("cars.txt"), updatedLines);

                    MessageBox err = new MessageBox("Uspesno ste iznajmili automobil."+ "\n" +"Cena je " +price*Integer.parseInt(textDays.getText())+"€.");
                    err.setVisible(true);
                } catch (IOException e) {
                    System.out.println("Greška prilikom ažuriranja fajla: " + e.getMessage());
                }
            }else {
                MessageBox err = new MessageBox("Nemamo termin za Vas.");
                err.setVisible(true);
            }

        }catch (DateTimeParseException e)
        {
            MessageBox err = new MessageBox("Niste ispravno uneli datum.");
            err.setVisible(true);
        }catch (IllegalArgumentException e)
        {
            MessageBox err = new MessageBox("Polje ne sme biti prazno.");
            err.setVisible(true);
        }
    }
    // Resetovanje polja
    private void resetFields() {
        comboBox2.removeAllItems();
        comboBox3.removeAllItems();
        labelPrice.setText("");
        textDays.setText("");
        dateOfText.setText("");
        textDates.setText("");
        cars.clear();
        loadCarsFromFile();
    }

}