package sample;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

//
public class Client implements Runnable {
    Thread thread;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private DbConnection db;

    boolean authenticated = false;
    UserInfo info = new UserInfo();

    public Client(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            db = DbConnection.getInstance();
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Connection opened!");
        try {
            while (true) {
                JSONObject fromClient = Message.toJson(reader.readLine());
                String command = Message.getString(fromClient, "command");

                JSONObject toClient = new JSONObject();
                if (command == null) {
                    toClient.put("status", false);
                    writer.println(Message.toString(toClient));
                    break;
                }

                synchronized (db) {
                    if (!authenticated) {
                        if (command.equals("LOGIN")) {
                            String uname = Message.getString(fromClient, "uname");
                            String passw = Message.getString(fromClient, "passw");
                            boolean success = db.login(uname, passw);
                            toClient.put("status", success);
                            if (success) {
                                authenticated = true;
                                info = db.get_user_info(uname);
                            }
                        } else if (command.equals("REGISTER")) {
                            String email = Message.getString(fromClient, "email");
                            String uname = Message.getString(fromClient, "uname");
                            String passw = Message.getString(fromClient, "passw");
                            String firstname = Message.getString(fromClient, "firstname");
                            String lastname = Message.getString(fromClient, "lastname");
                            boolean success = db.register(email, uname, passw, firstname, lastname);
                            toClient.put("status", success);
                            if (success) {
                                authenticated = true;
                                info = db.get_user_info(uname);
                            }
                        }
                    } else {
                        if (command.equals("LOGOUT")) {
                            authenticated = false;
                            info = new UserInfo();
                            toClient.put("status", true);
                        } else {
                            info = db.get_user_info(info.getUname());
                            if (command.equals("GET")) {
                                String type = Message.getString(fromClient, "type");
                                if (type.equals("USER_INFO")) {
                                    toClient.put("user_info", info.toJson());
                                } else if (type.equals("FAMILY_NAME")) {
                                    toClient.put("family_name", db.get_family_name(info));
                                } else if (type.equals("POSTS")) {
                                    toClient.put("posts", db.get_posts(info));
                                } else if (type.equals("TASKS")) {
                                    toClient.put("tasks", db.get_tasks(info));
                                } else if (type.equals("SHOPPING_LIST")) {
                                    toClient.put("shopping_list", db.get_shopping_list(info));
                                } else if (type.equals("FAMILY_MEMBERS")) {
                                    toClient.put("family_members", db.get_family_members(info));
                                }
                            } else if (command.equals("ADD")) {
                                String type = Message.getString(fromClient, "type");
                                if (type.equals("POST")) {
                                    String title = Message.getString(fromClient, "title");
                                    String body = Message.getString(fromClient, "body");
                                    toClient.put("status", db.add_post(info, title, body));
                                } else if (type.equals("TASK")) {
                                    String title = Message.getString(fromClient, "title");
                                    String description = Message.getString(fromClient, "description");
                                    String deadline = Message.getString(fromClient, "deadline");
                                    toClient.put("status", db.add_task(info, title, description, deadline));
                                } else if (type.equals("LIST_ITEM")) {
                                    String iname = Message.getString(fromClient, "iname");
                                    float quantity = Message.getFloat(fromClient, "quantity");
                                    String unit = Message.getString(fromClient, "unit");
                                    toClient.put("status", db.add_list_item(info, iname, quantity, unit));
                                } else if (type.equals("FAMILY_MEMBER")) {
                                    String uname = Message.getString(fromClient, "uname");
                                    toClient.put("status", db.add_family_member(info, uname));
                                } else if (type.equals("FAMILY")) {
                                    String fname = Message.getString(fromClient, "fname");
                                    toClient.put("status", db.add_family(info, fname));
                                }
                            } else if (command.equals("REMOVE")) {
                                String type = Message.getString(fromClient, "type");
                                if (type.equals("POST")) {
                                    int id = Message.getInt(fromClient, "id");
                                    toClient.put("status", db.remove_post(info, id));
                                } else if (type.equals("TASK")) {
                                    int id = Message.getInt(fromClient, "id");
                                    toClient.put("status", db.remove_task(info, id));
                                } else if (type.equals("LIST_ITEM")) {
                                    int id = Message.getInt(fromClient, "id");
                                    toClient.put("status", db.remove_list_item(info, id));
                                } else if (type.equals("FAMILY_MEMBER")) {
                                    String uname = Message.getString(fromClient, "uname");
                                    toClient.put("status", db.remove_family_member(info, uname));
                                } else if (type.equals("FAMILY")) {
                                    toClient.put("status", db.remove_family(info));
                                }
                            } else if (command.equals("CHANGE")) {
                                String type = Message.getString(fromClient, "type");
                                if (type.equals("TASK_DONE")) {
                                    int id = Message.getInt(fromClient, "id");
                                    boolean done = Message.getBoolean(fromClient, "done");
                                    toClient.put("status", db.change_task_done(info, id, done));
                                }
                            }
                        }
                    }
                    writer.println(Message.toString(toClient));
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        System.out.println("Connection closed!");
    }
}
