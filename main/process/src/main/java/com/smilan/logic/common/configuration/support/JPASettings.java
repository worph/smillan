package com.smilan.logic.common.configuration.support;

/**
 * @author Thomas
 *
 */
public final class JPASettings {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // JPA defined settings
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * THe name of the {@link javax.persistence.spi.PersistenceProvider} implementor
     * <p/>
     * See JPA 2 sections 9.4.3 and 8.2.1.4
     */
    public static final String PROVIDER                         = "javax.persistence.provider";

    /**
     * The type of transactions supported by the entity managers.
     * <p/>
     * See JPA 2 sections 9.4.3 and 8.2.1.2
     */
    public static final String TRANSACTION_TYPE                 = "javax.persistence.transactionType";

    /**
     * The JNDI name of a JTA {@link javax.sql.DataSource}.
     * <p/>
     * See JPA 2 sections 9.4.3 and 8.2.1.5
     */
    public static final String JTA_DATASOURCE                   = "javax.persistence.jtaDataSource";

    /**
     * The JNDI name of a non-JTA {@link javax.sql.DataSource}.
     * <p/>
     * See JPA 2 sections 9.4.3 and 8.2.1.5
     */
    public static final String NON_JTA_DATASOURCE               = "javax.persistence.nonJtaDataSource";

    /**
     * The name of a JDBC driver to use to connect to the database.
     * <p/>
     * Used in conjunction with {@link #JDBC_URL}, {@link #JDBC_USER} and {@link #JDBC_PASSWORD} to define how to make connections to the database in lieu of a datasource (either {@link #JTA_DATASOURCE} or {@link #NON_JTA_DATASOURCE}).
     * <p/>
     * See section 8.2.1.9
     */
    public static final String JDBC_DRIVER                      = "javax.persistence.jdbc.driver";

    /**
     * The JDBC connection url to use to connect to the database.
     * <p/>
     * Used in conjunction with {@link #JDBC_DRIVER}, {@link #JDBC_USER} and {@link #JDBC_PASSWORD} to define how to make connections to the database in lieu of a datasource (either {@link #JTA_DATASOURCE} or {@link #NON_JTA_DATASOURCE}).
     * <p/>
     * See section 8.2.1.9
     */
    public static final String JDBC_URL                         = "javax.persistence.jdbc.url";

    /**
     * The JDBC connection user name.
     * <p/>
     * Used in conjunction with {@link #JDBC_DRIVER}, {@link #JDBC_URL} and {@link #JDBC_PASSWORD} to define how to make connections to the database in lieu of a datasource (either {@link #JTA_DATASOURCE} or {@link #NON_JTA_DATASOURCE}).
     * <p/>
     * See section 8.2.1.9
     */
    public static final String JDBC_USER                        = "javax.persistence.jdbc.user";

    /**
     * The JDBC connection password.
     * <p/>
     * Used in conjunction with {@link #JDBC_DRIVER}, {@link #JDBC_URL} and {@link #JDBC_USER} to define how to make connections to the database in lieu of a datasource (either {@link #JTA_DATASOURCE} or {@link #NON_JTA_DATASOURCE}).
     * <p/>
     * See JPA 2 section 8.2.1.9
     */
    public static final String JDBC_PASSWORD                    = "javax.persistence.jdbc.password";

    /**
     * Used to indicate whether second-level (what JPA terms shared cache) caching is
     * enabled as per the rules defined in JPA 2 section 3.1.7.
     * <p/>
     * See JPA 2 sections 9.4.3 and 8.2.1.7
     *
     * @see javax.persistence.SharedCacheMode
     */
    public static final String SHARED_CACHE_MODE                = "javax.persistence.sharedCache.mode";

    /**
     * NOTE : Not a valid EMF property...
     * <p/>
     * Used to indicate if the provider should attempt to retrieve requested data in the shared cache.
     *
     * @see javax.persistence.CacheRetrieveMode
     */
    public static final String SHARED_CACHE_RETRIEVE_MODE       = "javax.persistence.cache.retrieveMode";

    /**
     * NOTE : Not a valid EMF property...
     * <p/>
     * Used to indicate if the provider should attempt to store data loaded from the database in the shared cache.
     *
     * @see javax.persistence.CacheStoreMode
     */
    public static final String SHARED_CACHE_STORE_MODE          = "javax.persistence.cache.storeMode";

    /**
     * Used to indicate what form of automatic validation is in effect as per rules defined
     * in JPA 2 section 3.6.1.1
     * <p/>
     * See JPA 2 sections 9.4.3 and 8.2.1.8
     *
     * @see javax.persistence.ValidationMode
     */
    public static final String VALIDATION_MODE                  = "javax.persistence.validation.mode";

    /**
     * Used to pass along any discovered validator factory.
     */
    public static final String VALIDATION_FACTORY               = "javax.persistence.validation.factory";

    /**
     * Used to request (hint) a pessimistic lock scope.
     * <p/>
     * See JPA 2 sections 8.2.1.9 and 3.4.4.3
     */
    public static final String LOCK_SCOPE                       = "javax.persistence.lock.scope";

    /**
     * Used to request (hint) a pessimistic lock timeout (in milliseconds).
     * <p/>
     * See JPA 2 sections 8.2.1.9 and 3.4.4.3
     */
    public static final String LOCK_TIMEOUT                     = "javax.persistence.lock.timeout";

    /**
     * Used to coordinate with bean validators
     * <p/>
     * See JPA 2 section 8.2.1.9
     */
    public static final String PERSIST_VALIDATION_GROUP         = "javax.persistence.validation.group.pre-persist";

    /**
     * Used to coordinate with bean validators
     * <p/>
     * See JPA 2 section 8.2.1.9
     */
    public static final String UPDATE_VALIDATION_GROUP          = "javax.persistence.validation.group.pre-update";

    /**
     * Used to coordinate with bean validators
     * <p/>
     * See JPA 2 section 8.2.1.9
     */
    public static final String REMOVE_VALIDATION_GROUP          = "javax.persistence.validation.group.pre-remove";

    /**
     * Used to pass along the CDI BeanManager, if any, to be used.
     */
    public static final String CDI_BEAN_MANAGER                 = "javax.persistence.bean.manager";

    /**
     * Specifies whether schema generation commands for schema creation are to be determine based on object/relational
     * mapping metadata, DDL scripts, or a combination of the two.
     * If no value is specified, a default is assumed as follows:
     * <ul>
     * <li>
     * if source scripts are specified (per {@value #SCHEMA_GEN_CREATE_SCRIPT_SOURCE}),then "scripts" is assumed</li>
     * <li>
     * otherwise, "metadata" is assumed</li>
     * </ul>
     *
     * See JPA 2 section 8.2.1.9
     */
    public static final String SCHEMA_GEN_CREATE_SOURCE         = "javax.persistence.schema-generation.create-source";

    /**
     * Specifies whether schema generation commands for schema dropping are to be determine based on object/relational
     * mapping metadata, DDL scripts, or a combination of the two.
     * If no value is specified, a default is assumed as follows:
     * <ul>
     * <li>
     * if source scripts are specified (per {@value #SCHEMA_GEN_DROP_SCRIPT_SOURCE}),then "scripts" is assumed</li>
     * <li>
     * otherwise, "metadata" is assumed</li>
     * </ul>
     *
     * See JPA 2 section 8.2.1.9
     */
    public static final String SCHEMA_GEN_DROP_SOURCE           = "javax.persistence.schema-generation.drop-source";

    /**
     * Specifies the CREATE script file as either a {@link java.io.Reader} configured for reading of the DDL script
     * file or a string designating a file {@link java.net.URL} for the DDL script.
     *
     * @see #SCHEMA_GEN_CREATE_SOURCE
     * @see #SCHEMA_GEN_DROP_SCRIPT_SOURCE
     */
    public static final String SCHEMA_GEN_CREATE_SCRIPT_SOURCE  = "javax.persistence.schema-generation.create-script-source";

    /**
     * Specifies the DROP script file as either a {@link java.io.Reader} configured for reading of the DDL script
     * file or a string designating a file {@link java.net.URL} for the DDL script.
     *
     * @see #SCHEMA_GEN_DROP_SOURCE
     * @see #SCHEMA_GEN_CREATE_SCRIPT_SOURCE
     */
    public static final String SCHEMA_GEN_DROP_SCRIPT_SOURCE    = "javax.persistence.schema-generation.drop-script-source";

    /**
     * Specifies the type of schema generation action to be taken by the persistence provider in regards to sending
     * commands directly to the database via JDBC.
     * <p/>
     * If no value is specified, the default is "none".
     *
     * See JPA 2 section 8.2.1.9
     */
    public static final String SCHEMA_GEN_DATABASE_ACTION       = "javax.persistence.schema-generation.database.action";

    /**
     * Specifies the type of schema generation action to be taken by the persistence provider in regards to writing
     * commands to DDL script files.
     * <p/>
     * If no value is specified, the default is "none".
     *
     * See JPA 2 section 8.2.1.9
     */
    public static final String SCHEMA_GEN_SCRIPTS_ACTION        = "javax.persistence.schema-generation.scripts.action";

    /**
     * For cases where the {@value #SCHEMA_GEN_SCRIPTS_ACTION} value indicates that schema creation commands should
     * be written to DDL script file, {@value #SCHEMA_GEN_SCRIPTS_CREATE_TARGET} specifies either a {@link java.io.Writer} configured for output of the DDL script or a string specifying the file URL for the DDL
     * script.
     *
     * @see #SCHEMA_GEN_SCRIPTS_ACTION
     */
    public static final String SCHEMA_GEN_SCRIPTS_CREATE_TARGET = "javax.persistence.schema-generation.scripts.create-target";

    /**
     * For cases where the {@value #SCHEMA_GEN_SCRIPTS_ACTION} value indicates that schema drop commands should
     * be written to DDL script file, {@value #SCHEMA_GEN_SCRIPTS_DROP_TARGET} specifies either a {@link java.io.Writer} configured for output of the DDL script or a string specifying the file URL for the DDL
     * script.
     *
     * @see #SCHEMA_GEN_SCRIPTS_ACTION
     */
    public static final String SCHEMA_GEN_SCRIPTS_DROP_TARGET   = "javax.persistence.schema-generation.scripts.drop-target";

    /**
     * Specifies whether the persistence provider is to create the database schema(s) in addition to creating
     * database objects (tables, sequences, constraints, etc). The value of this boolean property should be set
     * to {@code true} if the persistence provider is to create schemas in the database or to generate DDL that
     * contains "CREATE SCHEMA" commands. If this property is not supplied (or is explicitly {@code false}), the
     * provider should not attempt to create database schemas.
     */
    public static final String SCHEMA_GEN_CREATE_SCHEMAS        = "javax.persistence.create-database-schemas";

    /**
     * Allows passing the specific {@link java.sql.Connection} instance to be used for performing schema generation
     * where the target is "database".
     * <p/>
     * May also be used to determine the values for {@value #SCHEMA_GEN_DB_NAME}, {@value #SCHEMA_GEN_DB_MAJOR_VERSION} and {@value #SCHEMA_GEN_DB_MINOR_VERSION}.
     */
    public static final String SCHEMA_GEN_CONNECTION            = "javax.persistence.schema-generation-connection";

    /**
     * Specifies the name of the database provider in cases where a Connection to the underlying database is
     * not available (aka, mainly in generating scripts). In such cases, a value for {@value #SCHEMA_GEN_DB_NAME} *must* be specified.
     * <p/>
     * The value of this setting is expected to match the value returned by {@link java.sql.DatabaseMetaData#getDatabaseProductName()} for the target database.
     * <p/>
     * Additionally specifying {@value #SCHEMA_GEN_DB_MAJOR_VERSION} and/or {@value #SCHEMA_GEN_DB_MINOR_VERSION} may be required to understand exactly how to generate the required schema commands.
     *
     * @see #SCHEMA_GEN_DB_MAJOR_VERSION
     * @see #SCHEMA_GEN_DB_MINOR_VERSION
     */
    public static final String SCHEMA_GEN_DB_NAME               = "javax.persistence.database-product-name";

    /**
     * Specifies the major version of the underlying database, as would be returned by {@link java.sql.DatabaseMetaData#getDatabaseMajorVersion} for the target database. This value is used to
     * help more precisely determine how to perform schema generation tasks for the underlying database in cases
     * where {@value #SCHEMA_GEN_DB_NAME} does not provide enough distinction.
     *
     * @see #SCHEMA_GEN_DB_NAME
     * @see #SCHEMA_GEN_DB_MINOR_VERSION
     */
    public static final String SCHEMA_GEN_DB_MAJOR_VERSION      = "javax.persistence.database-major-version";

    /**
     * Specifies the minor version of the underlying database, as would be returned by {@link java.sql.DatabaseMetaData#getDatabaseMinorVersion} for the target database. This value is used to
     * help more precisely determine how to perform schema generation tasks for the underlying database in cases
     * where te combination of {@value #SCHEMA_GEN_DB_NAME} and {@value #SCHEMA_GEN_DB_MAJOR_VERSION} does not provide
     * enough distinction.
     *
     * @see #SCHEMA_GEN_DB_NAME
     * @see #SCHEMA_GEN_DB_MAJOR_VERSION
     */
    public static final String SCHEMA_GEN_DB_MINOR_VERSION      = "javax.persistence.database-minor-version";

    /**
     * Specifies a {@link java.io.Reader} configured for reading of the SQL load script or a string designating the
     * file {@link java.net.URL} for the SQL load script.
     * <p/>
     * A "SQL load script" is a script that performs some database initialization (INSERT, etc).
     */
    public static final String SCHEMA_GEN_LOAD_SCRIPT_SOURCE    = "javax.persistence.sql-load-script-source";

    /** Private default constructor: cannot be instantiated */
    private JPASettings() {
    }

}
