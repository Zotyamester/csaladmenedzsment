package sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;

public class DbConnection {
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/testdb";

    private static final String USER = "root";
    private static final String PASS = "root";

    private static DbConnection instance = null;

    private Connection conn = null;

    public static DbConnection getInstance() {
        if (instance == null)
            instance = new DbConnection();
        return instance;
    }

    public void launch() throws ClassNotFoundException, SQLException {
        System.out.println("Connecting to the database...");
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        System.out.println("Connected to the database successfully...");
    }

    public void close() throws SQLException {
        conn.close();
    }

    private static final String LOGIN =
              "SELECT person.passw, person.salt\n"
            + "FROM person WHERE person.uname = ?";

    public boolean login(String uname, String passw) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    LOGIN + ';');
            stmt.setString(1, uname);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                return false;
            String actualHash = rs.getString(1);
            String salt = rs.getString(2);
            String hash = Security.toHexString(Security.getSHA(passw + salt));
            return hash.equals(actualHash);
        } catch (SQLException e) {
            return false;
        }
    }

    private static final String REGISTER =
              "INSERT INTO person\n"
            + "(email, uname, passw, salt, firstname, lastname)\n"
            + "VALUES (?, ?, ?, ?, ?, ?)";

    public boolean register(String email, String uname, String passw,
                                   String firstname, String lastname) {
        String salt = Security.generateSalt();
        String saltedHashedPass = Security.toHexString(Security.getSHA(passw + salt));
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    REGISTER + ';');
            stmt.setString(1, email);
            stmt.setString(2, uname);
            stmt.setString(3, saltedHashedPass);
            stmt.setString(4, salt);
            stmt.setString(5, firstname);
            stmt.setString(6, lastname);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // "We won't actually delete it, to keep their data. Eat your heart out GDPR! >D"
    // Viccet félretéve ez a funkció még nincs implementálva, de tervben van.
    public final boolean unregister(UserInfo info) {
        return true;
    }

    private static final String GET_USER_INFO =
              "SELECT person.id, person.email, person.firstname, person.lastname, person.fid\n"
            + "FROM person WHERE person.uname = ?";

    public UserInfo get_user_info(String uname) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                GET_USER_INFO + ';');
        stmt.setString(1, uname);
        ResultSet rs = stmt.executeQuery();
        UserInfo info = new UserInfo();
        if (rs.next()) {
            info.setId(rs.getInt(1));
            info.setEmail(rs.getString(2));
            info.setUname(uname);
            info.setFirstname(rs.getString(3));
            info.setLastname(rs.getString(4));
            info.setFid(rs.getInt(5));
        }
        return info;
    }

    private static final String GET_FAMILY_NAME =
              "SELECT family.fname\n"
            + "FROM family WHERE family.id = ?";

    public final String get_family_name(UserInfo info) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                GET_FAMILY_NAME + ';');
        stmt.setInt(1, info.getFid());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
        return "NULL";
    }

    private static final String GET_IIDS =
              "SELECT person.id\n"
            + "FROM person\n"
            + "WHERE person.fid = ?";

    private static final String GET_POSTS =
              "SELECT post.id, post.title, post.body, post.cdate\n"
            + "FROM post\n"
            + "WHERE post.iid IN (\n"
            + GET_IIDS + '\n'
            + ")";

    public JSONArray get_posts(UserInfo info) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                GET_POSTS + ';');
        stmt.setInt(1, info.getFid());
        ResultSet rs = stmt.executeQuery();
        JSONArray array = new JSONArray();
        while (rs.next()) {
            JSONObject obj = new JSONObject();
            obj.put("id", rs.getInt(1));
            obj.put("title", rs.getString(2));
            obj.put("body", rs.getString(3));
            obj.put("cdate", rs.getString(4));
            array.add(obj);
        }
        return array;
    }

    private static final String GET_TASKS =
              "SELECT task.id, task.title, task.description, task.deadline, task.done\n"
            + "FROM task\n"
            + "WHERE task.iid IN (\n"
            + GET_IIDS
            + ")";

    public JSONArray get_tasks(UserInfo info) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                GET_TASKS + ';');
        stmt.setInt(1, info.getFid());
        ResultSet rs = stmt.executeQuery();
        JSONArray array = new JSONArray();
        while (rs.next()) {
            JSONObject obj = new JSONObject();
            obj.put("id", rs.getInt(1));
            obj.put("title", rs.getString(2));
            obj.put("description", rs.getString(3));
            obj.put("deadline", rs.getString(4));
            obj.put("done", rs.getBoolean(5));
            array.add(obj);
        }
        return array;
    }

    private static final String GET_SHOPPING_LIST =
              "SELECT listitem.id, listitem.iname, listitem.quantity, listitem.unit\n"
            + "FROM listitem\n"
            + "WHERE listitem.iid In (\n"
            + GET_IIDS
            + ")";

    public JSONArray get_shopping_list(UserInfo info) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                GET_SHOPPING_LIST + ';');
        stmt.setInt(1, info.getFid());
        ResultSet rs = stmt.executeQuery();
        JSONArray array = new JSONArray();
        while (rs.next()) {
            JSONObject obj = new JSONObject();
            obj.put("id", rs.getInt(1));
            obj.put("iname", rs.getString(2));
            obj.put("quantity", rs.getFloat(3));
            obj.put("unit", rs.getString(4));
            array.add(obj);
        }
        return array;
    }

    private static final String GET_FAMILY_MEMBERS =
              "SELECT person.email, person.uname, person.firstname, person.lastname\n"
            + "FROM person\n"
            + "WHERE person.fid = ?";

    public JSONArray get_family_members(UserInfo info) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                GET_FAMILY_MEMBERS + ';');
        stmt.setInt(1, info.getFid());
        ResultSet rs = stmt.executeQuery();
        JSONArray array = new JSONArray();
        while (rs.next()) {
            JSONObject obj = new JSONObject();
            obj.put("email", rs.getString(1));
            obj.put("uname", rs.getString(2));
            obj.put("firstname", rs.getString(3));
            obj.put("lastname", rs.getString(4));
            array.add(obj);
        }
        return array;
    }

    private static final String ADD_POST =
              "INSERT INTO post\n"
            + "(iid, title, body, cdate)\n"
            + "VALUES (?, ?, ?, ?)";

    public boolean add_post(UserInfo info, String title, String body) {
        if (info.getFid() == 0)
            return false;
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    ADD_POST + ';');
            stmt.setInt(1, info.getId());
            stmt.setString(2, title);
            stmt.setString(3, body);
            stmt.setString(4, new Date(System.currentTimeMillis()).toString());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private static final String ADD_TASK =
              "INSERT INTO task\n"
            + "(iid, title, description, deadline, done)\n"
            + "VALUES (?, ?, ?, ?, ?)";

    public boolean add_task(UserInfo info, String title, String description, String deadline) {
        if (info.getFid() == 0)
            return false;
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    ADD_TASK + ';');
            stmt.setInt(1, info.getId());
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, deadline);
            stmt.setBoolean(5, false);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private static final String CHANGE_TASK_DONE =
              "UPDATE task\n"
            + "SET task.done = ?\n"
            + "WHERE task.id = ? AND EXISTS\n"
            + "(SELECT * FROM task INNER JOIN person ON task.iid = person.id WHERE task.id = ? AND person.fid = ?)";

    public boolean change_task_done(UserInfo info, int id, boolean done) {
        if (info.getFid() == 0)
            return false;
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    CHANGE_TASK_DONE + ';');
            stmt.setBoolean(1, done);
            stmt.setInt(2, id);
            stmt.setInt(3, id);
            stmt.setInt(4, info.getFid());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private static final String ADD_LIST_ITEM =
              "INSERT INTO listitem\n"
            + "(iid, iname, quantity, unit)\n"
            + "VALUES (?, ?, ?, ?)";

    public boolean add_list_item(UserInfo info, String iname, float quantity, String unit) {
        if (info.getFid() == 0)
            return false;
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    ADD_LIST_ITEM + ';');
            stmt.setInt(1, info.getId());
            stmt.setString(2, iname);
            stmt.setFloat(3, quantity);
            stmt.setString(4, unit);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private static final String ADD_FAMILY_MEMBER =
              "UPDATE person\n"
            + "SET person.fid = ?\n"
            + "WHERE person.uname = ? AND person.fid IS NULL";

    public boolean add_family_member(UserInfo info, String uname) {
        if (info.getFid() == 0)
            return false;
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    ADD_FAMILY_MEMBER + ';');
            stmt.setInt(1, info.getFid());
            stmt.setString(2, uname);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private static final String ADD_FAMILY =
              "INSERT INTO family\n"
            + "(fname)\n"
            + "VALUES (?)";

    private static final String ADD_FIRST_MEMBER =
              "UPDATE person\n"
            + "SET person.fid = LAST_INSERT_ID()\n"
            + "WHERE person.id = ?";

    public boolean add_family(UserInfo info, String fname) {
        if (info.getFid() != 0)
            return false;
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement(
                    ADD_FAMILY + ';');
            stmt.setString(1, fname);
            if (stmt.executeUpdate() != 1)
                return false;

            stmt = conn.prepareStatement(
                    ADD_FIRST_MEMBER + ';');
            stmt.setInt(1, info.getId());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final String REMOVE_POST =
              "DELETE\n"
            + "FROM post\n"
            + "WHERE post.id = ? AND post.iid IN (\n"
            + GET_IIDS
            + ")";

    public boolean remove_post(UserInfo info, int pid) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    REMOVE_POST + ';');
            stmt.setInt(1, pid);
            stmt.setInt(2, info.getFid());
            System.out.println(stmt.toString());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private static final String REMOVE_TASK =
              "DELETE\n"
            + "FROM task\n"
            + "WHERE task.id = ? AND task.iid IN (\n"
            + GET_IIDS
            + ")";

    public boolean remove_task(UserInfo info, int pid) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    REMOVE_TASK + ';');
            stmt.setInt(1, pid);
            stmt.setInt(2, info.getFid());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private static final String REMOVE_LIST_ITEM =
              "DELETE\n"
            + "FROM listitem\n"
            + "WHERE listitem.id = ? AND listitem.iid IN (\n"
            + GET_IIDS
            + ")";

    public boolean remove_list_item(UserInfo info, int pid) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    REMOVE_LIST_ITEM + ';');
            stmt.setInt(1, pid);
            stmt.setInt(2, info.getFid());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private static final String REMOVE_FAMILY_MEMBER =
              "UPDATE person\n"
            + "SET person.fid = NULL\n"
            + "WHERE person.fid = ? AND person.uname = ?";

    public boolean remove_family_member(UserInfo info, String uname) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    REMOVE_FAMILY_MEMBER + ';');
            stmt.setInt(1, info.getFid());
            stmt.setString(2, uname);

            if (stmt.executeUpdate() == 1) {
                stmt = conn.prepareStatement(
                      "DELETE FROM post WHERE post.iid IN\n"
                        + "(SELECT person.id FROM person WHERE person.uname = ?);");
                stmt.setString(1, uname);
                stmt.executeUpdate();

                stmt = conn.prepareStatement(
                      "DELETE FROM task WHERE task.iid IN\n"
                        + "(SELECT person.id FROM person WHERE person.uname = ?);");
                stmt.setString(1, uname);
                stmt.executeUpdate();

                stmt = conn.prepareStatement(
                      "DELETE FROM listitem WHERE listitem.iid IN\n"
                        + "(SELECT person.id FROM person WHERE person.uname = ?);");
                stmt.setString(1, uname);
                stmt.executeUpdate();

                stmt = conn.prepareStatement(GET_IIDS);
                stmt.setInt(1, info.getFid());
                if (!stmt.executeQuery().next()) {
                    stmt = conn.prepareStatement(
                            REMOVE_FAMILY + ';');
                    stmt.setInt(1, info.getFid());
                    stmt.executeUpdate();
                }

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final String REMOVE_FAMILY_POSTS =
              "DELETE\n"
            + "FROM post\n"
            + "WHERE post.iid IN (\n"
            + GET_IIDS
            + ")";

    private static final String REMOVE_FAMILY_TASKS =
              "DELETE\n"
            + "FROM task\n"
            + "WHERE task.iid IN (\n"
            + GET_IIDS
            + ")";

    private static final String REMOVE_FAMILY_SHOPPING_LIST =
              "DELETE\n"
            + "FROM listitem\n"
            + "WHERE listitem.iid IN (\n"
            + GET_IIDS
            + ")";

    private static final String REMOVE_FAMILY_MEMBERS =
              "UPDATE person\n"
            + "SET person.fid = NULL\n"
            + "WHERE person.fid = ?";

    private static final String REMOVE_FAMILY =
              "DELETE\n"
            + "FROM family\n"
            + "WHERE family.id = ?";

    public boolean remove_family(UserInfo info) {
        if (info.getFid() == 0)
            return false;
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement(
                    REMOVE_FAMILY_POSTS + ';');
            stmt.setInt(1, info.getFid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement(
                    REMOVE_FAMILY_TASKS + ';');
            stmt.setInt(1, info.getFid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement(
                    REMOVE_FAMILY_SHOPPING_LIST + ';');
            stmt.setInt(1, info.getFid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement(
                    REMOVE_FAMILY_POSTS + ';');
            stmt.setInt(1, info.getFid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement(
                    REMOVE_FAMILY_MEMBERS + ';');
            stmt.setInt(1, info.getFid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement(
                    REMOVE_FAMILY + ';');
            stmt.setInt(1, info.getFid());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
