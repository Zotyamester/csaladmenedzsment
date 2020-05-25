package sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection {
    private static final String IP = "localhost";
    private static final int PORT = 7070;

    private static Socket socket = null;
    private static BufferedReader reader = null;
    private static PrintWriter writer = null;

    public static void connect() throws IOException {
        socket = new Socket(IP, PORT);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public static void disconnect() throws IOException {
        writer.close();
        reader.close();
        socket.close();
    }

    public static boolean login(String uname, String passw) throws IOException {
        String hashed = Security.toHexString(Security.getSHA(passw));

        JSONObject toServer = new JSONObject();
        toServer.put("command", "LOGIN");
        toServer.put("uname", uname);
        toServer.put("passw", hashed);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static boolean logout() throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "LOGOUT");
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static boolean register(String email, String uname, String password,
                                   String firstname, String lastname) throws IOException {
        String hashed = Security.toHexString(Security.getSHA(password));

        JSONObject toServer = new JSONObject();
        toServer.put("command", "REGISTER");
        toServer.put("email", email);
        toServer.put("uname", uname);
        toServer.put("passw", hashed);
        toServer.put("firstname", firstname);
        toServer.put("lastname", lastname);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static boolean createFamily(String fname) throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "ADD");
        toServer.put("type", "FAMILY");
        toServer.put("fname", fname);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static boolean addUser(String uname) throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "ADD");
        toServer.put("type", "FAMILY_MEMBER");
        toServer.put("uname", uname);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static boolean removeUser(String uname) throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "REMOVE");
        toServer.put("type", "FAMILY_MEMBER");
        toServer.put("uname", uname);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static boolean removeFamily() throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "REMOVE");
        toServer.put("type", "FAMILY");
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static UserInfo getUserInfo() throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "GET");
        toServer.put("type", "USER_INFO");
        writer.println(Message.toString(toServer));

        return Message.getUserInfo(Message.toJson(reader.readLine()));
    }

    public static String getFamilyName(int fid) throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "GET");
        toServer.put("type", "FAMILY_NAME");
        writer.println(Message.toString(toServer));

        return (String) Message.toJson(reader.readLine()).get("family_name");
    }

    public static boolean removeContent(int id, int type) throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "REMOVE");
        toServer.put("id", id);
        String t;
        switch (type) {
            case 0:
                t = "POST";
                break;
            case 1:
                t = "TASK";
                break;
            case 2:
                t = "LIST_ITEM";
                break;
            default:
                t = "";
        }
        toServer.put("type", t);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static JSONArray fetchHome() throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "GET");
        toServer.put("type", "FAMILY_MEMBERS");
        writer.println(Message.toString(toServer));

        return (JSONArray) Message.toJson(reader.readLine()).get("family_members");
    }

    public static JSONArray fetchPosts() throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "GET");
        toServer.put("type", "POSTS");
        writer.println(Message.toString(toServer));

        return (JSONArray) Message.toJson(reader.readLine()).get("posts");
    }

    public static JSONArray fetchTasks() throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "GET");
        toServer.put("type", "TASKS");
        writer.println(Message.toString(toServer));

        return (JSONArray) Message.toJson(reader.readLine()).get("tasks");
    }

    public static JSONArray fetchShoppingList() throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "GET");
        toServer.put("type", "SHOPPING_LIST");
        writer.println(Message.toString(toServer));

        return (JSONArray) Message.toJson(reader.readLine()).get("shopping_list");
    }

    public static boolean createPost(String title, String body) throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "ADD");
        toServer.put("type", "POST");
        toServer.put("title", title);
        toServer.put("body", body);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static boolean createTask(String title, String description, String deadline) throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "ADD");
        toServer.put("type", "TASK");
        toServer.put("title", title);
        toServer.put("description", description);
        toServer.put("deadline", deadline);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static boolean createListItem(String iname, float quantity, String unit) throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "ADD");
        toServer.put("type", "LIST_ITEM");
        toServer.put("iname", iname);
        toServer.put("quantity", quantity);
        toServer.put("unit", unit);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }

    public static boolean changeTaskDone(int id, boolean done) throws IOException {
        JSONObject toServer = new JSONObject();
        toServer.put("command", "CHANGE");
        toServer.put("type", "TASK_DONE");
        toServer.put("id", id);
        toServer.put("done", done);
        writer.println(Message.toString(toServer));

        JSONObject fromServer = Message.toJson(reader.readLine());
        boolean status = Message.getBoolean(fromServer, "status");

        return status;
    }
}
