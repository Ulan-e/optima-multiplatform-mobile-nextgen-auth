package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.db.entry.ForeignBank;

/**
  Created by Timur on 13.05.2017.
 */

public class DictionaryResponse {
//    @SerializedName("seco")
//    public ArrayList<Dictionary> seco;
    @SerializedName("knp")
    public ArrayList<Dictionary> knp;
    @SerializedName("bic")
    public ArrayList<Dictionary> bic;
    @SerializedName("voCodes")
    public ArrayList<Dictionary> vo_codes;
    @SerializedName("countries")
    public ArrayList<Country> countries;
    @SerializedName("foreignBanks")
    public ArrayList<ForeignBank> foreignBanks;
    @SerializedName("isoCountries")
    public ArrayList<Dictionary> countriesForRegMastercard;
}
