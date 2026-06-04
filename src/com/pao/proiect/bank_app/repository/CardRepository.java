package com.pao.proiect.bank_app.repository;

import com.pao.proiect.bank_app.model.Card;
import com.pao.proiect.bank_app.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository implements Repository<Card, String> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Card mapRow(ResultSet rs) throws SQLException {
        String numarCard = rs.getString("numar_card");
        String ibanAsociat = rs.getString("iban_asociat");
        String cvv = rs.getString("cvv");
        LocalDate dataExpirare = rs.getDate("data_expirare").toLocalDate();
        String pin = rs.getString("pin");
        boolean blocat = rs.getBoolean("blocat");

        return new Card(numarCard, ibanAsociat, cvv, dataExpirare, pin, blocat);
    }

    @Override
    public void save(Card card) throws SQLException {
        String sql = "INSERT INTO card (numar_card, iban_asociat, cvv, data_expirare, pin, blocat) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, card.getNumarCard());
            ps.setString(2, card.getIbanAsociat());
            ps.setString(3, card.getCvv());
            ps.setDate(4, Date.valueOf(card.getDataExpirare()));
            ps.setString(5, card.getPin());
            ps.setBoolean(6, card.isBlocat());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Card> findById(String id) throws SQLException {
        String sql = "SELECT numar_card, iban_asociat, cvv, data_expirare, pin, blocat FROM card WHERE numar_card = ?";
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
    public List<Card> findAll() throws SQLException {
        String sql = "SELECT numar_card, iban_asociat, cvv, data_expirare, pin, blocat FROM card";
        List<Card> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Card card) throws SQLException {
        String sql = "UPDATE card SET pin = ?, blocat = ? WHERE numar_card = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, card.getPin());
            ps.setBoolean(2, card.isBlocat());
            ps.setString(3, card.getNumarCard());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM card WHERE numar_card = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
