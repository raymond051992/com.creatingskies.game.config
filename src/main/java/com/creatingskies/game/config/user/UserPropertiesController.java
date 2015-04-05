package com.creatingskies.game.config.user;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import com.creatingskies.game.classes.PropertiesViewController;
import com.creatingskies.game.common.AlertDialog;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.model.user.SecurityQuestion;
import com.creatingskies.game.model.user.User;
import com.creatingskies.game.model.user.User.Status;
import com.creatingskies.game.model.user.User.Type;
import com.creatingskies.game.model.user.UserDao;

public class UserPropertiesController extends PropertiesViewController{

	@FXML TextField firstNameTextField;
	@FXML TextField lastNameTextField;
	@FXML TextField userNameTextField;
	
	@FXML PasswordField passwordField;
	@FXML PasswordField confirmPasswordField;
	@FXML PasswordField securityAnswerField;
	
	@FXML ChoiceBox<Status> statusChoices;
	@FXML ChoiceBox<Type> typeChoices;
	@FXML ChoiceBox<SecurityQuestion> questionChoices;
	
	private User getUser(){
		return (User) getCurrentRecord();
	}
	
	@Override
	public void init() {
		super.init();
		statusChoices.setItems(FXCollections.observableArrayList(Status.values()));
		typeChoices.setItems(FXCollections.observableArrayList(Type.values()));
		questionChoices.setItems(FXCollections.observableArrayList(new UserDao()
				.findAllSecurityQuestions()));
		
		statusChoices.getSelectionModel().selectFirst();
		typeChoices.getSelectionModel().selectFirst();
		questionChoices.getSelectionModel().selectFirst();
		
		questionChoices.setConverter(new StringConverter<SecurityQuestion>() {
			@Override
			public String toString(SecurityQuestion object) {
				return object.getQuestion();
			}
			
			@Override
			public SecurityQuestion fromString(String string) {
				return null;
			}
		});
	}
	
	@Override
	protected String getViewTitle() {
		if(getCurrentAction() == Action.ADD){
			return "Create New User";
		} else if (getCurrentAction() == Action.EDIT) {
			return "Edit User " + getUser().getDisplayString();
		} else {
			return "User " + getUser().getDisplayString();
		}
	}
	
	public void show(Action action, User user){
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("UserProperties.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            UserPropertiesController controller = (UserPropertiesController) loader.getController();
            controller.setCurrentAction(action);
            controller.setCurrentRecord(user);
            controller.init();
            MainLayout.getRootLayout().setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@FXML
    private void handleSave() {
        if (isInputValid()) {
            getUser().setFirstName(firstNameTextField.getText());
            getUser().setLastName(lastNameTextField.getText());
            getUser().setUsername(userNameTextField.getText());
            getUser().setPassword(passwordField.getText());
            getUser().setType(typeChoices.getValue());
            getUser().setStatus(statusChoices.getValue());
            getUser().setSecurityQuestion(questionChoices.getValue());
            getUser().setSecurityQuestionAnswer(securityAnswerField.getText());
            new UserDao().saveOrUpdate(getUser());
            new UsersController().show();
        }
    }
	
    @FXML
    private void handleCancel() {
        new UsersController().show();
    }
    
    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameTextField.getText() == null || firstNameTextField.getText().length() == 0) {
            errorMessage += "Firstname is required.\n"; 
        }
        
        if (lastNameTextField.getText() == null || lastNameTextField.getText().length() == 0) {
            errorMessage += "Lastname is required.\n"; 
        }
        
        if (userNameTextField.getText() == null || userNameTextField.getText().length() == 0) {
            errorMessage += "Username is required.\n";
        } else {
        	User user = new UserDao().findActiveUser(userNameTextField.getText());
			if (user != null
					&& (getCurrentAction().equals(Action.EDIT) && user
							.getIdNo() != getUser().getIdNo())) {
        		errorMessage += "Username already exists.\n";
        	}
        }
        
        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "Password is required.\n";
        } else if (confirmPasswordField.getText() == null || confirmPasswordField.getText().length() == 0) {
            errorMessage += "Password confirmation is required.\n";
        } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
        	errorMessage += "Passwords doesn't match.\n";
        }
        
        if (securityAnswerField.getText() == null || securityAnswerField.getText().length() == 0) {
            errorMessage += "Answer for security question is required.\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
			new AlertDialog(AlertType.ERROR, "Invalid fields",
					"Please correct invalid fields.", errorMessage)
					.showAndWait();
            return false;
        }
    }

}
