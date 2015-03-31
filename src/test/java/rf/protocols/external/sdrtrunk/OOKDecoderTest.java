package rf.protocols.external.sdrtrunk;

import junit.framework.TestCase;

import java.util.Arrays;

/**
 * TODO: add description
 *
 * @author Eugeny.Schava
 */
public class OOKDecoderTest extends TestCase {
    public void test0() throws Exception {
        test(
                new int[] {0, 1, -1, 5, 7, -1, -2, -1, 5, 7, 6, 7, 0, -1, -5, -1, -2, -1, 1, 2, 0},
                new int[] {0, 0,  0, 1, 1,  0,  0,  0, 1, 1, 1, 1, 1,  1,  1,  1,  1,  1, 0, 0, 0});
    }

    public void test1() throws Exception {
        test(
                new int[] {5, 0, -1, -2, -1, -2, 0},
                new int[] {1, 0,  0,  0,  0,  0, 0});
    }

//    public void testHex() throws Exception {
//        String data = "39 3d 15 de c2 d3 04 33 3d 1d e7 c3 cc fa 2c 3e 22 ee c6 c4 f4 16 03 fd fb fc fe 00 02 02 01 00";
//        OOKDecoder decoder = new OOKDecoder(10);
//        boolean active = false;
//        for (int i = 0; i < data.length() - 2; i+=3) {
//            float s = Integer.parseInt(data.substring(i, i + 2), 16);
//            if (s > 128) s -= 256;
//
//            long ind = decoder.process(s);
//            if (ind > 0) {
//                active = true;
//            } else if (ind < 0) {
//                active = false;
//            }
//
//            System.out.println(s + ":\t" + active);
//        }
//    }

    private void test(int[] input, int[] output) {
        OOKDecoder decoder = new OOKDecoder(5);

        int[] expectedOutput = new int[input.length];
        int startInd = -1;

        for (int s : input) {
            long ind = decoder.process(s);
            if (ind > 0) {
                startInd = (int) ind;
            } else if (ind < 0) {
                for (int j = startInd; j < -ind; j++)
                    expectedOutput[j - 1] = 1;
            }
        }

        assertTrue(Arrays.toString(expectedOutput) + "!=" + Arrays.toString(output), Arrays.equals(expectedOutput, output));
    }
}
