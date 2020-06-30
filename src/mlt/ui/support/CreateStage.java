package mlt.ui.support;

import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

class CreateStageTask extends Task<Stage>
{


    @Override
    protected Stage call() throws Exception {
        return null;
    }
}

public class CreateStage  {


    // creates Stage with specified parameter
    public void createStage(String title, String path , Stage s, int status , boolean initmodality , boolean resizable , boolean maximized )
    {
        if (s == null)
            s = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            Parent p = loader.load();
            s.setScene(new Scene(p));
            s.setTitle(title);
            if (initmodality)
                s.initModality(Modality.APPLICATION_MODAL);
            s.setResizable(resizable);
            s.setMaximized(maximized);
            s.getIcons().addAll(new Image("/mlt/list3.png"));
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // overloaded version of above to support for theme
    public void createStage(String title, String path , Stage s, int status , boolean initmodality , boolean resizable , boolean maximized, String theme )
    {
        if (s == null)
            s = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            Parent p = loader.load();
            Scene scene = new Scene(p);

            if (theme.equals("black"))
                scene.getStylesheets().setAll("mlt/ui/style/newStyle.css","mlt/ui/style/common.css","mlt/ui/style/style.css");
            else
            {
                // adding style file according to theme
                if (theme.contains("emerald")){
                    scene.getStylesheets().setAll("/mlt/ui/style/new3.css");

                }
                else if (theme.contains("teal")){
                    scene.getStylesheets().setAll("/mlt/ui/style/new4.css");
                }
                else if (theme.contains("blue")){
                    scene.getStylesheets().setAll("/mlt/ui/style/new5.css");
                }
                else
                    scene.getStylesheets().setAll("/mlt/ui/style/new2.css");
            }
            s.setScene(scene);
            s.setTitle(title);
            if (initmodality)
                s.initModality(Modality.APPLICATION_MODAL);
            s.setResizable(resizable);
            s.setMaximized(maximized);
            // setting stage icon
            s.getIcons().addAll(new Image("/mlt/list3.png"));
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FXMLLoader createStage2(String title, String path , Stage s, int status , boolean initmodality , boolean resizable , boolean maximized, String theme )
    {
        if (s == null)
            s = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            Parent p = loader.load();
            Scene scene = new Scene(p);

            if (theme.equals("black"))
                scene.getStylesheets().setAll("mlt/ui/style/newStyle.css","mlt/ui/style/common.css","mlt/ui/style/style.css");
            else
            {
                //scene.getStylesheets().setAll("mlt/ui/style/new2.css");
                if (theme.contains("emerald")){
                    scene.getStylesheets().setAll("/mlt/ui/style/new3.css");

                }
                else if (theme.contains("teal")){
                    scene.getStylesheets().setAll("/mlt/ui/style/new4.css");
                }
                else if (theme.contains("blue")){
                    scene.getStylesheets().setAll("/mlt/ui/style/new5.css");
                }
                else
                    scene.getStylesheets().setAll("/mlt/ui/style/new2.css");
            }
            s.setScene(scene);
            s.setTitle(title);
            if (initmodality)
                s.initModality(Modality.APPLICATION_MODAL);
            s.setResizable(resizable);
            s.setMaximized(maximized);
            s.getIcons().addAll(new Image("/mlt/list3.png"));
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader;
    }

    public FXMLLoader createStage(String title, String path , Stage s, int status , boolean initmodality , boolean resizable , boolean maximized, boolean retLoader )
    {


        if (s == null)
            s = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            Parent p = loader.load();
            s.setScene(new Scene(p));
            s.setTitle(title);
            if (initmodality)
                s.initModality(Modality.APPLICATION_MODAL);
            s.setResizable(resizable);
            s.setMaximized(maximized);
            s.getIcons().addAll(new Image("/mlt/list3.png"));
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }

    public FXMLLoader createStage(String title, String path , Stage s, int status , boolean initmodality , boolean resizable , boolean maximized, boolean retLoader , String theme)
    {

        if (s == null)
            s = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            Parent p = loader.load();
            Scene scene = new Scene(p);

            if (theme.contains("black"))
                scene.getStylesheets().setAll("mlt/ui/style/newStyle.css","mlt/ui/style/common.css","mlt/ui/style/style.css");
            else
            {
                //scene.getStylesheets().setAll("mlt/ui/style/new2.css");

                if (theme.contains("emerald")){
                    scene.getStylesheets().setAll("/mlt/ui/style/new3.css");

                }
                else if (theme.contains("teal")){
                    scene.getStylesheets().setAll("/mlt/ui/style/new4.css");
                }
                else if (theme.contains("blue")){
                    scene.getStylesheets().setAll("/mlt/ui/style/new5.css");
                }
                else
                    scene.getStylesheets().setAll("/mlt/ui/style/new2.css");
            }
            s.setScene(scene);

            s.setScene(scene);
            s.setTitle(title);
            if (initmodality)
                s.initModality(Modality.APPLICATION_MODAL);
            s.setResizable(resizable);
            s.setMaximized(maximized);
            s.getIcons().addAll(new Image("/mlt/list3.png"));
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }
}
