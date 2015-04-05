package com.creatingskies.game.config.company;

import java.io.IOException;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import com.creatingskies.game.classes.TableViewController;
import com.creatingskies.game.common.AlertDialog;
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
	
	private CompanyDAO companyDAO;
	
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
		super.init();
		companyDAO = new CompanyDAO();
		
		companyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getName()));
		companyActionColumn.setCellFactory(generateCellFactory(Action.EDIT, Action.DELETE));
		
		groupNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getName()));

		groupActionColumn.setCellFactory(generateCellFactory(groupsTable,
				Action.EDIT, Action.DELETE));
		
		companiesTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> loadCompanyDetails(newValue));
		
		resetTableView();
	}
	
	private void loadCompanyDetails(Company company) {
		groupsTable.setItems(FXCollections.observableArrayList(companyDAO
				.findAllGroupsForCompany(company)));
	}
	
	private void resetTableView(){
		companiesTable.setItems(FXCollections.observableArrayList(companyDAO
				.findAllCompanies()));
	}
	
	@FXML
	private void handleAdd() {
		Company company = new Company();
	    boolean saveClicked = new CompanyDialogController().show(company);
	    if (saveClicked) {
	        companyDAO.saveOrUpdate(company);
	        resetTableView();
	    }
	}
	
	@Override
	public TableView<? extends IRecord> getTableView() {
		return companiesTable;
	}
	
	@Override
	protected void deleteRecord(IRecord record) {
		super.deleteRecord(record);
		Optional<ButtonType> result = new AlertDialog(AlertType.CONFIRMATION, "Confirmation Dialog",
				"Are you sure you want to delete this record?", null).showAndWait();
		
		if(result.get() == ButtonType.OK){
			try {
				companyDAO.delete(record);
				resetTableView();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void editRecord(IRecord record) {
		super.editRecord(record);
		
		if(record instanceof Company){
			Company company = (Company) record;
			System.out.println("pls dont edit company " + company.getName());
		} else if(record instanceof Group){
			Group group = (Group) record;
			System.out.println("pls dont edit group " + group.getName());
		}
		
		
	}
	
}
