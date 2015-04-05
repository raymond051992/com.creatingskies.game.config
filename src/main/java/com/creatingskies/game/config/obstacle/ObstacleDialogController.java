package com.creatingskies.game.config.obstacle;

import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.creatingskies.game.classes.ViewController;
import com.creatingskies.game.common.AlertDialog;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.core.Game.Type;
import com.creatingskies.game.model.obstacle.Obstacle;

public class ObstacleDialogController extends ViewController {

	@FXML private TextField nameField;
	@FXML private ChoiceBox<Type> gameTypeChoices;
	@FXML private Slider slider;
	
	private Stage dialogStage;
	private Obstacle obstacle;
	private boolean saveClicked = false;
	
	public boolean show(Obstacle obstacle) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("ObstacleDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Obstacle");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(MainLayout.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        ObstacleDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setObstacle(obstacle);
	        
	        dialogStage.showAndWait();
	        return controller.isSaveClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public void initialize() {
		super.init();
		
		gameTypeChoices.setItems(FXCollections
				.observableArrayList(Type.values()));
		
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMin(0);
		slider.setMajorTickUnit(1);
		slider.setMax(7);
	}

	public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
        nameField.setText(obstacle.getName());
        
        slider.setValue(obstacle.getDifficulty() != null ?
        		obstacle.getDifficulty() : 0.0);
        
        if(obstacle.getGameType() != null){
        	gameTypeChoices.setValue(obstacle.getGameType());
        }
    }
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	public boolean isSaveClicked() {
        return saveClicked;
    }
	
	@Override
	protected String getViewTitle() {
		return "Obstacles";
	}
	
	@FXML
	private void handleOpenBrowseDialog(){
		FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Image files", "*.jpeg", "*.jpg", "*.png", "*.bmp", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(MainLayout.getPrimaryStage());

        if (file != null) {
            new AlertDialog(AlertType.INFORMATION, "Todo!", "Todo: Under construction.",
            		file.getAbsolutePath()).show();;
        }
	}
	
	@FXML
    private void handleSave() {
        if (isInputValid()) {
            obstacle.setName(nameField.getText());
            obstacle.setDifficulty((int) slider.getValue());
            obstacle.setGameType(gameTypeChoices.getValue());
            saveClicked = true;
            dialogStage.close();
        }
    }
	
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    private boolean isInputValid() {
    	//TODO Add validation
    	
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No obstacle name!\n"; 
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
