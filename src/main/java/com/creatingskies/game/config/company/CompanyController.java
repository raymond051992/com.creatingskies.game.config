package com.creatingskies.game.config.company;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.creatingskies.game.classes.TableViewController;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.model.IRecord;
import com.creatingskies.game.model.company.Company;
import com.creatingskies.game.model.company.CompanyDAO;
import com.creatingskies.game.model.company.Group;

public class CompanyController extends TableViewController{

	@FXML private TableView<Company> companiesTable;
	@FXML private TableColumn<Company, String> companyNameColumn;
	@FXML private TableColumn<Company, Object> companyActionColumn;
	
	@FXML private TableView<Group> groupsTable;
	@FXML private TableColumn<Group, String> groupNameColumn;
	@FXML private TableColumn<Group, Object> groupActionColumn;
	
	@Override
	protected String getViewTitle() {
		return "Companies";
	}
	
	public void show(){
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Company.fxml"));
            AnchorPane companies = (AnchorPane) loader.load();
            MainLayout.getRootLayout().setCenter(companies);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@FXML
	@SuppressWarnings("unchecked")
	public void initialize(){
		super.initialize();
		companyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getName()));
		
		companyActionColumn.setCellFactory(generateCellFactory(Action.EDIT, Action.DELETE));
		resetCompanyList(new CompanyDAO());
		companiesTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> loadCompanyDetails(newValue));
	}
	
	@SuppressWarnings("unchecked")
	private void loadCompanyDetails(Company company) {
		CompanyDAO companyDAO = new CompanyDAO();

		groupNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
						cellData.getValue().getName()));

		groupActionColumn.setCellFactory(generateCellFactory(groupsTable,
				Action.EDIT, Action.DELETE));

		groupsTable.setItems(FXCollections.observableArrayList(companyDAO
				.findAllGroupsForCompany(company)));
	}
	
	private void resetCompanyList(CompanyDAO companyDAO){
		companiesTable.setItems(FXCollections.observableArrayList(companyDAO
				.findAllCompanies()));
	}
	
	@FXML
	private void handleAdd() {
		Company company = new Company();
	    boolean saveClicked = showCompanyDialog(company);
	    if (saveClicked) {
	        CompanyDAO companyDAO = new CompanyDAO();
	        companyDAO.saveOrUpdate(company);
	        resetCompanyList(companyDAO);
	    }
	}
	
	public boolean showCompanyDialog(Company company) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("CompanyDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Edit Company");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(MainLayout.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the company into the controller.
	        CompanyDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setCompany(company);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return controller.isSaveClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public TableView<? extends IRecord> getTableView() {
		return companiesTable;
	}
	
	@Override
	protected void deleteRecord(IRecord record) {
		super.deleteRecord(record);
		Company company = (Company) record;
		System.out.println("pls dont delete company " + company.getName());
	}
	
	@Override
	protected void editRecord(IRecord record) {
		super.editRecord(record);
		Company company = (Company) record;
		System.out.println("pls dont edit company " + company.getName());
	}
	
}
