/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp.clock;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import javafx.event.*;
import javafx.util.Duration;

import java.util.Calendar;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Label;

public class DigitalClock {
   
    private static DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

   
    
    

     /* private void bindToTime(Label lbl) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event -> lbl.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }*/
    public void bindToTime(Label lbl) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Calendar time = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        //System.out.println("" + simpleDateFormat.format(time.getTime()));
                         lbl.setText(simpleDateFormat.format(time.getTime()));
                    }
                }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
