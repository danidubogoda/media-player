package lk.ijse.dep11.controller;

import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class MainSceneController {

    public AnchorPane root;
    public TextField txtBrowse;
    public Button btnBrowse;
    public MediaView mvScreen;
    public Button btnStart;
    public Button btnStop;
    public ProgressBar pbProgress;
    public Slider slider;
    public Label lblstart;
    public Label lblend;
    public Button btnPause;
    public Button btnMute;
    public JFXSlider sprPlay;
    public Label lblVolumn;
    public ImageView imvIMG;
    MediaPlayer videoPlayer;

    public void  initialize(){
        for (Button button : new Button[]{btnStart, btnStop, btnMute, btnPause}) {
            button.setDisable(true);
        }


    }
    public void btnBrowseOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().
                add(new FileChooser.ExtensionFilter("Video","*.mp4", "*.avi", "*.mkv"));
//        fileChooser.getExtensionFilters()
//                .add(new FileChooser.ExtensionFilter("Audio","*.mp3","*.wav", "*webm"));

        File videoFile = fileChooser.showOpenDialog(root.getScene().getWindow());
//        File audioFile = fileChooser.showOpenDialog(root.getScene().getWindow());

        if(videoFile!=null){
            System.out.println("Video.........");
            txtBrowse.setText(videoFile.getAbsolutePath());
            Media media = new Media(videoFile.toURI().toString());
            videoPlayer = new MediaPlayer(media);

            for (Button button : new Button[]{btnStart, btnStop, btnMute, btnPause}) {
                button.setDisable(false);
            }
        }else {
            txtBrowse.clear();
        }
    }

    public void btnStartOnAction(ActionEvent actionEvent) {
        if(videoPlayer!=null){
            mvScreen.setMediaPlayer(videoPlayer);
            videoPlayer.play();

            lblVolumn.setText(String.format("%d",(int)slider.getValue()));
             updateTime();
             changeVolumn();
           // setTime();
        }
    }

    private void changeVolumn() {
        videoPlayer.volumeProperty().addListener(e->{
            videoPlayer.setVolume(slider.getValue());
            double volume = videoPlayer.getVolume();
            System.out.println(volume);
            videoPlayer.setVolume(volume);
            System.out.println("player volumn -> "+videoPlayer.getVolume());
        });

        slider.valueProperty().addListener(e->{
            double sound = slider.getValue();
            lblVolumn.setText(String.valueOf((int) sound));

            videoPlayer.setVolume(sound);
            double volume = videoPlayer.getVolume();
            System.out.println("vol -> "+volume);

        });
    }




    public void btnStopOnAction(ActionEvent actionEvent) {
        if(videoPlayer!=null){
            mvScreen.setMediaPlayer(null);
            videoPlayer.stop();
            videoPlayer = null;
            sprPlay.setValue(0);
        }
    }

    public void updateTime(){

        videoPlayer.currentTimeProperty().addListener(observable -> {
            sprPlay.setMin(0);
            sprPlay.setMax(videoPlayer.getTotalDuration().toSeconds());

            int seconds = (int)videoPlayer.getCurrentTime().toSeconds();
            int s1 = seconds % 60;
            int s2 = seconds / 60;
            int s3 = s2 % 60;
            s2 = s2 / 60;

            String runTime = String.format("%02d:%02d:%02d",s2, s3, s1);

            int secondsT = (int) (videoPlayer.getTotalDuration().toSeconds()-videoPlayer.getCurrentTime().toSeconds());
            int e1 = secondsT % 60;
            int e2 = secondsT / 60;
            int e3 = e2 % 60;
            e2 = e2 / 60;
            String totalTime = String.format("%02d:%02d:%02d",e2, e3, e1);
            lblstart.setText(runTime);
            lblend.setText(String.valueOf(totalTime));

            sprPlay.setValue(videoPlayer.getCurrentTime().toSeconds());

        });

    }
    public void setTime(){

        sprPlay.valueProperty().addListener(e->{
            System.out.println("Calling sprPlay "+ sprPlay.getValue());
//            sprPlay.setMin(0);
//            sprPlay.setMax(videoPlayer.getTotalDuration().toSeconds());
            double value = sprPlay.getValue();
            videoPlayer.seek(Duration.seconds(value));

        });
    }

    public void btnPauseOnAction(ActionEvent actionEvent) {
        videoPlayer.pause();
    }


    public void btnMuteOnAction(ActionEvent actionEvent) {
        if(btnMute.getText().equals("Mute")) {
            videoPlayer.setMute(true);
            btnMute.setText("Unmute");
        }else{
            videoPlayer.setMute(false);
            btnMute.setText("Mute");
        }

    }

}
