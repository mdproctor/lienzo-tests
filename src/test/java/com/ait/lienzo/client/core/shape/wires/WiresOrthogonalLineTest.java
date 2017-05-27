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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.types.PathPartEntryJSO;
import com.ait.lienzo.client.core.types.PathPartList;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.Direction;
import com.ait.lienzo.test.LienzoMockitoTestRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import static com.ait.lienzo.shared.core.types.Direction.*;

@RunWith(LienzoMockitoTestRunner.class)
public class WiresOrthogonalLineTest
{

    @Mock
    private Layer layer;

    @Before
    public void setup()
    {
        layer = new Layer();
    }

    @Test
    public void testEastToWest2Points()
    {
        /*      H<-+
         *         |
         *         +-->T
         */
        LineAsserter.given(EAST, WEST,
                           100, 100,
                           200, 200)
                    .expect(120, 100,
                            150, 100,
                            150, 200,
                            180, 200);


        /*         +-->T
         *         |
         *      H<-+
         */
        LineAsserter.given(EAST, WEST,
                           100, 100,
                           200, 0)
                    .expect(120, 100,
                            150, 100,
                            150, 0,
                            180, 0);


        /*        H<-+
         *           |
         *     +-----+
         *     |
         *     +->T
         */
        LineAsserter.given(EAST, WEST,
                           100, 100,
                           50, 300)
                    .expect(120, 100,
                            130, 100,
                            130, 200,
                            20, 200,
                            20, 300,
                            30, 300);

        /* it has to do this to make enough room for the offset and corrections on the head and the tail)
         *      +->T
         *      |
         *      --+
         *        |
         *      H-+
         */
        LineAsserter.given(EAST, WEST,
                           50, 300,
                           100, 100)
                    .expect(70, 300,
                            80, 300,
                            80, 200,
                            70, 200,
                            70, 100,
                            80, 100);
    }

    @Test
    public void testWestToEast2Points()
    {
        /*      T<-+
         *         |
         *         +-->H
         */
        LineAsserter.given(WEST, EAST,
                           200, 200,
                           100, 100)
                    .expect(180, 200,
                            150, 200,
                            150, 100,
                            120, 100);


        /*          T<-+
         *             |
         *     +-------+
         *     |
         *     +->H
         */
        LineAsserter.given(WEST, EAST,
                           50, 300,
                           100, 100)
                    .expect(30, 300,
                            20, 300,
                            20, 200,
                            130, 200,
                            130, 100,
                            120, 100);
    }

    @Test
    public void testNorthToSouth2Points()
    {
        /*       +- +
         *       |  |
         *       v  |
         *       H  |
         *          | T
         *          | ^
         *          | |
         *          --+
         */
        LineAsserter.given(NORTH, SOUTH,
                           100, 100,
                           200, 200)
                    .expect(100, 80,
                            100, 70,
                            150, 70,
                            150, 230,
                            200, 230,
                            200, 220);
    }

    @Test
    public void testNoneToNone2Points()
    {
        /*       +--->T
         *       |
         *       v
         *       H
         */
        LineAsserter.given(NONE, NONE,
                           100, 200,
                           200, 100)
                    .expect(100, 180,
                            100, 100,
                            180, 100);

        /*       +--->H
         *       |
         *       v
         *       T
         */
        LineAsserter.given(NONE, NONE,
                           200, 100,
                           100, 200)
                    .expect(180, 100,
                            100, 100,
                            100, 180);

        /*      H<---+
         *           |
         *           v
         *           T
         */
        LineAsserter.given(NONE, NONE,
                           100, 100,
                           200, 200)
                    .expect(120, 100,
                            200, 100,
                            200, 180);

        /*      t<---+
         *           |
         *           v
         *           H
         */
        LineAsserter.given(NONE, NONE,
                           200, 200,
                           100, 100)
                    .expect(200, 180,
                            200, 100,
                            120, 100);

        /*          T
         *          ^
         *          |
         *          +-+
         *            |
         *            v
         *            H
         */
        LineAsserter.given(NONE, NONE,
                           200, 200,
                           190, 100)
                    .expect(200, 180,
                            200, 150,
                            190, 150,
                            190, 120);

        /*          H
         *          ^
         *          |
         *          +-+
         *            |
         *            v
         *            T
         */
        LineAsserter.given(NONE, NONE,
                           190, 100,
                           200, 200)
                    .expect(190, 120,
                            190, 150,
                            200, 150,
                            200, 180);

        /*
         *         T<-+
         *            |
         *            +->H
         */
        LineAsserter.given(NONE, NONE,
                           200, 200,
                           100,190)
                    .expect(180, 200,
                            150, 200,
                            150, 190,
                            120, 190);


    }



    public static class LineAsserter
    {
        private double[] actualPoints;

        public static LineAsserter given(Direction headDirection, Direction tailDirection, double... givenPoints)
        {
            return new LineAsserter().line(headDirection, tailDirection, givenPoints);
        }

        public LineAsserter line(Direction headDirection, Direction tailDirection, double... points)
        {
            points = rotate(points);
            headDirection = rotate(headDirection);
            tailDirection = rotate(tailDirection);
            OrthogonalPolyLine line = createLine(points);
            line.setCorrectionOffset(10);
            line.setHeadOffset(20);
            line.setTailOffset(20);
            line.setCornerRadius(0);
            line.setHeadDirection(headDirection);
            line.setTailDirection(tailDirection);

            line.parse(line.getAttributes());
            PathPartList plist = line.getPathPartList();
            System.out.println( WiresOrthogonalLineTest.toString(plist));

            assertEquals(PathPartEntryJSO.MOVETO_ABSOLUTE, plist.get(0).getCommand());
            List<Double> doubleList = new ArrayList<Double> ();

            for (int j = 0; j < plist.get(0).getPoints().size(); j++)
            {
                doubleList.add( plist.get(0).getPoints().get(j) );
            }

            for (int i = 1; i < plist.size(); i++)
            {
                assertEquals( PathPartEntryJSO.LINETO_ABSOLUTE, plist.get(i).getCommand() );
                for (int j = 0; j < plist.get(i).getPoints().size(); j++)
                {
                    doubleList.add( plist.get(i).getPoints().get(j) );
                }
            }

            double[] doubleArray = new double[doubleList.size()];
            for (int i = 0; i < doubleList.size(); i++)
            {
                doubleArray[i] = doubleList.get(i);
            }

            actualPoints = doubleArray;
            return this;
        }

        public void expect(double... expectedPoints)
        {
            System.out.println( WiresOrthogonalLineTest.toString(expectedPoints) );
            System.out.println( WiresOrthogonalLineTest.toString(actualPoints) );
            assertArrayEquals( expectedPoints, actualPoints, 0 );
        }

        private final OrthogonalPolyLine createLine(final double... points)
        {
            return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setDraggable(false);
        }
        
    }

    public static String toString(double... points)
    {
        StringBuilder sbuilder = new StringBuilder();
        for ( int i=0; i < points.length; i++)
        {
            sbuilder.append( (int) points[i] );
            if ( i < points.length -1 )
            {
                sbuilder.append( ", " );
            }
        }
        return sbuilder.toString();
    }

    public static String toString(PathPartList list)
    {
        StringBuilder sbuilder = new StringBuilder();
        for ( int i = 0; i < list.size(); i++)
        {
            PathPartEntryJSO entry = list.get(i);
            int c = entry.getCommand();
            switch( c )
            {
                case 0: sbuilder.append("UNDEFINED_PATH_PART"); break;
                case 1: sbuilder.append("L"); break;
                case 2: sbuilder.append("M"); break;
                case 3: sbuilder.append("BEZIER_CURVETO_ABSOLUTE"); break;
                case 4: sbuilder.append("QUADRATIC_CURVETO_ABSOLUTE"); break;
                case 5: sbuilder.append("ARCTO_ABSOLUTE"); break;
                case 6: sbuilder.append("CLOSE_PATH_PART"); break;
                case 7: sbuilder.append("CANVAS_ARCTO_ABSOLUTE"); break;
            }

            sbuilder.append(" ");
            for ( int j = 0; j < entry.getPoints().size(); j++ )
            {
                double p = entry.getPoints().get(j);
                sbuilder.append(p);
                if ( j <  entry.getPoints().size() - 1)
                {
                    sbuilder.append(", ");
                }
            }
            if ( i <  list.size() - 1)
            {
                sbuilder.append("\n");
            }
        }

        return sbuilder.toString();
    }
}