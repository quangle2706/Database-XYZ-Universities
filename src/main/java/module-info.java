module com.example.cop4710_xyzuniversity_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens quangle.db.xyzuniversity to javafx.fxml;
    exports quangle.db.xyzuniversity;
    exports quangle.db.xyzuniversity.models;
    opens quangle.db.xyzuniversity.models to javafx.fxml;
}