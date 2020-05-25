package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends NiceController {
    @FXML
    private TextField lUnameField;
    @FXML
    private PasswordField lPasswField;

    @FXML
    private AnchorPane registrationPane;
    @FXML
    private TextField rEmailField;
    @FXML
    private TextField rUnameField;
    @FXML
    private PasswordField rPasswField;
    @FXML
    private TextField rFirstnameField;
    @FXML
    private TextField rLastnameField;

    @FXML
    private AnchorPane resetPasswPane;

    @FXML
    private ImageView logoutButton;

    @FXML
    private TabPane mainTabPane;
    @FXML
    private Label tabHelloLabel;
    @FXML
    private Label tabFamilyLabel;
    @FXML
    private TableView<MemberInfo> familyMembersTable;
    @FXML
    private TableColumn<MemberInfo, String> emailCol;
    @FXML
    private TableColumn<MemberInfo, String> unameCol;
    @FXML
    private TableColumn<MemberInfo, String> firstnameCol;
    @FXML
    private TableColumn<MemberInfo, String> lastnameCol;
    @FXML
    private TextField arUserField;
    @FXML
    private VBox postsFeed;
    @FXML
    private VBox tasksFeed;
    @FXML
    private TableView<ListItem> shoppingListTable;
    @FXML
    private TableColumn<ListItem, Integer> idCol;
    @FXML
    private TableColumn<ListItem, String> inameCol;
    @FXML
    private TableColumn<ListItem, Float> quantityCol;
    @FXML
    private TableColumn<ListItem, String> unitCol;

    @FXML
    private AnchorPane createPostPane;
    @FXML
    private TextField cpTitleField;
    @FXML
    private TextArea cpBodyArea;

    @FXML
    private AnchorPane createTaskPane;
    @FXML
    private TextField ctTitleField;
    @FXML
    private TextArea ctDescriptionArea;
    @FXML
    private DatePicker ctDeadlineDatePicker;

    @FXML
    private AnchorPane createListItemPane;
    @FXML
    private TextField cliNameField;
    @FXML
    private TextField cliQuantityField;
    @FXML
    private TextField cliUnitField;

    @FXML
    private AnchorPane createFamilyPane;
    @FXML
    private TextField cfNameField;

    @FXML
    private AnchorPane removeContentPane;
    @FXML
    private TextField rcIdField;
    @FXML
    private ChoiceBox rcTypeChoiceBox;
    
    private boolean authenticated = false;
    private UserInfo userInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        emailCol.setCellValueFactory(new PropertyValueFactory<MemberInfo, String>("email"));
        unameCol.setCellValueFactory(new PropertyValueFactory<MemberInfo, String>("uname"));
        firstnameCol.setCellValueFactory(new PropertyValueFactory<MemberInfo, String>("firstname"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<MemberInfo, String>("lastname"));

        postsFeed.setPadding(new Insets(10, 50, 50, 50));
        postsFeed.setSpacing(10);

        tasksFeed.setPadding(new Insets(10, 50, 50, 50));
        tasksFeed.setSpacing(10);

        idCol.setCellValueFactory(new PropertyValueFactory<ListItem, Integer>("id"));
        inameCol.setCellValueFactory(new PropertyValueFactory<ListItem, String>("iname"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<ListItem, Float>("quantity"));
        unitCol.setCellValueFactory(new PropertyValueFactory<ListItem, String>("unit"));
    }

    @FXML
    public void login(Event event) {
        try {
            if (ServerConnection.login(lUnameField.getText(), lPasswField.getText())) {;
                authenticated = true;
                fetchHome(null);
                logoutButton.setVisible(true);
            }
            lUnameField.setText("");
            lPasswField.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(Event event) {
        try {
            if (ServerConnection.logout()) {
                authenticated = false;
                mainTabPane.setVisible(false);
                logoutButton.setVisible(false);
                createFamilyPane.setVisible(false);
                createPostPane.setVisible(false);
                createListItemPane.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void register(Event event) {
        try {
            if (ServerConnection.register(rEmailField.getText(), rUnameField.getText(),
                    rPasswField.getText(), rFirstnameField.getText(), rLastnameField.getText())) {;
                authenticated = true;
                goBackFromRegistration(null);
                fetchHome(null);
                logoutButton.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToRegistration(Event event) {
        registrationPane.setVisible(true);
    }

    @FXML
    public void goBackFromRegistration(Event event) {
        registrationPane.setVisible(false);
        rEmailField.setText("");
        rUnameField.setText("");
        rPasswField.setText("");
        rFirstnameField.setText("");
        rLastnameField.setText("");
    }

    @FXML
    public void goToResetPassw(Event event) {
        resetPasswPane.setVisible(true);
    }

    @FXML
    public void goBackFromResetPassw(Event event) {
        resetPasswPane.setVisible(false);
    }

    @FXML
    public void goToCreatePost(Event event) {
        createPostPane.setVisible(true);
    }

    @FXML
    public void goBackFromCreatePost(Event event) {
        createPostPane.setVisible(false);
        cpTitleField.setText("");
        cpBodyArea.setText("");
    }

    @FXML
    public void goToCreateTask(Event event) {
        createTaskPane.setVisible(true);
    }

    @FXML
    public void goBackFromCreateTask(Event event) {
        createTaskPane.setVisible(false);
        ctTitleField.setText("");
        ctDescriptionArea.setText("");
        ctDeadlineDatePicker.setValue(null);
    }

    @FXML
    public void goToCreateListItem(Event event) {
        createListItemPane.setVisible(true);
    }

    @FXML
    public void goBackFromCreateListItem(Event event) {
        createListItemPane.setVisible(false);
        cliNameField.setText("");
        cliQuantityField.setText("");
        cliUnitField.setText("");
    }

    @FXML
    public void goToRemovePost(Event event) {
        rcTypeChoiceBox.setValue("Bejegyzés");
        removeContentPane.setVisible(true);
    }

    @FXML
    public void goToRemoveTask(Event event) {
        rcTypeChoiceBox.setValue("Feladat");
        removeContentPane.setVisible(true);
    }

    @FXML
    public void goToRemoveListItem(Event event) {
        rcTypeChoiceBox.setValue("Bevásárlólista tétel");
        removeContentPane.setVisible(true);
    }

    @FXML
    public void goBackFromRemoveContent(Event event) {
        removeContentPane.setVisible(false);
        rcIdField.setText("");
    }

    @FXML
    public void createFamily(Event event) {
        try {
            if (ServerConnection.createFamily(cfNameField.getText())) {
                createFamilyPane.setVisible(false);
                mainTabPane.setVisible(true);
                fetchHome(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfNameField.setText("");
    }

    @FXML
    public void addUser(Event event) {
        try {
            if (ServerConnection.addUser(arUserField.getText()))
                fetchHome(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        arUserField.setText("");
    }

    @FXML
    public void removeUser(Event event) {
        try {
            if (ServerConnection.removeUser(arUserField.getText()))
                fetchHome(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        arUserField.setText("");
    }

    @FXML
    public void removeFamily(Event event) {
        try {
            if (ServerConnection.removeFamily())
                fetchHome(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void fetchHome(Event event) {
        if (!authenticated)
            return;
        try {
            userInfo = ServerConnection.getUserInfo();
            if (userInfo.getFid() != 0) {
                tabHelloLabel.setText(String.format("Üdv, %s %s!", userInfo.getFirstname(), userInfo.getLastname()));
                tabFamilyLabel.setText(String.format("%s család", ServerConnection.getFamilyName(userInfo.getFid())));
                familyMembersTable.getItems().clear();
                JSONArray family_members = ServerConnection.fetchHome();
                for (int i = 0; i < family_members.size(); i++) {
                    JSONObject entry = (JSONObject) family_members.get(i);
                    MemberInfo info = new MemberInfo(
                            Message.getString(entry, "email"), Message.getString(entry, "uname"),
                            Message.getString(entry, "firstname"), Message.getString(entry, "lastname"));
                    familyMembersTable.getItems().add(info);
                }
                mainTabPane.setVisible(true);
            } else {
                mainTabPane.setVisible(false);
                createFamilyPane.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void fetchPosts(Event event) {
        if (!authenticated)
            return;
        try {
            userInfo = ServerConnection.getUserInfo();
            JSONArray posts = ServerConnection.fetchPosts();
            postsFeed.getChildren().clear();
            for (int i = 0; i < posts.size(); i++) {
                JSONObject entry = (JSONObject) posts.get(i);
                BorderPane post = new BorderPane();
                Label title = new Label(String.format("%s (%d)",
                        Message.getString(entry, "title"), Message.getInt(entry, "id")));
                post.setTop(title);
                TextArea body = new TextArea(Message.getString(entry, "body"));
                post.setCenter(body);
                Label cdate = new Label(Message.getString(entry, "cdate"));
                post.setBottom(cdate);
                postsFeed.getChildren().add(0, post);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void fetchTasks(Event event) {
        if (!authenticated)
            return;
        try {
            JSONArray tasks = ServerConnection.fetchTasks();
            tasksFeed.getChildren().clear();
            for (int i = 0; i < tasks.size(); i++) {
                JSONObject entry = (JSONObject) tasks.get(i);
                BorderPane task = new BorderPane();
                int id = Message.getInt(entry, "id");
                Label title = new Label(String.format("%s (%d)", Message.getString(entry, "title"), id));
                task.setTop(title);
                TextArea description = new TextArea(Message.getString(entry, "description"));
                task.setCenter(description);
                Label deadline = new Label((Message.getString(entry, "deadline")));
                task.setBottom(deadline);
                CheckBox done = new CheckBox("Kész");
                done.setSelected(Message.getBoolean(entry, "done"));
                done.setOnAction((x) -> {
                    try {
                        ServerConnection.changeTaskDone(id, done.isSelected());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                task.setBottom(done);
                tasksFeed.getChildren().add(0, task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void fetchShoppingList(Event event) {
        if (!authenticated)
            return;
        try {
            shoppingListTable.getItems().clear();
            JSONArray shopping_list = ServerConnection.fetchShoppingList();
            for (int i = 0; i < shopping_list.size(); i++) {
                JSONObject entry = (JSONObject) shopping_list.get(i);
                ListItem item = new ListItem(
                        Message.getInt(entry, "id"), Message.getString(entry, "iname"),
                        Message.getFloat(entry, "quantity"), Message.getString(entry, "unit"));
                shoppingListTable.getItems().add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createPost(Event event) {
        try {
            if (ServerConnection.createPost(cpTitleField.getText(), cpBodyArea.getText())) {
                goBackFromCreatePost(null);
                fetchPosts(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createTask(Event event) {
        try {
            if (ServerConnection.createTask(ctTitleField.getText(), ctDescriptionArea.getText(),
                    ctDeadlineDatePicker.getValue().toString())) {
                goBackFromCreateTask(null);
                fetchTasks(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createListItem(Event event) {
        try {
            if (ServerConnection.createListItem(cliNameField.getText(), Float.parseFloat(cliQuantityField.getText()),
                    cliUnitField.getText())) {
                goBackFromCreateListItem(null);
                fetchShoppingList(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeContent(Event event) {
        try {
            int id = Integer.parseInt(rcIdField.getText());
            int selected = rcTypeChoiceBox.getSelectionModel().getSelectedIndex();
            if (ServerConnection.removeContent(id, selected)) {
                goBackFromRemoveContent(null);
                switch (selected) {
                    case 0:
                        fetchPosts(null);
                        break;
                    case 1:
                        fetchTasks(null);
                        break;
                    case 2:
                        fetchShoppingList(null);
                        break;
                    default:
                        fetchHome(null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
