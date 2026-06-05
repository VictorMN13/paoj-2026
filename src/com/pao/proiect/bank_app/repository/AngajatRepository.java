package com.pao.proiect.bank_app.repository;

import com.pao.proiect.bank_app.model.Angajat;
import com.pao.proiect.bank_app.model.Manager;
import com.pao.proiect.bank_app.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AngajatRepository implements Repository<Angajat, String> {
    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Angajat mapRow(ResultSet rs) throws SQLException {
        boolean isManager = rs.getBoolean("is_manager");

        if (isManager) {
            return new Manager(
                    rs.getString("id"),
                    rs.getString("nume"),
                    rs.getString("prenume"),
                    rs.getString("email"),
                    rs.getString("parola"),
                    rs.getDouble("salariu"),
                    rs.getString("sucursala"),
                    rs.getDouble("bonus_conducere"),
                    rs.getString("departament")
            );
        } else {
            return new Angajat(
                    rs.getString("id"),
                    rs.getString("nume"),
                    rs.getString("prenume"),
                    rs.getString("email"),
                    rs.getString("parola"),
                    rs.getDouble("salariu"),
                    rs.getString("sucursala")
            );
        }
    }

    @Override
    public void save(Angajat angajat) throws SQLException {
        String sql = "INSERT INTO angajat (id, nume, prenume, email, parola, salariu, sucursala, is_manager, bonus_conducere, departament) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, angajat.getId());
            ps.setString(2, angajat.getNume());
            ps.setString(3, angajat.getPrenume());
            ps.setString(4, angajat.getEmail());
            ps.setString(5, angajat.getParola());
            ps.setDouble(6, angajat.getSalariu());
            ps.setString(7, angajat.getSucursala());

            if (angajat instanceof Manager manager) {
                ps.setBoolean(8, true);
                ps.setDouble(9, manager.getBonusConducere());
                ps.setString(10, manager.getDepartament());
            } else {
                ps.setBoolean(8, false);
                ps.setNull(9, java.sql.Types.DOUBLE);
                ps.setNull(10, java.sql.Types.VARCHAR);
            }
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Angajat> findById(String id) throws SQLException {
        String sql = "SELECT * FROM angajat WHERE id = ?";
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
    public List<Angajat> findAll() throws SQLException {
        String sql = "SELECT * FROM angajat ORDER BY nume";
        List<Angajat> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Angajat angajat) throws SQLException {
        String sql = "UPDATE angajat SET nume = ?, prenume = ?, email = ?, parola = ?, salariu = ?, sucursala = ?, is_manager = ?, bonus_conducere = ?, departament = ? WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, angajat.getNume());
            ps.setString(2, angajat.getPrenume());
            ps.setString(3, angajat.getEmail());
            ps.setString(4, angajat.getParola());
            ps.setDouble(5, angajat.getSalariu());
            ps.setString(6, angajat.getSucursala());

            if (angajat instanceof Manager manager) {
                ps.setBoolean(7, true);
                ps.setDouble(8, manager.getBonusConducere());
                ps.setString(9, manager.getDepartament());
            } else {
                ps.setBoolean(7, false);
                ps.setNull(8, java.sql.Types.DOUBLE);
                ps.setNull(9, java.sql.Types.VARCHAR);
            }

            ps.setString(10, angajat.getId());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM angajat WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
