package view;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static java.nio.file.StandardCopyOption.*;

// install e(fx)clipse from eclipse marketplace and restart eclipse
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import reader.Breakdown;
import reader.Data;
import reader.Lip;
import reader.LSFileReader;

public class Runner extends Application {

    private Desktop desktop = Desktop.getDesktop();
    private Stage primaryStage;
    private static BorderPane mainLayout;
    private static Data d;
    

    /**
     * fxml tag allows this to be seen by scene builder to bind it
     * to choicebox within scene builder
     */
    @FXML
    private ChoiceBox<String> fileTypes;
    @FXML
    private ChoiceBox<String> mouthTypes;
    @FXML
    private TextField frameStart;
    private static int frameStartInt;

    @FXML
    private TextField frameEnd;
    private static int frameEndInt;

    @FXML
    private Button fileButton;

    @FXML
    private Label filename;

    @Override
    public void start(final Stage stage) throws IOException {
    	this.primaryStage = stage;
    	this.primaryStage.setTitle("Papagayo .dat Parser");

    	showMainView();
    	showDatFileForm();
    }

    /**
     * called on the form loaded which may not contain all the fxml vars,
     * hence null checks
     */
    @FXML
    private void initialize() {
    	ArrayList<String> types = new ArrayList<String>();
    	ObservableList<String> list = FXCollections.observableArrayList(types);

    	if(mouthTypes != null) {
	    	types = new ArrayList<String>();
	    	try {
				Stream<Path> sp = Files.walk(Paths.get("src/assets")).filter(Files::isRegularFile)
					     .map(p -> p.getParent())
					     .distinct();
				Iterator<Path> it = sp.iterator();
				System.out.println(it.toString());
				while(it.hasNext()) {
					types.add(it.next().toString().split("\\\\")[2]);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	list = FXCollections.observableArrayList(types);
	    	mouthTypes.setItems(list);
	    	mouthTypes.setValue("default");
    	}


        final FileChooser fileChooser = new FileChooser();

        if(fileButton != null) {
	        fileButton.setOnAction(
	            new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {
	                    File file = fileChooser.showOpenDialog(primaryStage);
	                    if (file != null) {
	                        openFile(file);
	                    }
	                }
	            });
        }
        
        if(frameStart != null) {
        	numbersOnly(frameStart);
        	frameStart.textProperty().addListener((observable, oldValue, newValue) -> {
        	    System.out.println("fs textfield changed from " + oldValue + " to " + newValue);
        	    frameStartInt = Integer.parseInt(newValue);
        	});
        }
        if(frameEnd != null) {
        	numbersOnly(frameEnd);
        	frameEnd.textProperty().addListener((observable, oldValue, newValue) -> {
        	    System.out.println("fe textfield changed from " + oldValue + " to " + newValue);
        	    frameEndInt = Integer.parseInt(newValue);
        	});
        }
    }

    private void numbersOnly(TextField field) {
    	field.textProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(ObservableValue<? extends String> observable, String oldValue,
    	        String newValue) {
    	        if (!newValue.matches("\\d*")) {
    	            field.setText(newValue.replaceAll("[^\\d]", ""));
    	        }
    	    }
    	});
    }
    
//    private void switchForm(Number index) throws IOException {
//    	System.out.println(fileTypes.getValue());
//    	if(index.intValue() == LINETYPE.NA.ordinal() || d == null) {
//    		fileTypes.getSelectionModel().select(LINETYPE.NA.ordinal());
//    		mainLayout.setCenter(null);
//    	}
//    }
    
    private void showDatFileForm() throws IOException {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(Runner.class.getResource("/view/DatFileForm.fxml"));
    	BorderPane datFileForm = loader.load();
    	mainLayout.setCenter(datFileForm);
	}

	private void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(Runner.class.getResource("/view/MainView.fxml"));
        mainLayout = loader.load();
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void openFile(File file) {
        try {
    		String path = file.getAbsolutePath();
            System.out.println(path);
            d = LSFileReader.readFile(path);
            path = path.split("\\\\")[path.split("\\\\").length-1];
            filename.setText(path);
            frameStartInt = 1;
            frameEndInt = d.getBreakdowns().get(d.getBreakdowns().size()-1).getFrameNum();
        }
        catch (IOException ex) {
            Logger.getLogger(
                FileChooser.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }

    @FXML
    private void generate() throws Exception {
    	System.out.println("CLICKED!");
    	d.getOutputDir().mkdirs();
    	System.out.println(d.getBreakdowns().size());
    	int frameNum = frameStartInt;
    	for(int currBreakdownIndex = 0; currBreakdownIndex < d.getBreakdowns().size()-1; currBreakdownIndex++) {
    		int nextBreakdown = currBreakdownIndex+1;
    		while(frameNum < d.getBreakdowns().get(nextBreakdown).getFrameNum()-1 && frameNum < frameEndInt+1) {
    			Breakdown currBreakdown = d.getBreakdowns().get(currBreakdownIndex);
    			File file = new File(d.getOutputDir(), "out"+frameNum+".png");

    			System.out.println(file.getAbsolutePath());
    			Files.copy(Lip.getFile(currBreakdown.getPh(), mouthTypes.getValue()).toPath(), file.toPath(), REPLACE_EXISTING);
    			
    			frameNum++;
    		}
    	}
    }
}