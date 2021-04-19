import uod.gla.menu.Finalisable;
import uod.gla.io.File;
import uod.gla.menu.MenuBuilder;
import uod.gla.menu.MenuItem;
import java.util.*;
import java.io.Serializable;
import uod.gla.util.Reader;
import java.util.List;
import java.util.stream.Collectors;


public class AppLauncher implements Finalisable {
    public static List<Vehicle> vehicleList = new ArrayList<>();
    public static HashMap<Vehicle, Date> sales = new HashMap();
    private static File vehicleListFile = new File("data", "inventoryFile");
    private static File salesFile = new File("data", "salesFile");
    public static final int MIN_YEAR = 1900;
    public static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private static AppLauncher appObject = new AppLauncher();

// I decided to retain the createData method as it instantiates a defined set of data which was used in testing. If you wish to replicate
// tests using identical data, first delete DataFiles/inventoryFile and DataFiles/salesFile, then place a call to appObject.createData() in main.
    public static void main(String[] args) {
        List<Vehicle> retrievedVehicleList = vehicleListFile.retrieve(true);
        if (retrievedVehicleList != null) {
            vehicleList = retrievedVehicleList;
        }
        HashMap<Vehicle, Date> retrievedSalesList = salesFile.retrieve(true);
        if (retrievedSalesList != null) {
            sales = retrievedSalesList;
        }
        MenuItem d = new MenuItem("D", "Display all available vehicle records", appObject, "display");
        MenuItem c = new MenuItem("C", "Create a new Vehicle Record", appObject, "create");
        MenuItem s = new MenuItem("S", "Search Vehicles by full or partial VIN", appObject, "search");
        MenuItem e = new MenuItem("E", "Edit options for existing vehicles", appObject, "edit");
        MenuItem t = new MenuItem("T", "Remove a Record", appObject, "remove");
        MenuItem v = new MenuItem("v", "View recently sold vehicles", appObject, "showSales");
        MenuBuilder.displayMenu(appObject, d, c, s, e, t, v);
        appObject.finalise();
        System.out.println("Thank you for using the application.");

    }

    private HashMap<String, Vehicle> MapVins(List<Vehicle> v) {
        HashMap<String, Vehicle> vinMap = new HashMap<>();
        for (Vehicle vehicle : v) {
            vinMap.put(vehicle.getVin(), vehicle);
        }
        return vinMap;
    }

    private String makeVin() {
        String vin = "";
        VinGenerator gen = new VinGenerator();
        while (true) {
            String _vin = gen.generate();
            if (!MapVins(vehicleList).keySet().contains(_vin)) {
                vin = _vin;
                break;
            }
        }
        return vin;
    }

    public static void search() {
        String key = Reader.readLine("Please enter a search key of at least 3 characters: ").toUpperCase();
        if (key.length() > 2 && key.length() < 18) {
            if (getByVin(key) != null) {
                System.out.println(getByVin(key).size() + " match(es) found. Displaying: \n");
                for (Vehicle v : getByVin(key)) {
                    System.out.println(v.toString());
                }
            }
        } else {
            System.out.print("Please enter a search key of appropriate length.  \n");
        }

    }

    private static List<Vehicle> getByVin(String key) {
        List<Vehicle> results = vehicleList.stream()
                .filter(a -> Objects.equals(a.getVin().contains(key), true))
                .collect(Collectors.toList());
        if (results == null || results.isEmpty()) {
            System.out.println("No matches found.");
            return null;
        }
        HashMap<String, Vehicle> resultMap = appObject.MapVins(results);
        String match = Reader.readObject("Please select a vehicle from the list", resultMap.keySet());
        return results;

    }

    public static void remove() {
        HashMap<String, Vehicle> vinMap = appObject.MapVins(vehicleList);
        String key = Reader.readObject(vinMap.size() + " Available Vehicles. Select Vehicle to remove: ", vinMap.keySet());

        String name = vinMap.get(key).getClass().getSimpleName();

        boolean confirm = Reader.readBoolean("This is a " + vinMap.get(key).describe() + ". Is this the vehicle record you wish to remove?");
        if (confirm) {
            vehicleList.remove(vinMap.get(key));
            appObject.finalise();
            boolean sold = Reader.readBoolean("Do you want to add this " + name + " to the sales register?");
            if (sold) {
                sales.put(vinMap.get(key), Calendar.getInstance().getTime());
            }
        } else {

        }
    }

    public static void edit() {
        HashMap<String, Vehicle> vinMap = appObject.MapVins(vehicleList);
        String vehicleKey = Reader.readObject(vinMap.size() + " Available Vehicles. Select Vehicle to edit: ", vinMap.keySet());
        Vehicle vehicle = vinMap.get(vehicleKey);
        String optionKey;
        if (vehicle.Options.keySet().size() > 1) {
            optionKey = Reader.readObject("Available options for selected vehicle- Select which to edit. \n" + vehicle.outputStringOptions(), vehicle.Options.keySet());
            boolean option = vehicle.Options.get(optionKey);
            vehicle.Options.put(optionKey, vehicle.editOption(option));
        } else {
            System.out.print(vehicle.outputStringOptions());
            boolean edit = Reader.readBoolean("Do you wish to edit this option?");
            optionKey = vehicle.Options.keySet().iterator().next();
            if (edit) {
                boolean option = vehicle.Options.get(optionKey);
                vehicle.Options.put(optionKey, vehicle.editOption(option));
            }
        }

        System.out.println(vehicle.outputStringOptions());
        appObject.finalise();
    }

    public static void list() {

        HashMap<String, Vehicle> vinMap = appObject.MapVins(vehicleList);
        String key = Reader.readObject(vinMap.size() + " Available Vehicles. Select Vehicle to display: ", vinMap.keySet());
        System.out.println(vinMap.get(key));
    }


    public static void showSales() {
        HashMap<Vehicle, Date> recentSales = new HashMap<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int months = Reader.readInt("How many months of sales data do you wish to view?", 1, 60);
        System.out.print("Displaying sales data for previous " + months + " month(s): \n \n");
        c.add(Calendar.MONTH, -months);
        Date lastMonth = c.getTime();
        for (Map.Entry<Vehicle, Date> e : sales.entrySet()) {
            if (e.getValue().after(lastMonth))
                recentSales.put(e.getKey(), e.getValue());
        }
        for (Map.Entry<Vehicle, Date> sale : recentSales.entrySet()) {
            System.out.print(sale.getKey().getVin() + " " + sale.getKey().describe() + " \n Date of Sale: " + sale.getValue() + "\n \n");
        }
        System.out.print(recentSales.size() + " sale(s) in specified period. \n");
    }

    public static void create() {
        Vehicle vehicle;
        String vin = appObject.makeVin();
        boolean sat = false, park = false, tow = false, rack = false;
        Vehicle.Type type = Reader.readEnum("Please select which type of vehicle to create: ", Vehicle.Type.class);

        Vehicle.Make mak = Reader.readEnum("Please select a make: ", Vehicle.Make.class);
        String mod = Reader.readLine("Model: ");
        int year = Reader.readInt("Year of manufacture: ", MIN_YEAR, MAX_YEAR);
        int mil = Reader.readInt("Mileage: ", 0, 999999);
        Vehicle.Colour col = Reader.readEnum("Colour: ", Vehicle.Colour.class);
        Vehicle.Gearbox gea = Reader.readEnum("Gearbox: ", Vehicle.Gearbox.class);

        if (type != Vehicle.Type.MOTORCYCLE) {
            sat = Reader.readBoolean("Satnav? ");
            park = Reader.readBoolean("Parking Sensor? ");
            tow = Reader.readBoolean("Towbar? ");
            rack = Reader.readBoolean("Roofrack?");
        }

        switch (type) {
            case ESTATE: {
                boolean seat = Reader.readBoolean("Third Row Seat? ");
                vehicle = new Estate(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack, seat);

                break;
            }
            case HATCHBACK: {
                vehicle = new Hatchback(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack);
                break;
            }
            case MOTORCYCLE: {
                boolean box = Reader.readBoolean("Luggage Box? ");
                vehicle = new Motorcycle(mak, mod, col, vin, year, mil, gea, box);
                break;
            }
            case SALOON: {
                vehicle = new Saloon(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack);
                break;
            }
            case SUV: {
                boolean awd = Reader.readBoolean("All Wheel Drive? ");
                vehicle = new SUV(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack, awd);
                break;
            }
            default: {
                vehicle = null;
            }

        }
        if (vehicle != null) {
            System.out.print(vehicle.getVin() + " - A " + vehicle.describe() + " created successfully. \n");
        }
        vehicleList.add(vehicle);
        appObject.finalise();

    }

    public static void display() {
        for (int i = 0; i < vehicleList.size(); i++) {
            System.out.println(vehicleList.get(i).toString() + "\n");
        }
        System.out.println(vehicleList.size() + " records displayed. \n");
    }


    public void finalise() {
        vehicleListFile.save((Serializable) vehicleList);
        salesFile.save((Serializable) sales);

    }


    //This method should not be needed as the data has been saved in Datafiles/data/inventoryFile, included here for ease of set-up in case of issues
    public void createData() {
        Vehicle vehicles[] = new Vehicle[10];
        vehicles[0] = new Motorcycle(Vehicle.Make.HONDA, "CB 1100 RS", Vehicle.Colour.BLACK, "KMS4OFHG8ZCK7UQM0", 2017, 53245,
                Vehicle.Gearbox.MANUAL, true);
        vehicles[1] = new SUV(Vehicle.Make.AUDI, "Q5 40 TDI Sport", Vehicle.Colour.RED, "5UX8FLCQQ3Y4232DO", 2020, 0,
                Vehicle.Gearbox.AUTOMATIC, true, true, false, true, true);
        vehicles[2] = new Estate(Vehicle.Make.SKODA, "Superb Estate", Vehicle.Colour.SILVER, "5NEHCYNYN46VZBS1Z", 2020, 0,
                Vehicle.Gearbox.AUTOMATIC, true, false, false, false, false);
        vehicles[3] = new Saloon(Vehicle.Make.VOLVO, "S60", Vehicle.Colour.BLACK, appObject.makeVin(), 2019, 203024,
                Vehicle.Gearbox.MANUAL, true, true, true, true);
        vehicles[4] = new Hatchback(Vehicle.Make.FORD, "Fiesta 1.4 TDCi", Vehicle.Colour.YELLOW, "6X16TEJT4A0SN27PE", 2012, 107500,
                Vehicle.Gearbox.MANUAL, false, false, false, false);
        vehicles[5] = new Motorcycle(Vehicle.Make.HONDA, "Fireblade SP", Vehicle.Colour.BLACK, "IUH66MZJM7SOA0HFC", 2020, 0,
                Vehicle.Gearbox.MANUAL, true);
        vehicles[6] = new SUV(Vehicle.Make.FORD, "EcoSport", Vehicle.Colour.RED, "J2O47BII3PYRDGTSD", 2021, 0,
                Vehicle.Gearbox.MANUAL, false, true, true, false, true);
        vehicles[7] = new Estate(Vehicle.Make.SKODA, "Octavia Estate", Vehicle.Colour.GREEN, "9FLF4VGE4LJD31HS0", 2020, 0,
                Vehicle.Gearbox.AUTOMATIC, true, false, true, false, false);
        vehicles[8] = new Saloon(Vehicle.Make.AUDI, "A3", Vehicle.Colour.BLUE, appObject.makeVin(), 2020, 0,
                Vehicle.Gearbox.MANUAL, false, false, true, false);
        vehicles[9] = new Hatchback(Vehicle.Make.HONDA, "Civic", Vehicle.Colour.RED, "H5DVAKEQUZLRL7DH6", 2020, 0,
                Vehicle.Gearbox.AUTOMATIC, false, true, false, false);

        for (int i = 0; i < vehicles.length; i++) {
            vehicleList.add(vehicles[i]);
        }

    }
}

