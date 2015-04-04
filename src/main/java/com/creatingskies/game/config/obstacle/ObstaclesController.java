package com.creatingskies.game.config.obstacle;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import com.creatingskies.game.classes.TableViewController;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.model.IRecord;
import com.creatingskies.game.model.obstacle.Obstacle;
import com.creatingskies.game.model.obstacle.ObstacleDAO;

public class ObstaclesController extends TableViewController{

	@FXML private TableView<Obstacle> obstaclesTable;
	@FXML private TableColumn<Obstacle, String> nameColumn;
	@FXML private TableColumn<Obstacle, Object> actionColumn;
	
	public void show(){
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Obstacles.fxml"));
            AnchorPane obstacles = (AnchorPane) loader.load();
            MainLayout.getRootLayout().setCenter(obstacles);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@FXML
	@SuppressWarnings("unchecked")
	public void initialize(){
		super.initialize();
		nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getName()));
		
		actionColumn.setCellFactory(generateCellFactory(Action.EDIT, Action.DELETE));
		resetTableView(new ObstacleDAO());
	}
	
	private void resetTableView(ObstacleDAO obstacleDAO){
		obstaclesTable.setItems(FXCollections
				.observableArrayList(obstacleDAO.findAll()));
	}
	
	@Override
	protected String getViewTitle() {
		return "Obstacles";
	}
	
	@Override
	public TableView<? extends IRecord> getTableView() {
		return obstaclesTable;
	}

}
