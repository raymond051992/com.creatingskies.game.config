package com.creatingskies.game.config.obstacle;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.creatingskies.game.classes.Util;
import com.creatingskies.game.classes.ViewController;
import com.creatingskies.game.common.AlertDialog;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.model.obstacle.Obstacle;

public class ObstacleDialogController extends ViewController {

	@FXML private TextField nameField;
	@FXML private CheckBox forRowingCheckBox;
	@FXML private CheckBox forCyclingCheckBox;
	@FXML private Slider difficultySlider;
	@FXML private TextField fileNameField;
	
	private Stage dialogStage;
	private Obstacle obstacle;
	private boolean saveClicked = false;
	private final String NO_FILE_MESSAGE = "Please choose a file.";
	
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
		fileNameField.setText(NO_FILE_MESSAGE);
	}
	
	public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
        nameField.setText(obstacle.getName());
        
        difficultySlider.setValue(obstacle.getDifficulty() != null ?
        		obstacle.getDifficulty() : 0.0);
        
        forRowingCheckBox.setSelected(obstacle.getForRowing());
        forCyclingCheckBox.setSelected(obstacle.getForCycling());
        
        fileNameField.setText(obstacle.getImageFileName() != null
        		&& !obstacle.getImageFileName().equals("") ?
        		obstacle.getImageFileName() : NO_FILE_MESSAGE);
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
        
        obstacle.setImage(Util.fileToByteArray(file));
        obstacle.setImageFileName(file != null ? file.getName() : null);
		obstacle.setImageFileType(Util.getFileExtension(obstacle
				.getImageFileName()));
        
		fileNameField.setText(obstacle.getImageFileName() != null
        		&& !obstacle.getImageFileName().equals("") ?
        		obstacle.getImageFileName() : NO_FILE_MESSAGE);
	}
	
	@FXML
    private void handleSave() {
        if (isInputValid()) {
            obstacle.setName(nameField.getText());
            obstacle.setDifficulty((int) difficultySlider.getValue());
            obstacle.setForRowing(forRowingCheckBox.isSelected());
            obstacle.setForCycling(forCyclingCheckBox.isSelected());
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

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "Obstacle name is required.\n"; 
        }
        
        if (!forRowingCheckBox.isSelected() && !forCyclingCheckBox.isSelected()){
        	errorMessage += "At least one game type should be selected.\n";
        }
        
        if(fileNameField.getText().equals(NO_FILE_MESSAGE)){
        	errorMessage += "Image for obstacle is required.\n";
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
