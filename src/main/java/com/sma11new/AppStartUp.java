package com.sma11new;

import com.sma11new.config.Config;
import com.sma11new.controller.DisclaimerController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;

import java.util.Objects;


public class AppStartUp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 加载声明 FXML 文件
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Disclaimer.fxml"));
        Parent disclaimerRoot = loader.load();
        Scene scene = new Scene(disclaimerRoot);
        Stage dialogStage = new Stage();
        dialogStage.setScene(scene);
        dialogStage.setTitle("使用协议");
        dialogStage.showAndWait();
        boolean confirmed = DisclaimerController.isConfirmed();

        // 根据用户选择继续或退出
        if (confirmed) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Shiro.fxml")));
            primaryStage.setTitle(Config.NAME + " " + Config.VERSION);
            primaryStage.setScene(new Scene(root));
            // 退出程序的时候，子线程也一起退出
            primaryStage.setOnCloseRequest(new javafx.event.EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });
            // 设置窗口不可拉伸
            primaryStage.setResizable(false);

            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("img/ikun.png")).toString()));

            primaryStage.show();
        } else {
            // 用户点击取消或关闭窗口，退出应用
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}