package kz.optimabank.optima24.local.repository;

import java.util.List;

import kz.optimabank.optima24.db.entry.ForeignBank;

public interface ForeignBankRepository {

    void insertAll(List<ForeignBank> foreignBanks);

    List<ForeignBank> getAll();

    ForeignBank getForeignBank(int Id);

    void delete();
}
