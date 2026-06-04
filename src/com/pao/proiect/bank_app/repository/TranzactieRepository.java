package com.pao.proiect.bank_app.repository;

import com.pao.proiect.bank_app.model.Tranzactie;
import com.pao.proiect.bank_app.model.TipTranzactie;
import com.pao.proiect.bank_app.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TranzactieRepository implements Repository<Tranzactie, String> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Tranzactie mapRow(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String ibanSursa = rs.getString("iban_sursa");
        String ibanDestinatie = rs.getString("iban_destinatie");
        double suma = rs.getDouble("suma");
        TipTranzactie tip = TipTranzactie.valueOf(rs.getString("tip_tranzactie"));

        LocalDateTime dataTimp = rs.getTimestamp("data_timp").toLocalDateTime();

        return new Tranzactie(id, dataTimp, suma, tip, ibanSursa, ibanDestinatie);
    }

    @Override
    public void save(Tranzactie tranzactie) throws SQLException {
        String sql = "INSERT INTO tranzactie (id, iban_sursa, iban_destinatie, suma, tip_tranzactie, data_timp) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, tranzactie.idTranzactie());
            ps.setString(2, tranzactie.ibanSursa());
            ps.setString(3, tranzactie.ibanDestinatie());
            ps.setDouble(4, tranzactie.suma());
            ps.setString(5, tranzactie.tip().name());
            ps.setTimestamp(6, Timestamp.valueOf(tranzactie.timestamp()));

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Tranzactie> findById(String id) throws SQLException {
        String sql = "SELECT id, iban_sursa, iban_destinatie, suma, tip_tranzactie, data_timp FROM tranzactie WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Tranzactie> findAll() throws SQLException {
        String sql = "SELECT id, iban_sursa, iban_destinatie, suma, tip_tranzactie, data_timp FROM tranzactie ORDER BY data_timp DESC";
        List<Tranzactie> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Tranzactie tranzactie) throws SQLException {
        throw new SQLException("Operatiune interzisa: Tranzactiile bancare nu pot fi modificate.");
    }

    @Override
    public void delete(String id) throws SQLException {
        throw new SQLException("Operatiune interzisa: Tranzactiile bancare nu pot fi sterse.");
    }
}