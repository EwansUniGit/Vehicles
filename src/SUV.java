import java.util.HashMap;

public class SUV extends Car {

    public SUV(Make _make, String _model, Colour _vehicleColour, String _vin, int _year, int _mileage, Gearbox _gearbox,
               boolean _sat, boolean _park, boolean _tow, boolean _rack,
               boolean _awd) {
        super(_make, _model, _vehicleColour, _vin, _year, _mileage, _gearbox, _sat, _park, _tow, _rack);
        Options.put("All Wheel Drive Train", _awd);
    }

    public boolean getAllWheelDriveTrain() {
        return Options.get("All Wheel Drive Train");
    }





}