package com.aiep.evsum2.repository;

import com.aiep.evsum2.domain.DepartamentosJefes;
import com.aiep.evsum2.repository.rowmapper.DepartamentosJefesRowMapper;
import com.aiep.evsum2.repository.rowmapper.DepartamentosRowMapper;
import com.aiep.evsum2.repository.rowmapper.JefesRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the DepartamentosJefes entity.
 */
@SuppressWarnings("unused")
class DepartamentosJefesRepositoryInternalImpl
    extends SimpleR2dbcRepository<DepartamentosJefes, Long>
    implements DepartamentosJefesRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DepartamentosRowMapper departamentosMapper;
    private final JefesRowMapper jefesMapper;
    private final DepartamentosJefesRowMapper departamentosjefesMapper;

    private static final Table entityTable = Table.aliased("departamentos_jefes", EntityManager.ENTITY_ALIAS);
    private static final Table departamentosTable = Table.aliased("departamentos", "departamentos");
    private static final Table jefesTable = Table.aliased("jefes", "jefes");

    public DepartamentosJefesRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DepartamentosRowMapper departamentosMapper,
        JefesRowMapper jefesMapper,
        DepartamentosJefesRowMapper departamentosjefesMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(DepartamentosJefes.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.departamentosMapper = departamentosMapper;
        this.jefesMapper = jefesMapper;
        this.departamentosjefesMapper = departamentosjefesMapper;
    }

    @Override
    public Flux<DepartamentosJefes> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<DepartamentosJefes> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DepartamentosJefesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(DepartamentosSqlHelper.getColumns(departamentosTable, "departamentos"));
        columns.addAll(JefesSqlHelper.getColumns(jefesTable, "jefes"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(departamentosTable)
            .on(Column.create("departamentos_id", entityTable))
            .equals(Column.create("id", departamentosTable))
            .leftOuterJoin(jefesTable)
            .on(Column.create("jefes_id", entityTable))
            .equals(Column.create("id", jefesTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, DepartamentosJefes.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DepartamentosJefes> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<DepartamentosJefes> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<DepartamentosJefes> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<DepartamentosJefes> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<DepartamentosJefes> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private DepartamentosJefes process(Row row, RowMetadata metadata) {
        DepartamentosJefes entity = departamentosjefesMapper.apply(row, "e");
        entity.setDepartamentos(departamentosMapper.apply(row, "departamentos"));
        entity.setJefes(jefesMapper.apply(row, "jefes"));
        return entity;
    }

    @Override
    public <S extends DepartamentosJefes> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
