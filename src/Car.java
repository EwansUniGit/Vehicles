
public abstract class Car extends Vehicle {

    public Car(Make _make, String _model, Colour _vehicleColour, String _vin, Integer _year, Integer _mileage, Gearbox _gearbox,
               boolean _sat, boolean _park, boolean _tow, boolean _rack) {
        super(_make, _model, _vehicleColour, _vin, _year, _mileage, _gearbox);
        Options.put("Satnav", _sat);
        Options.put("Parking Sensor", _park);
        Options.put("Tow Bar", _tow);
        Options.put("Roof Rack", _rack);
    }

    public boolean getSatnav() {
        return Options.get("Satnav");
    }

    public boolean getParkingSensor() {
        return Options.get("Parking Sensor");
    }

    public boolean getTowBar() {
        return Options.get("Tow Bar");
    }

    public boolean getRoofRack() {
        return Options.get("Roof Rack");
    }

    @Override
    public boolean editOption(boolean option) {
        super.editOption(option);
        if (option == true) {
            System.out.print("This car is already fitted with that option. \n");
        }
        return true;
    }


}

