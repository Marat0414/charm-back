package kz.muradaliev.charm.back.dao;

import kz.muradaliev.charm.back.model.Gender;
import kz.muradaliev.charm.back.model.Profile;
import kz.muradaliev.charm.back.model.Role;
import kz.muradaliev.charm.back.model.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ProfileDao {

    private static final ProfileDao INSTANCE = new ProfileDao();

    private static final String URL = "jdbc:postgresql://localhost:5432/charm_repository";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0414";

    @SneakyThrows
    public static ProfileDao getInstance() {
        Class.forName("org.postgresql.Driver");
        return INSTANCE;
    }

    public Profile save(Profile profile) {
        //language=POSTGRES-PSQL
        String sql = "INSERT INTO profile (email, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, profile.getEmail());
            stmt.setString(2, profile.getPassword());

            int insertCount = stmt.executeUpdate();
            log.debug("Insert count: {}", insertCount);

            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();

            profile.setId(rs.getLong("id"));
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Profile> findById(Long id) {
        //language=POSTGRES-PSQL
        String sql = "SELECT * FROM profile WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            Profile profile = null;
            if (rs.next()) {
                profile = mapToProfile(rs);
            }
            return Optional.ofNullable(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Profile> findByEmailAndPassword(String email, String password) {
        //language=POSTGRES-PSQL
        String sql = "SELECT * FROM profile WHERE email = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            Profile profile = null;
            if (rs.next()) {
                profile = mapToProfile(rs);
            }
            return Optional.ofNullable(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existByEmail(String email) {
        //language=POSTGRES-PSQL
        String sql = "SELECT * FROM profile WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Profile> findAll() {
        //language=POSTGRES-PSQL
        String sql = "SELECT * FROM profile";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            List<Profile> profiles = new ArrayList<>();
            while (rs.next()) {
                profiles.add(mapToProfile(rs));
            }
            return profiles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Profile profile) {
        List<Object> args = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("UPDATE profile SET email = ?, password = ?");
        args.add(profile.getEmail());
        args.add(profile.getPassword());

        if (profile.getName() != null) {
            queryBuilder.append(", name = ?");
            args.add(profile.getName());
        }
        if (profile.getSurname() != null) {
            queryBuilder.append(", surname = ?");
            args.add(profile.getSurname());
        }
        if (profile.getBirthDate() != null) {
            queryBuilder.append(", birth_date = ?");
            args.add(Date.valueOf(profile.getBirthDate()));
        }
        if (profile.getAbout() != null) {
            queryBuilder.append(", about = ?");
            args.add(profile.getAbout());
        }
        if (profile.getGender() != null) {
            queryBuilder.append(", gender = ?");
            args.add(profile.getGender().toString());
        }
        if (profile.getStatus() != null) {
            queryBuilder.append(", status = ?");
            args.add(profile.getStatus().toString());
        }
        if (profile.getPhoto() != null) {
            queryBuilder.append(", photo = ?");
            args.add(profile.getPhoto());
        }

        queryBuilder.append(" WHERE id = ?");
        args.add(profile.getId());

        String sql = queryBuilder.toString();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < args.size(); i++) {
                stmt.setObject(i + 1, args.get(i));
            }

            log.debug("Final update sql: {}", stmt);
            int updateCount = stmt.executeUpdate();
            log.debug("Update count: {}", updateCount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id) {
        //language=POSTGRES-PSQL
        String sql = "DELETE FROM profile WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            int deleteCount = stmt.executeUpdate();
            log.debug("Delete count: {}", deleteCount);
            return deleteCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Profile mapToProfile(ResultSet rs) throws SQLException {
        Profile result = new Profile();
        result.setId(rs.getLong("id"));
        result.setEmail(rs.getString("email"));
        result.setPassword(rs.getString("password"));
        result.setName(rs.getString("name"));
        result.setSurname(rs.getString("surname"));
        Date birthDate = rs.getDate("birth_date");
        if (birthDate != null) {
            result.setBirthDate(birthDate.toLocalDate());
        }
        result.setAbout(rs.getString("about"));
        String gender = rs.getString("gender");
        if (gender != null) {
            result.setGender(Gender.valueOf(gender));
        }
        result.setPhoto(rs.getString("photo"));
        String status = rs.getString("status");
        if (status != null) {
            result.setStatus(Status.valueOf(status));
        }
        String role = rs.getString("role");
        if (role != null) {
            result.setRole(Role.valueOf(role));
        }
        return result;
    }
}
