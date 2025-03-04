package com.kok.kokapi.common.util;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class MySQLDatabaseCleanerExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        MySQLDatabaseCleaner mySQLDatabaseCleaner = SpringExtension.getApplicationContext(context)
            .getBean(MySQLDatabaseCleaner.class);
        mySQLDatabaseCleaner.cleanUp();
    }
}
