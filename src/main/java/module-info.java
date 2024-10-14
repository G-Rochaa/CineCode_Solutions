module cineCode_2 {
	exports Control;
	exports Model;
	exports View;
	
    opens View to javafx.fxml;
    opens Model to javax.persistence,org.hibernate.orm.core;
    opens Control;

	requires java.persistence;
	requires java.sql;
	requires javafx.base;
	requires javafx.fxml;
    requires javafx.controls;
	requires javafx.graphics;
	requires net.bytebuddy;
	requires org.hibernate.orm.core;
	requires java.management;
	requires org.controlsfx.controls;
	requires java.desktop;
  }