package com.chaijiaxun.pm25tracker;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.chaijiaxun.pm25tracker.utils.AppData;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Text;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.chaijiaxun.pm25tracker", appContext.getPackageName());
    }

//    @Rule
//    public ActivityTestRule<MainActivity> rule  = new  ActivityTestRule<>(MainActivity.class);
//
//    @Test
//    public void ensureTextViewIsPresent() throws Exception {
//        AppData.getInstance().init(InstrumentationRegistry.getTargetContext());
//
//        MainActivity activity = rule.getActivity();
//        View messageText = activity.findViewById(R.id.text_message);
//        assertThat(messageText, notNullValue());
//        assertThat(messageText, instanceOf(TextView.class));
//
//    }
}
