module com.stm.pegelhub.library {
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.slf4j;
    requires java.sql;
    opens com.stm.pegelhub.lib.model;
    opens com.stm.pegelhub.lib.internal.dto;
    exports com.stm.pegelhub.lib;
    exports com.stm.pegelhub.lib.model;
}