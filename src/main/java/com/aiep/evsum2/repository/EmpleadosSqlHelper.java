package com.aiep.evsum2.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EmpleadosSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombreempleado", table, columnPrefix + "_nombreempleado"));
        columns.add(Column.aliased("apellidoempleado", table, columnPrefix + "_apellidoempleado"));
        columns.add(Column.aliased("telefonoempleado", table, columnPrefix + "_telefonoempleado"));
        columns.add(Column.aliased("correoempleado", table, columnPrefix + "_correoempleado"));

        return columns;
    }
}
