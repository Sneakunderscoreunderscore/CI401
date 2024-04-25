module ci401.breakout {
    requires javafx.controls;
    requires javafx.fxml;

    opens ci401.breakout to javafx.fxml;
    exports ci401.breakout;
}
