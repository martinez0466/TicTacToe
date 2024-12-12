module com.example.tictactoe6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tictactoe6 to javafx.fxml;
    exports com.example.tictactoe6;
}