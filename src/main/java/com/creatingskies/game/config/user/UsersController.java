package com.creatingskies.game.config.user;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.common.TableRowActivateButton;
import com.creatingskies.game.common.TableRowEditButton;
import com.creatingskies.game.common.TableRowViewButton;
import com.creatingskies.game.model.user.User;
import com.creatingskies.game.model.user.UserDao;

public class UsersController {

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
	private void initialize(){
		UserDao userDao = new UserDao();
		
		firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
		lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
		usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
		typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().toString()));
		
		
		Callback<TableColumn<User, Object>, TableCell<User, Object>> 
			printColumnCellFactory = 
                new Callback<TableColumn<User, Object>, TableCell<User, Object>>() {

            @Override
            public TableCell<User, Object> call(final TableColumn<User,Object> param) {
                final TableCell<User, Object> cell = new TableCell<User, Object>() {

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                        	ButtonBar buttonBar = new ButtonBar();
                        	final Button viewButton = new TableRowViewButton();
                        	final Button editButton = new TableRowEditButton();
                        	final Button activateButton = new TableRowActivateButton();
                            viewButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    param.getTableView().getSelectionModel().select(getIndex());
                                    User item = usersTable.getSelectionModel().getSelectedItem();
                                    if (item != null) {
                                        System.out.println("view - "+item.getFirstName());
                                    }
                                }
                            });
                            
                            editButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    param.getTableView().getSelectionModel().select(getIndex());
                                    User item = usersTable.getSelectionModel().getSelectedItem();
                                    if (item != null) {
                                        System.out.println("edit - "+item.getFirstName());
                                    }
                                }
                            });
                            
                            activateButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    param.getTableView().getSelectionModel().select(getIndex());
                                    User item = usersTable.getSelectionModel().getSelectedItem();
                                    if (item != null) {
                                        System.out.println("activate - "+item.getFirstName());
                                    }
                                }
                            });
                            
                            buttonBar.setButtonMinWidth(20);
                            buttonBar.setMinWidth(90);
                            buttonBar.setPrefWidth(90);
                            buttonBar.setMaxWidth(90);
                            buttonBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                            
                            buttonBar.getButtons().add(activateButton);
                            buttonBar.getButtons().add(editButton);
                            buttonBar.getButtons().add(viewButton);
                            
                            
                            
                            setAlignment(Pos.CENTER);
                            setGraphic(buttonBar);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        }
                    }
                };
                return cell;
            }
        };
        actionColumn.setCellFactory(printColumnCellFactory);

		
		usersTable.setItems(FXCollections.observableArrayList(userDao.findAll()));
	}
}
