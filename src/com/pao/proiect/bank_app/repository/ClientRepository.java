package com.pao.proiect.bank_app.repository;

import com.pao.proiect.bank_app.model.Client;
import com.pao.proiect.bank_app.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, String> {
    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        Client c = new Client(
                rs.getString("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("parola"),
                rs.getString("adresa"),
                rs.getString("numar_telefon")
        );
        return c;
    }

    @Override
    public void save(Client client) throws SQLException {
        String sql = "INSERT INTO client (id, nume, prenume, email, parola, adresa, numar_telefon) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, client.getId());
            ps.setString(2, client.getNume());
            ps.setString(3, client.getPrenume());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getParola());
            ps.setString(6, client.getAdresa());
            ps.setString(7, client.getNumarTelefon());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Client> findById(String id) throws SQLException {
        String sql = "SELECT id, nume, prenume, email, parola, adresa, numar_telefon FROM client WHERE id = ?";
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
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT id, nume, prenume, email, parola, adresa, numar_telefon FROM client ORDER BY id";
        List<Client> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Client client) throws SQLException {
        String sql = "UPDATE client SET nume = ?, prenume = ?, email = ?, parola = ?, adresa = ?, numar_telefon = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, client.getNume());
            ps.setString(2, client.getPrenume());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getParola());
            ps.setString(5, client.getAdresa());
            ps.setString(6, client.getNumarTelefon());
            ps.setString(7, client.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
