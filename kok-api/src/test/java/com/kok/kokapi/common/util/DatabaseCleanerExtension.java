package com.kok.kokapi.common.util;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseCleanerExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        SpringExtension.getApplicationContext(context)
            .getBeansOfType(DatabaseCleaner.class)
            .values()
            .forEach(DatabaseCleaner::cleanUp);
    }
}
