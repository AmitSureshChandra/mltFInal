package mlt;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;

import java.sql.SQLException;

public class MainMethod extends Application {

    boolean flag = false;

    @Override
    public void start(Stage primaryStage) throws Exception{
     //   Parent root = FXMLLoader.load(getClass().getResource("/mlt/ui/preloader/preloader.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/mlt/ui/login/Login.fxml"));
  //        Parent root = FXMLLoader.load(getClass().getResource("/test/test/test.fxml"));
      //  Parent root = FXMLLoader.load(getClass().getResource("/mlt/ui/setting/setting.fxml"));
       // primaryStage.setTitle("Teacher Setup");
        primaryStage.setTitle("User Login");
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        scene.getStylesheets().setAll("/mlt/ui/style/new2.css");


        primaryStage.setScene(scene);
        primaryStage.getIcons().addAll(new Image("/mlt/user2.png"));


        primaryStage.setResizable(false);
        primaryStage.show();

        if (flag)
            new MyAlert().createAlert("Couldn't Connect to Local Server","Error",primaryStage);
    }

    class DbInit extends Task<Void>{

        @Override
        protected Void call() throws Exception {

            try {
                DbHandler.excAction("USE meritlist ");
                DbHandler.createLoginTable();

                DbHandler.createAccountTable();
                DbHandler.createOperationTable();
                DbHandler.createCourseTable();
                DbHandler.createTeacherTable();
                DbHandler.createSubjectTable();
                DbHandler.createMeritListTable();
                DbHandler.createProfilePhotoTable();
                DbHandler.createPerSeatsTable();
                DbHandler.createTotalSeatsTable();
                DbHandler.createMltDataTable();
                System.out.println("All required table created ");
            } catch (Exception e) {
                flag = true;
                System.out.println("Table creation Error");
            }
            return null;
        }
    }

    @Override
    public void init() {



        //new Thread(new DbInit()).start();

       try{
            DbHandler.excAction("USE meritlist ");
            DbHandler.createAllRequiredTable();
            System.out.println("All required table created ");

        } catch (SQLException e) {
            flag = true;
            System.out.println("Table creation Error");
            System.out.println("Couldn't Connect to Local Server");
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        try{
            DbHandler.getConnection().close();
        }catch (Exception e){

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}