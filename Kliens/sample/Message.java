package sample;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Message {
    private static JSONParser parser = new JSONParser();

    public static String toString(JSONObject obj) {
        System.out.println("out: " + obj.toJSONString());
        return obj.toJSONString();
    }

    public static JSONObject toJson(String str) {
        System.out.println("in: " + str);
        JSONObject obj = null;
        boolean err;
        if (str != null) {
            try {
                obj = (JSONObject) parser.parse(str);
                err = false;
            } catch (ParseException e) {
                err = true;
            }
        } else {
            err = true;
        }

        if (err) {
            obj = new JSONObject();
        }

        return obj;
    }

    public static int getInt(JSONObject obj, String attr) {
        return (int)(long) obj.get(attr);
    }

    public static float getFloat(JSONObject obj, String attr) {
        return (float)(double) obj.get(attr);
    }

    public static boolean getBoolean(JSONObject obj, String attr) {
        return (boolean) obj.get(attr);
    }

    public static String getString(JSONObject obj, String attr) {
        return (String) obj.get(attr);
    }

    public static UserInfo getUserInfo(JSONObject obj) {
        JSONObject info = (JSONObject) obj.get("user_info");
        return new UserInfo(
                getInt(info, "id"), getString(info, "email"), getString(info, "uname"),
                getString(info, "firstname"), getString(info, "lastname"), getInt(info, "fid"));
    }
}
