package com.se2.bopit.ui;

import android.content.Context;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Lifecycle;
import com.se2.bopit.ui.providers.MiniGamesRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class CustomizeGameRulesActivityTest {

    class CustomizeGameRulesActivityMock extends CustomizeGameRulesActivity {

        @Override
        public Lifecycle getLifecycle() {
            return lifecycleMock;
        }

        @Override
        SwitchCompat createSwitchControl(SwitchCompat template) {
            return mock(SwitchCompat.class);
        }
    }

    Context contextMock;
    Lifecycle lifecycleMock;

    CustomizeGameRulesActivity activity;
    SwitchCompat switchMock;

    @Before
    public void setUp() throws Exception {
        lifecycleMock = mock(Lifecycle.class);
        contextMock = mock(Context.class);
        switchMock = mock(SwitchCompat.class);

        doReturn(contextMock)
                .when(switchMock)
                .getContext();

        activity = new CustomizeGameRulesActivityMock();
        activity.model = MiniGamesRegistry.getInstance().gameRules;
        activity.switchAvoidRepeatingTypes = switchMock;
    }

    @Test
    public void createSwitchListFromGameModel() {
        assertTrue(activity.uiModelsMap.isEmpty());

        activity.createSwitchListFromGameModel(switchMock);

        assertFalse(activity.uiModelsMap.isEmpty());
    }

    @Test
    public void revertToDefault() {
        activity.model.avoidRepeatingGameTypes = true;

        activity.revertToDefault();

        assertFalse(activity.model.avoidRepeatingGameTypes);
    }

}