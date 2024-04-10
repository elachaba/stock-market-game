import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Timer {
    private static Timer instance;
    private int secondsRemaining = 600;

    private Timer() {
        // Private constructor to prevent instantiation
        setupTimer();
    }

    public static synchronized Timer getInstance() {
        if (instance == null) {
            instance = new Timer();
        }
        return instance;
    }

    private void setupTimer() {
        // If time is up, reset the timer
        // Trigger any global timer events here
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    secondsRemaining--;

                    // If time is up, reset the timer
                    if (secondsRemaining < 0) {
                        secondsRemaining = 600;
                        // Trigger any global timer events here
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }
}
