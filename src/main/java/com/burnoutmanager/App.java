package com.burnoutmanager;

import com.burnoutmanager.util.DadosIniciais;
import com.burnoutmanager.util.HibernateUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        inicializarDados();
        
        scene = new Scene(loadFXML("main"), 1024, 768);
        stage.setScene(scene);
        stage.setTitle("Sistema de Gerenciamento de Burnout");
        stage.show();
        
        stage.setOnCloseRequest(event -> {
            HibernateUtil.shutdown();
            Platform.exit();
        });
    }
    
    private void inicializarDados() {
        try {
            HibernateUtil.getSessionFactory();
            DadosIniciais.carregarDados();
            System.out.println("Sistema inicializado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar o sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
} 