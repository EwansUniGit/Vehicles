public class Motorcycle extends Vehicle {

    public Motorcycle(Make _make, String _model, Colour _vehicleColour, String _vin, int _year, int _mileage, Gearbox _gearbox,
                      boolean _luggageBox) {
        super(_make, _model, _vehicleColour, _vin, _year, _mileage, _gearbox);
        Options.put("Luggage Box", _luggageBox);
    }

    public boolean getLuggageBox() {
        return Options.get("Luggage Box");
    }

    @Override
    public boolean editOption(boolean option) {

        option = !option;
        return option;
    }
}