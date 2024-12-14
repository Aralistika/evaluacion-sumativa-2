package com.aiep.evsum2.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class JefesSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombrejefe", table, columnPrefix + "_nombrejefe"));
        columns.add(Column.aliased("telefonojefe", table, columnPrefix + "_telefonojefe"));

        return columns;
    }
}
