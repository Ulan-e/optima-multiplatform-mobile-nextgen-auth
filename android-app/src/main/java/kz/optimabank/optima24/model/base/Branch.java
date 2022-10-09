package kz.optimabank.optima24.model.base;

/**
 * Created by Жексенов on 14.11.2014.
 */
public  class Branch  {
    //        private static final long serialVersionUID = 7367936263743995287L;
    private String coordinates, location, name;
    private int id, num, region_id;
    private boolean cash_in;

    public String getCoordinates() {
        return coordinates;
    }
    public String getLocation() {
        return location;
    }
    public String getName() {
        return name;
    }

    public int getAtm_id() {
        return id;
    }
    public int getATM_num() {
        return num;
    }
    public int getRegion_id() {
        return region_id;
    }

    public boolean getCash_in() {
        return cash_in;
    }

}