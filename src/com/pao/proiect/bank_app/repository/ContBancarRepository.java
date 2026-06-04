package com.pao.proiect.bank_app.repository;

import com.pao.proiect.bank_app.model.Client;
import com.pao.proiect.bank_app.model.ContBancar;
import com.pao.proiect.bank_app.model.ContCurent;
import com.pao.proiect.bank_app.model.ContEconomii;
import com.pao.proiect.bank_app.model.Moneda;
import com.pao.proiect.bank_app.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContBancarRepository implements Repository<ContBancar, String> {
    private final ClientRepository clientRepository = new ClientRepository();

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private ContBancar mapRow(ResultSet rs) throws SQLException {
        String iban = rs.getString("iban");
        String clientId = rs.getString("client_id");
        double sold = rs.getDouble("sold");
        Moneda moneda = Moneda.valueOf(rs.getString("moneda"));
        String tipCont = rs.getString("tip_cont");

        Client titular = clientRepository.findById(clientId)
                .orElseThrow(() -> new SQLException("Eroare integritate: Clientul cu ID " + clientId + " nu exista in DB!"));

        if ("CURENT".equals(tipCont)) {
            double comision = rs.getDouble("comision_administrare");
            return new ContCurent(iban, sold, moneda, titular, comision);
        } else if ("ECONOMII".equals(tipCont)) {
            double dobanda = rs.getDouble("rata_dobanda");
            return new ContEconomii(iban, sold, moneda, titular, dobanda);
        } else {
            throw new SQLException("Tip de cont necunoscut in baza de date: " + tipCont);
        }
    }

    @Override
    public void save(ContBancar cont) throws SQLException {
        String sql = "INSERT INTO cont_bancar (iban, client_id, sold, moneda, tip_cont, comision_administrare, rata_dobanda) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cont.getIban());
            ps.setString(2, cont.getTitular().getId());
            ps.setDouble(3, cont.getSold());
            ps.setString(4, cont.getMoneda().name());

            if (cont instanceof ContCurent curent) {
                ps.setString(5, "CURENT");
                ps.setDouble(6, curent.getComisionAdministrare());
                ps.setNull(7, Types.NUMERIC);
            } else if (cont instanceof ContEconomii economii) {
                ps.setString(5, "ECONOMII");
                ps.setNull(6, Types.NUMERIC);
                ps.setDouble(7, economii.getRataDobanda());
            }

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<ContBancar> findById(String id) throws SQLException {
        String sql = "SELECT iban, client_id, sold, moneda, tip_cont, comision_administrare, rata_dobanda FROM cont_bancar WHERE iban = ?";
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
    public List<ContBancar> findAll() throws SQLException {
        String sql = "SELECT iban, client_id, sold, moneda, tip_cont, comision_administrare, rata_dobanda FROM cont_bancar";
        List<ContBancar> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(ContBancar cont) throws SQLException {
        String sql = "UPDATE cont_bancar SET sold = ?, comision_administrare = ?, rata_dobanda = ? WHERE iban = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setDouble(1, cont.getSold());

            if (cont instanceof ContCurent curent) {
                ps.setDouble(2, curent.getComisionAdministrare());
                ps.setNull(3, Types.NUMERIC);
            } else if (cont instanceof ContEconomii economii) {
                ps.setNull(2, Types.NUMERIC);
                ps.setDouble(3, economii.getRataDobanda());
            }

            ps.setString(4, cont.getIban());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM cont_bancar WHERE iban = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}