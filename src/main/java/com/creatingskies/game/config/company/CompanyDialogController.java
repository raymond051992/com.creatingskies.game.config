package com.creatingskies.game.config.company;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.creatingskies.game.classes.TableViewController;
import com.creatingskies.game.common.AlertDialog;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.model.IRecord;
import com.creatingskies.game.model.company.Company;
import com.creatingskies.game.model.company.Group;

public class CompanyDialogController extends TableViewController {

	@FXML private TextField companyNameField;
	@FXML private TextField groupNameField;
	
	@FXML private TableView<Group> groupsTable;
	@FXML private TableColumn<Group, String> groupNameColumn;
	@FXML private TableColumn<Group, Object> groupActionColumn;
	
	private Stage dialogStage;
	private Company company;
	private boolean saveClicked = false;
	
	public boolean show(Company company) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("CompanyDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Company");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(MainLayout.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        CompanyDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setCompany(company);

	        dialogStage.showAndWait();
	        return controller.isSaveClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	@Override
	protected String getViewTitle() {
		return "Companies";
	}
	
	@SuppressWarnings("unchecked")
	public void initialize() {
		super.init();
		groupNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getName()));
		groupActionColumn.setCellFactory(generateCellFactory(Action.DELETE));
	}

	public void setCompany(Company company) {
        this.company = company;
        companyNameField.setText(company.getName());
    }
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	@Override
	public TableView<? extends IRecord> getTableView() {
		return groupsTable;
	}
	
	public boolean isSaveClicked() {
        return saveClicked;
    }
	
	@FXML
	private void handleAddGroup(){
		Group group = new Group();
		group.setCompany(company);
		group.setName(groupNameField.getText());
		company.getGroups().add(group);
		groupsTable.getItems().add(group);
		
		groupNameField.clear();
	}
	
	@FXML
    private void handleSave() {
        if (isInputValid()) {
            company.setName(companyNameField.getText());
            saveClicked = true;
            dialogStage.close();
        }
    }
	
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (companyNameField.getText() == null || companyNameField.getText().length() == 0) {
            errorMessage += "No company name!\n"; 
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
    
	@Override
	protected void deleteRecord(IRecord record) {
		super.deleteRecord(record);
		groupsTable.getItems().remove(record);
	}

}
