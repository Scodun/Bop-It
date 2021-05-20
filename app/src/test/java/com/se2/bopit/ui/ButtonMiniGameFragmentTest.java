package com.se2.bopit.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.ButtonMiniGameModel;
import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.interfaces.GameListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ButtonMiniGameFragmentTest {

    class ButtonMiniGameMock extends ButtonMiniGameFragment {
        ButtonMiniGameMock() {
            super(ButtonMiniGameModel.createRandomGameModel(
                    Arrays.stream(ButtonColor.values())
                            .map(ButtonModel::new)
                            .collect(Collectors.toList()),
                    ButtonColor.values().length));
        }

        @Nullable
        @Override
        public Context getContext() {
            return contextMock;
        }
    }

    ButtonMiniGameFragment miniGame = new ButtonMiniGameMock();
    //ButtonMiniGameFragment spy;
    Context contextMock;
    Resources resourcesMock;

    @Before
    public void setUp() throws Exception {
        //spy = spy(miniGame);
        contextMock = mock(Context.class);
        resourcesMock = mock(Resources.class);
        doReturn(resourcesMock).when(contextMock).getResources();
        //doReturn(resourcesMock).when(spy).getResources();
        //when(spy.getResources()).thenReturn(resourcesMock);
    }

    @After
    public void tearDown() throws Exception {
        reset(resourcesMock, contextMock);
    }


    @Ignore("TBD, or consider skipping it")
    @Test
    public void onCreateView() {

        LayoutInflater inflaterMock = mock(LayoutInflater.class);
        View viewMock = mock(View.class);
        doReturn(viewMock).when(inflaterMock).inflate(anyInt(), any(), anyBoolean());

        miniGame.onCreateView(inflaterMock, null, null);
    }

    @Test
    public void applyButtonModel() {
        Button buttonMock = mock(Button.class);
        VectorDrawableCompat buttonDrawable = mock(VectorDrawableCompat.class);
        doReturn(buttonDrawable).when(buttonMock).getBackground();

        ButtonModel model = miniGame.gameModel.responses.get(0);

        miniGame.applyButtonModel(model, buttonMock);

        verify(buttonDrawable).setTint(anyInt());
        verify(buttonMock).setOnClickListener(any());
        verify(buttonMock).setText(any());
    }

    @Test
    public void setButtonColor() {
        Button buttonMock = mock(Button.class);
        VectorDrawableCompat buttonDrawable = mock(VectorDrawableCompat.class);
        doReturn(buttonDrawable).when(buttonMock).getBackground();

        for (ButtonModel model : miniGame.gameModel.responses) {
            miniGame.setButtonColor(model, buttonMock);
        }

        verify(buttonDrawable, times(miniGame.gameModel.responses.size())).setTint(anyInt());
    }

    @Test
    public void setGameListener() {
        GameListener listener = r -> {
        };
        miniGame.setGameListener(listener);
        assertSame(listener, miniGame.gameModel.getGameListener());
    }
}