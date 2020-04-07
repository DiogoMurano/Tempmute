package br.com.diogomurano.tempmute.data;

import br.com.diogomurano.tempmute.model.MutePlayer;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class DataManager {

    private File file;
    private Connection connection;

    public DataManager(File file) {
        this.file = file;
    }

    public void setupConnection() {
        try {
            file.mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<MutePlayer> findPlayerByName(String playerName) {
        MutePlayer player = null;

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `mute_player` WHERE `playerName`=?")) {
            stmt.setString(1, playerName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                player = MutePlayer.builder()
                        .playerName(playerName)
                        .expires(rs.getLong("expires"))
                        .build();
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(player);
    }

    public void insertPlayer(MutePlayer player) {
        if(findPlayerByName(player.getPlayerName()).isPresent()) {
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE `mute_player` SET `expires`=? WHERE `playerName`=?")) {
                stmt.setLong(1, player.getExpires());
                stmt.setString(2, player.getPlayerName());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO `mute_player` (`playerName`, `expires`) VALUES (?, ?)")) {
                stmt.setString(1, player.getPlayerName());
                stmt.setLong(2, player.getExpires());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removePlayer(String playerName) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM `mute_player` WHERE `playerName`=?")) {
            stmt.setString(1, playerName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setupTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS `mute_player` (`playerName` VARCHAR(16) NOT NULL, `expires`" +
                    " BIGINT(20) NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
