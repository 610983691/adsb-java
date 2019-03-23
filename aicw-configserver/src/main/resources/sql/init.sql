CREATE TABLE PT_CONFIG_MAIN (
    ID          INTEGER        PRIMARY KEY AUTOINCREMENT
                               NOT NULL,
    PRODUCT_ID  INTEGER        NOT NULL,
    MODULE_ID   INTEGER        ,
    CONFIG_TYPE VARCHAR2 (128) NOT NULL,
    CREATE_UUID VARCHAR2 (64)  NOT NULL,
    EXTENDS_ID  INTEGER
);


CREATE TABLE PT_CONFIG_ITEMS (
    ID             INTEGER        PRIMARY KEY AUTOINCREMENT
                                  NOT NULL,
    CONFIG_MAIN_ID INTEGER        NOT NULL,
    ITEM_DESC      VARCHAR2 (128) NOT NULL,
    ITEM_IS_CRYPT  INTEGER        NOT NULL,
    ITEM_KEY       VARCHAR2 (128) NOT NULL,
    ITEM_VALUE     VARCHAR2 (512),
    ITEM_SEQ       INTEGER        NOT NULL,
    IS_EXTENDS     INTEGER        NOT NULL,
    EXTENDS_ID     INTEGER
);


CREATE TABLE PT_CONFIG_PRODUCT (
    ID           INTEGER        PRIMARY KEY AUTOINCREMENT
                                NOT NULL,
    PRODUCT_NAME VARCHAR2 (128) NOT NULL,
    PRODUCT_MARK VARCHAR2 (128) NOT NULL,
    CRYPT_TYPE   VARCHAR2 (128) NOT NULL,
    CRYPT_KEY    VARCHAR2 (128) NOT NULL,
    CREATE_UUID  VARCHAR2 (64)  NOT NULL
);


CREATE TABLE PT_CONFIG_MODULES (
    ID          INTEGER        PRIMARY KEY AUTOINCREMENT
                               NOT NULL,
    PRODUCT_ID  INTEGER        NOT NULL,
    MODULE_NAME VARCHAR2 (128) NOT NULL,
    MODULE_MARK VARCHAR2 (128) NOT NULL,
    IS_EXTENDS  INTEGER        NOT NULL
);


CREATE TABLE PT_CONFIG_TEMPLATE (
    ID            INTEGER        PRIMARY KEY AUTOINCREMENT
                                 NOT NULL,
    TEMPLATE_NAME VARCHAR2 (128) NOT NULL,
    TEMPLATE_DESC VARCHAR2 (128) NOT NULL,
    CONFIG_TYPE   VARCHAR2 (128) NOT NULL,
    CREATE_UUID   VARCHAR2 (64)  NOT NULL,
    IS_INNER      INTEGER        NOT NULL
);


CREATE TABLE PT_CONFIG_TEMPLATE_ITEMS (
    ID            INTEGER        PRIMARY KEY AUTOINCREMENT
                                 NOT NULL,
    TEMPLATE_ID   INTEGER        NOT NULL,
    ITEM_DESC     VARCHAR2 (128) NOT NULL,
    ITEM_IS_CRYPT INTEGER        NOT NULL,
    ITEM_KEY      VARCHAR2 (128) NOT NULL,
    ITEM_VALUE    VARCHAR2 (512),
    ITEM_SEQ      INTEGER        NOT NULL
);


CREATE TABLE PT_CONFIG_USER (
    ID            INTEGER       PRIMARY KEY AUTOINCREMENT
                                NOT NULL,
    UUID          VARCHAR2 (64) NOT NULL,
    USER_NAME     VARCHAR2 (64) NOT NULL,
    USER_PASSWORD VARCHAR2 (64) NOT NULL,
    IS_ADMIN      INTEGER       NOT NULL,
    USER_DESC VARCHAR2 (128)
);


INSERT INTO PT_CONFIG_USER (
                               IS_ADMIN,
                               USER_PASSWORD,
                               USER_NAME,
                               UUID,
                               ID
                           )
                           VALUES (
                               1,
                               '29958EA79A16077EA0AE703D012764B1', --coulee2018 md5 encrypt
                               'admincoulee',
                               'admincoulee',
                               1
                           );
