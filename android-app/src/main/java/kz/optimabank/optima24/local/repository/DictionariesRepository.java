package kz.optimabank.optima24.local.repository;

import java.util.List;

import kz.optimabank.optima24.db.entry.Dictionary;

public interface DictionariesRepository {

    void insertAll(List<Dictionary> dictionaries);

    List<Dictionary> getAll();

    List<Dictionary> getAllByType(Integer type);

    Dictionary getByType(String type);

    void deleteAll();
}
