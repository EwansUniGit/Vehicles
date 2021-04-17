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
    private static List<Vehicle> vehicleList = new ArrayList<>();
    private static File vehicleListFile = new File("data", "inventoryFile");
    private static AppLauncher appObject = new AppLauncher();
    private static VehicleFactory builder = new VehicleFactory();


    public static void main(String[] args) {
        appObject.createData();
        List<Vehicle> retrievedVehicleList = vehicleListFile.retrieve(true);
        if (retrievedVehicleList != null) {
            vehicleList = retrievedVehicleList;
        }

        MenuItem d = new MenuItem("D", "Display all available vehicle records", appObject, "display");
        MenuItem c = new MenuItem("C", "Create a new Vehicle Record", appObject, "create" );
        MenuItem s = new MenuItem("L", "Search existing records", appObject, "search");
        MenuItem e = new MenuItem("E", "Add or Remove options from existing vehicles", appObject, "edit");
        MenuItem del = new MenuItem("del", "Delete a Record", appObject, "remove");
        MenuBuilder.displayMenu(appObject, d, c, s, e, del);
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
        if (key.length() >2 && key.length() < 18) {
            if (getByVin(key) != null) {
                System.out.println(getByVin(key).toString());
            }
        }
        else {
            System.out.print("Please enter a search key of appropriate length.  \n");
        }

    }

    private static Vehicle getByVin(String key) {
        List<Vehicle> results = vehicleList.stream()
                .filter(a -> Objects.equals(a.getVin().contains(key), true))
                .collect(Collectors.toList());
        if (results == null || results.isEmpty()) {
            System.out.println("No matches found.");
            return null;
        }
        HashMap<String, Vehicle> resultMap = appObject.MapVins(results);
        String match = Reader.readObject("Please select a vehicle from the list", resultMap.keySet());
        return resultMap.get(match);

    }

    public static void remove() {
        HashMap<String, Vehicle> vinMap = appObject.MapVins(vehicleList);
        String key = Reader.readObject(vinMap.size() + " Available Vehicles. Select Vehicle to remove: ", vinMap.keySet());
        vehicleList.remove(vinMap.get(key));
    }


    public static void edit() {
        HashMap<String, Vehicle> vinMap = appObject.MapVins(vehicleList);
        String vehicleKey = Reader.readObject(vinMap.size() + " Available Vehicles. Select Vehicle to edit: ", vinMap.keySet());
        Vehicle vehicle = vinMap.get(vehicleKey);
        String optionKey = Reader.readObject("Available options for selected vehicle- Select which to edit. \n" + vehicle.outputStringOptions(), vehicle.Options.keySet());
        boolean option = vehicle.Options.get(optionKey);
        System.out.println(vehicle.outputStringOptions());
        vehicle.Options.put(optionKey, vehicle.editOption(option));
    }
    public static void list() {

        HashMap<String, Vehicle> vinMap = appObject.MapVins(vehicleList);
        String key = Reader.readObject(vinMap.size() + " Available Vehicles. Select Vehicle to display: ", vinMap.keySet());
        System.out.println(vinMap.get(key));
    }



    public static Vehicle create() {
        Vehicle vehicle;
        String vin = appObject.makeVin();
        boolean sat = false, park = false , tow = false, rack = false;
        Vehicle.Type type = Reader.readEnum("Please select which type of vehicle to create: ", Vehicle.Type.class);

        Vehicle.Make mak = Reader.readEnum("Please select a make: ", Vehicle.Make.class);
        String mod = Reader.readName("Model: ");
        int year = Reader.readInt("Year of construction: ", Vehicle.MIN_YEAR, Vehicle.MAX_YEAR);
        int mil = Reader.readInt("Mileage: ", 0, 999999);
        Vehicle.Colour col = Reader.readEnum("Colour: ", Vehicle.Colour.class);
        Vehicle.Gearbox gea = Reader.readEnum("Gearbox: ", Vehicle.Gearbox.class);

        if (type != Vehicle.Type.MOTORCYCLE){
             sat = Reader.readBoolean("Satnav? ");
             park = Reader.readBoolean("Parking Sensor? ");
             tow = Reader.readBoolean("Towbar? ");
             rack = Reader.readBoolean("Roofrack?");
        }

        switch (type) {
            case ESTATE: {
                boolean seat = Reader.readBoolean("Third Row Seat? ");
                return new Estate(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack, seat);
                break;
            }
            case HATCHBACK: {
                return new Hatchback(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack);
                break;
            }
            case MOTORCYCLE: {
                vehicle = builder.getVehicle("MOTORCYCLE");
                break;
            }
            case SALOON: {
                vehicle = builder.getVehicle("SALOON");
                break;
            }
            case SUV: {
                vehicle = builder.getVehicle("SUV");
                break;
            }
            default: {
                vehicle = null;
            }
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
        vehicleListFile.save((Serializable)vehicleList);

    }




    //This method should not be needed as the data has been saved in Datafiles/data/inventoryFile, included here for ease of set-up in case of issues
    public void createData() {
        Vehicle vehicles[] = new Vehicle[5];
        vehicles[0] = new Motorcycle(Vehicle.Make.HONDA, "CB 1100 RS", Vehicle.Colour.BLACK, "2G61L5S35E9243271", 2017, 53245,
                Vehicle.Gearbox.MANUAL, true);
        vehicles[1] = new SUV(Vehicle.Make.AUDI, "Q5 40 TDI Sport", Vehicle.Colour.RED, "5NPE24AFXFH164846", 2020, 0,
                Vehicle.Gearbox.AUTOMATIC, true, true, false, true, true);
        vehicles[2] = new Estate(Vehicle.Make.SKODA, "Superb Estate", Vehicle.Colour.SILVER, "2T2HA31U46C041163", 2020, 0,
                Vehicle.Gearbox.AUTOMATIC, true, false, false, false, false);
        vehicles[3] = new Saloon(Vehicle.Make.VOLVO, "S60", Vehicle.Colour.BLACK, "3C3CFFER0CT110709", 2019, 203024,
                Vehicle.Gearbox.MANUAL, true, true, true, true);
        vehicles[4] = new Hatchback(Vehicle.Make.FORD, "Fiesta 1.4 TDCi", Vehicle.Colour.YELLOW, "3C3CFFER0CT110709", 2012, 107500,
                Vehicle.Gearbox.MANUAL, false, false, false, false);
        for (int i = 0; i < vehicles.length; i++) {
            vehicleList.add(vehicles[i]);
        }

    }
}

