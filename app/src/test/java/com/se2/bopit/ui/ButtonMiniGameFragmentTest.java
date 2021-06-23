package com.se2.bopit.ui;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.gamemodel.ButtonMiniGameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.responsemodel.ButtonModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import info.hoang8f.widget.FButton;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
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


    @Test
    public void applyButtonModel() {
        FButton buttonMock = mock(FButton.class);
        VectorDrawableCompat buttonDrawable = mock(VectorDrawableCompat.class);
        doReturn(buttonDrawable).when(buttonMock).getBackground();

        ButtonModel model = miniGame.gameModel.getResponses().get(0);

        miniGame.applyButtonModel(model, buttonMock);

        verify(buttonDrawable).setTint(anyInt());
        verify(buttonMock).setOnClickListener(any());
        verify(buttonMock).setText(any());
    }

    @Test
    public void setButtonColor() {
        FButton buttonMock = mock(FButton.class);
        VectorDrawableCompat buttonDrawable = mock(VectorDrawableCompat.class);
        doReturn(buttonDrawable).when(buttonMock).getBackground();

        for (ButtonModel model : miniGame.gameModel.getResponses()) {
            miniGame.setButtonColor(model, buttonMock);
        }

        verify(buttonDrawable, times(miniGame.gameModel.getResponses().size())).setTint(anyInt());
    }

    @Test
    public void setGameListener() {
        GameListener listener = r -> {
        };
        miniGame.setGameListener(listener);
        assertSame(listener, miniGame.gameModel.getGameListener());
    }
}