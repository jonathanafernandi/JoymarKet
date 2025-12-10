module JoymarKet {
	requires java.sql;
	requires javafx.graphics;
	requires javafx.controls;
	
	opens main to javafx.graphics;
    opens view to javafx.graphics;
    opens model to javafx.base;
}