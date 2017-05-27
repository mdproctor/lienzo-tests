/*
 *
 *    Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.ait.lienzo.client.core.shape.wires;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.wires.MagnetManager.Magnets;
import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.util.ScratchPad;
import com.ait.lienzo.test.LienzoMockitoTestRunner;
import com.ait.tooling.nativetools.client.collection.NFastStringMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(LienzoMockitoTestRunner.class)
public class MagnetManagerTest
{
    @Mock
    private MagnetManager.Magnets       magnets;

    @Mock
    private ScratchPad                  scratchPad;

    @Mock
    private NFastStringMap<WiresShape>  shapesColors;

    @Mock
    private NFastStringMap<WiresMagnet> magnetsColors;

    private static final String LAYER_ID = "theLayer";

    private     Layer                       layer;

    private     WiresManager                tested;

    @Before
    public void setup()
    {
        layer = spy(new Layer());
        layer.setID(LAYER_ID);
        tested = WiresManager.get(layer);
    }

    @Test
    public void testDrawMagnetsToBack()
    {
        // ScratchPad and it's context preparations
        Context2D context = mock(Context2D.class);
        when(scratchPad.getContext()).thenReturn(context);
        when(scratchPad.getWidth()).thenReturn(121);
        when(scratchPad.getHeight()).thenReturn(132);

        // Magnets preparations. We will use them to check that all magnets are drawn
        WiresMagnet magnet1 = mock(WiresMagnet.class);
        WiresMagnet magnet2 = mock(WiresMagnet.class);
        WiresMagnet magnet3 = mock(WiresMagnet.class);
        when(magnets.size()).thenReturn(3);
        when(magnets.getMagnet(0)).thenReturn(magnet1);
        when(magnets.getMagnet(1)).thenReturn(magnet2);
        when(magnets.getMagnet(2)).thenReturn(magnet3);

        // We are not able to mock static method, so let's prepare all staff used by

        // Test starts here
        MagnetManager manager = spy(new MagnetManager());
        doNothing().when(manager).drawShapeToBacking(magnets, shapesColors, context);
        doNothing().when(manager).drawMagnet(eq(magnetsColors), eq(context), any(WiresMagnet.class));
        manager.drawMagnetsToBack(magnets, shapesColors, magnetsColors, scratchPad);

        verify(magnetsColors).clear();

        verify(manager).drawMagnet(magnetsColors, context, magnet1);
        verify(manager).drawMagnet(magnetsColors, context, magnet2);
        verify(manager).drawMagnet(magnetsColors, context, magnet3);

        verify(context).getImageData(0, 0, 121, 132);
    }

    @Test
    public void testDrawMagnet()
    {
        Context2D context = mock(Context2D.class);
        WiresMagnet magnet = mock(WiresMagnet.class);
        IPrimitive primitive = mock(IPrimitive.class);
        when(magnet.getControl()).thenReturn(primitive);

        // Test starts here
        MagnetManager manager = new MagnetManager();
        manager.drawMagnet(magnetsColors, context, magnet);

        // Magnet is registered in colors map
        verify(magnetsColors).put(anyString(), eq(magnet));

        // Magnet position is used during drawing
        verify(primitive).getX();
        verify(primitive).getY();

        // Magnet is drawn
        verify(context).beginPath();
        verify(context).stroke();
    }

    @Test
    public void testMagnetRectangle()
    {
//        WiresShape wiresShape = new WiresShape(new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#00CC00")).setX(50).setY(50).setDraggable(true);
//        tested.register(wiresShape);
//        tested.getMagnetManager().createMagnets(wiresShape);
//        Magnets magnets = wiresShape.getMagnets();
//        assertEquals( 10, magnets.size() );
    }
}
