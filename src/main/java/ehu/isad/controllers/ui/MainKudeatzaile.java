package ehu.isad.controllers.ui;

import ehu.isad.WhatWebFX;
import ehu.isad.controllers.db.WhatWebKud;
import ehu.isad.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.ProcessHandle.allProcesses;

public class MainKudeatzaile implements Initializable {

    private WhatWebFX mainApp;

    public void setMainApp(WhatWebFX main){
        this.mainApp = main;
    }

    @FXML
    private Button btn_cms;

    @FXML
    private TextField txt_bilatu;


    @FXML
    void onClickAddURL(ActionEvent event) {
        this.botoiaFocus();

        //Idatzi Kargatzen bla bla bla
        //Irudi de homer pentsatzen (monito con pandereta)

        Thread taskThread = new Thread( () -> {

            String newLine = System.getProperty("line.separator");
            final StringBuilder emaitza = new StringBuilder();
            urlIrakurri(txt_bilatu.getText()).forEach(line ->  {
                emaitza.append( line + newLine );
            });

            try {
                WhatWebKud.getInstantzia().insertIrakurri();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Platform.runLater( () -> {
                //txt_bilatu.setText(emaitza.toString());
            } );

        });

        taskThread.start();



    }


    @FXML
    void onClickClose(ActionEvent event) {
        Stage stage=mainApp.getStage();
        stage.close();
    }

    @FXML
    void onClickCMS(ActionEvent event) {

    }

    @FXML
    void onClickServer(ActionEvent event) {
    }

    @FXML
    void onClickWhatWeb(ActionEvent event) {
        this.mainApp.WhatWeb();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void botoiaFocus(){
        Platform.runLater(() -> btn_cms.requestFocus());
    }

    public List<String> urlIrakurri(String url) {
        List<String> processes = new LinkedList<String>();
        try {
            String line;
            Process p = null;
            String komandoa = "whatweb --colour='never' --log-sql=insertak.sql " + url;
            if(System.getProperty("os.name").toLowerCase().contains("win")) {
                komandoa = "wsl " + komandoa;
            }
            p = Runtime.getRuntime().exec(komandoa);

            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                processes.add(line);
                System.out.println(line);
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

        return processes;
    }
}
