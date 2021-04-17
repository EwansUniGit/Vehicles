import uod.gla.util.Reader;

import java.util.ArrayList;
import java.util.List;

public class VehicleBuilder {
    
    public static List<String> vins = new ArrayList<>();


    public Vehicle getVehicle(String vehicleType) {

        List<String> vins = new ArrayList<>();
        String vin = makeVin();



        Vehicle.Make mak = Reader.readEnum("Please select a make: ", Vehicle.Make.class);
        String mod = Reader.readName("Model: ");
        int year = Reader.readInt("Year of construction: ", Vehicle.MIN_YEAR, Vehicle.MAX_YEAR);
        int mil = Reader.readInt("Mileage: ", 0, 999999);
        Vehicle.Colour col = Reader.readEnum("Colour: ", Vehicle.Colour.class);
        Vehicle.Gearbox gea = Reader.readEnum("Gearbox: ", Vehicle.Gearbox.class);
        boolean sat = false, park = false , tow = false, rack = false;
        if (vehicleType.equalsIgnoreCase("ESTATE") || vehicleType.equalsIgnoreCase("HATCHBACK") || vehicleType.equalsIgnoreCase("SALOON") || vehicleType.equalsIgnoreCase("SUV")){
            sat = Reader.readBoolean("Satnav? ");
            park = Reader.readBoolean("Parking Sensor? ");
            tow = Reader.readBoolean("Towbar? ");
            rack = Reader.readBoolean("Roofrack?");

        }





        if (vehicleType == null ) {
            return null;
        }
        else if (vehicleType.equalsIgnoreCase("ESTATE")){
            boolean seat = Reader.readBoolean("Third Row Seat? ");
            return new Estate(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack, seat);

        }
        else if (vehicleType.equalsIgnoreCase("HATCHBACK")){return new Hatchback(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack);}
        else if (vehicleType.equalsIgnoreCase("MOTORCYCLE")){
            boolean box = Reader.readBoolean("Luggage Box? ");
            return new Motorcycle(mak, mod, col, vin, year, mil, gea, box);
        }
        else if (vehicleType.equalsIgnoreCase("SALOON")){return new Saloon(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack);}
        else if (vehicleType.equalsIgnoreCase("SUV")){
            boolean awd = Reader.readBoolean("All Wheel Drive? ");
            return new SUV(mak, mod, col, vin, year, mil, gea, sat, park, tow, rack, awd);
        }
        return null;
    }
}
