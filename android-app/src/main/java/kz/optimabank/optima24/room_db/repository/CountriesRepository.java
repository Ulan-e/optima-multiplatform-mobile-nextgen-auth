package kz.optimabank.optima24.room_db.repository;

import java.util.List;

import kz.optimabank.optima24.db.entry.Country;

public interface CountriesRepository {

    void insertAll(List<Country> countries);

    List<Country> getAll();

    void delete();
}
