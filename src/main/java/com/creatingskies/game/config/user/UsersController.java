package com.creatingskies.game.config.user;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.config.common.TableViewController;
import com.creatingskies.game.model.IRecord;
import com.creatingskies.game.model.user.User;
import com.creatingskies.game.model.user.UserDao;

public class UsersController extends TableViewController {

	@FXML private TableView<User> usersTable;
	@FXML private TableColumn<User, String> firstNameColumn;
	@FXML private TableColumn<User, String> lastNameColumn;
	@FXML private TableColumn<User, String> usernameColumn;
	@FXML private TableColumn<User, String> typeColumn;
	@FXML private TableColumn<User, Object> actionColumn;
	
	public void show(){
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Users.fxml"));
            AnchorPane users = (AnchorPane) loader.load();
            MainLayout.getRootLayout().setCenter(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@FXML
	@SuppressWarnings("unchecked")
	private void initialize(){
		UserDao userDao = new UserDao();
		
		firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
		lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
		usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
		typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().toString()));
		
		actionColumn.setCellFactory(generateCellFactory(Action.ACTIVATE, Action.EDIT, Action.VIEW));
		usersTable.setItems(FXCollections.observableArrayList(userDao.findAll()));
	}

	@Override
	public TableView<? extends IRecord> getTableView() {
		return usersTable;
	}
	
	@Override
	protected void viewRecord(IRecord record) {
		super.deleteRecord(record);
		User item = (User) record;
		System.out.println("view - " + item.getFirstName());
	}
	
	@Override
	protected void editRecord(IRecord record) {
		super.deleteRecord(record);
		User item = (User) record;
		System.out.println("edit - " + item.getFirstName());
	}
	
	@Override
	protected void activateRecord(IRecord record) {
		super.deleteRecord(record);
		User item = (User) record;
		System.out.println("activate - " + item.getFirstName());
	}
	
}
