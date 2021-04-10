public class Estate extends Car {
    private boolean thirdRowSeat;

    public Estate(Make _make, String _model, Colour _vehicleColour, String _vin, int _year, int _mileage, Gearbox _gearbox,
                  boolean _sat, boolean _park, boolean _tow, boolean _rack,
                  boolean _thirdRowSeat) {
        super(_make, _model, _vehicleColour, _vin, _year, _mileage, _gearbox, _sat, _park, _tow, _rack);
        Options.put("Third Row Seat", _thirdRowSeat);
    }

    public boolean getThirdRowSeat() {
        return Options.get("Third Row Seat");
    }

}
