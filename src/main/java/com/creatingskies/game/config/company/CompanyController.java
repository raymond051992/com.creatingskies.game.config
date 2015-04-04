package com.creatingskies.game.config.company;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import com.creatingskies.game.common.AlertDialog;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.config.common.TableViewController;
import com.creatingskies.game.model.IRecord;
import com.creatingskies.game.model.company.Company;
import com.creatingskies.game.model.company.CompanyDAO;

public class CompanyController extends TableViewController{

	@FXML private TableView<Company> companiesTable;
	@FXML private TableColumn<Company, String> nameColumn;
	@FXML private TableColumn<Company, Object> actionColumn;
	
	public void show(){
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Companies.fxml"));
            AnchorPane companies = (AnchorPane) loader.load();
            MainLayout.getRootLayout().setCenter(companies);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@FXML
	@SuppressWarnings("unchecked")
	private void initialize(){
		CompanyDAO companyDAO = new CompanyDAO();
		
		nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getName()));
		
		actionColumn.setCellFactory(generateCellFactory(Action.EDIT, Action.DELETE));
		companiesTable.setItems(FXCollections.observableArrayList(companyDAO.findAll()));
		companiesTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> loadCompanyDetails(newValue));
	}
	
	private void loadCompanyDetails(Company company) {
	    new AlertDialog(AlertType.CONFIRMATION, "Company clicked", "Test", "Boo!").showAndWait();
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
		super.deleteRecord(record);
		Company company = (Company) record;
		System.out.println("pls dont edit company " + company.getName());
	}

	
}
