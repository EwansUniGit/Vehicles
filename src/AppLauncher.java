import uod.gla.menu.Finalisable;
import uod.gla.io.File;
import uod.gla.menu.MenuBuilder;
import uod.gla.menu.MenuItem;
import java.util.HashMap;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import uod.gla.util.CollectionUtils;
import uod.gla.util.Reader;



public class AppLauncher implements Finalisable {
    private static List<Vehicle> vehicleList = new ArrayList<>();
    private static File vehicleListFile = new File("data", "inventoryFile");
    private static AppLauncher appObject = new AppLauncher();
    private static VehicleFactory factory = new VehicleFactory();

    public static void main(String[] args) {
        appObject.createData();
        List<Vehicle> retrievedVehicleList = vehicleListFile.<List<Vehicle>>retrieve(true);
        if (retrievedVehicleList != null) {
            vehicleList = retrievedVehicleList;
        }

        MenuItem d = new MenuItem("D", "Display all available vehicle records", appObject, "display");
        MenuItem c = new MenuItem("C", "Create a new Vehicle Record", appObject, "create" );
        MenuItem s = new MenuItem("L", "Search existing records", appObject, "list");
        MenuItem e = new MenuItem("E", "Add or Remove options from existing vehicles", appObject, "edit");
        MenuItem del = new MenuItem("del", "Delete a Record", appObject, "remove");
        MenuBuilder.displayMenu(appObject, d, c, s, e, del);
        appObject.finalise();
        System.out.println("Thank you for using the application.");

    }


    private HashMap<String, Vehicle> MapVins(List<Vehicle> v) {
        HashMap<String, Vehicle> vinMap = new HashMap<String, Vehicle>();
        for (int i = 0; i < v.size(); i++) {
            vinMap.put(v.get(i).getVin() + v.get(i).describe(), v.get(i));
        }
        return vinMap;
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
        vehicle.Options.put(optionKey, vehicle.editOption(option));
    }
    public static void list() {

        HashMap<String, Vehicle> vinMap = appObject.MapVins(vehicleList);
        String key = Reader.readObject(vinMap.size() + " Available Vehicles. Select Vehicle to display: ", vinMap.keySet());
        System.out.println(vinMap.get(key));
    }



    public static void create() {
        Vehicle vehicle;

        Vehicle.Type type = Reader.readEnum("Please select which type of vehicle to create: ", Vehicle.Type.class);

        switch (type) {
            case ESTATE: {
                vehicle = factory.getVehicle("ESTATE");
                break;
            }
            case HATCHBACK: {
                vehicle = factory.getVehicle("HATCHBACK");
                break;
            }
            case MOTORCYCLE: {
                vehicle = factory.getVehicle("MOTORCYCLE");
                break;
            }
            case SALOON: {
                vehicle = factory.getVehicle("SALOON");
                break;
            }
            case SUV: {
                vehicle = factory.getVehicle("SUV");
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

