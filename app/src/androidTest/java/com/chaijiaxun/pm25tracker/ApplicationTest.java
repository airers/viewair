package com.chaijiaxun.pm25tracker;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.test.ApplicationTestCase;
import android.test.MoreAsserts;

import org.junit.Test;

/**
 * Created by chaij on 06/05/2017.
 */

public class ApplicationTest  extends ApplicationTestCase<Application> {
    public ApplicationTest(Class<Application> applicationClass) {
        super(applicationClass);
    }

    private Application application;

    public ApplicationTest() {
        super(Application.class);
    }

    @Test
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();

    }

    @Test
    public void testCorrectVersion() throws Exception {
        PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        assertNotNull(info);
        MoreAsserts.assertMatchesRegex("\\d\\.\\d", info.versionName);
    }
}
