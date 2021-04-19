import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

public abstract class Vehicle implements Serializable {
    private final Make make;
    private final String model, vin;
    private final int year;
    private final Gearbox gearbox;
    private Colour vehicleColour;
    private int mileage;
    protected HashMap<String, Boolean> Options;
    protected enum Type {
        ESTATE, HATCHBACK, MOTORCYCLE, SALOON, SUV
    }

    protected enum Make {
        HONDA, AUDI, SKODA, VOLVO, FORD
    }

    protected enum Gearbox {
        MANUAL, AUTOMATIC
    }

    protected enum Colour {
        RED, BLACK, BLUE, GREEN, YELLOW, SILVER
    }

    public Vehicle(Make _make, String _model, Colour _vehicleColour, String _vin, int _year, int _mileage, Gearbox _gearbox) {
        make = _make;
        model = _model;
        vehicleColour = _vehicleColour;
        vin = _vin;
        year = _year;
        mileage = _mileage;
        gearbox = _gearbox;
        Options = new HashMap<String, Boolean>();

    }

    public Make getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public Colour getVehicleColour() {
        return vehicleColour;
    }

    public String getVin() {
        return vin;
    }

    public int getYear() {
        return year;
    }

    public int getMileage() {
        return mileage;
    }

    public Gearbox getGearbox() {
        return gearbox;
    }

    public String outputStringOptions() {
        String output = "";
        for (String key : Options.keySet() ) {
            output += " " + key + ": " + Options.get(key).toString() + "\n";
        }
        return output;
    }

    public String toString() {
        Class c = getClass();
        String vehicleString =
                "VIN: " + getVin() + "\n" +
                        " Vehicle Type: " + c.getSimpleName() + "\n" +
                        " Make: " + getMake() + "\n" +
                        " Model: " + getModel() + "\n" +
                        " Colour: " + getVehicleColour() + "\n" +
                        " Year: " + getYear() + "\n" +
                        " Mileage: " + getMileage() + "\n" +
                        " Gearbox: " + getGearbox() + "\n" +
                        this.outputStringOptions();


        return vehicleString;
    }

    public String describe() {
        Class c = getClass();
        String describe =  getYear() + " " + sentenceCase(getMake().toString()) + " " + getModel() + " " + c.getSimpleName() + " in " + sentenceCase(getVehicleColour().toString());
        return describe;
    }

    public void setMileage(int milesTravelled) {
        if (milesTravelled >= 0) {
            mileage += milesTravelled;
        } else {
            System.out.print("Please enter only positive integers for miles travelled value");
        }
    }



    public void setVehicleColour(Colour newColour) {
        vehicleColour = newColour;
    }

    private static String sentenceCase(String input) {
        String capital = input.substring(0, 1).toUpperCase();
        return capital + input.substring(1).toLowerCase();
    }

    public boolean editOption(boolean option) {

        return false;
    }


        }














