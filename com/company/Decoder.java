package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Decoder {

  public static void decode(final File fileIn, final File fileOut) throws Exception {
    try (final ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileIn))) {
      final int height = in.readShort();
      final int width = in.readShort();
      byte[] bufR = new byte[height * width];
      byte[] bufG = new byte[height * width];
      byte[] bufB = new byte[height * width];


      decodeComponent(in, height, width, bufR);
      decodeComponent(in, height, width, bufG);
      decodeComponent(in, height, width, bufB);

      final BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
      unite(bufR, bufG, bufB, out);
      final String name = fileOut.getName();
      ImageIO.write(out, name.substring(name.lastIndexOf('.') + 1), fileOut);
    }
    catch (final IOException e) {
      e.printStackTrace();
      throw new Exception();
    }
    catch (final Exception e) {
      throw new Exception();
    }
  }

  private static void decodeComponent(final InputStream in, final int height, final int width, final byte[] out) throws Exception {
    try {
      decodeRLE(in, out);
      decodeDelta(out);
    }
    catch (final IOException e) {
      e.printStackTrace();
      throw new Exception();
    }
    catch (final Exception e) {
      throw new Exception();
    }
  }

  private static void unite(final byte[] bufR, final byte[] bufG, final byte[] bufB, final BufferedImage out) throws Exception {
    final int height = out.getHeight();
    final int width = out.getWidth();
    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        out.setRGB(j, i, new Color((bufR[i * width + j] + 256) % 256, (bufG[i * width + j] + 256) % 256, (bufB[i * width + j] + 256) % 256).getRGB());
      }
    }
  }

  private static void decodeRLE(final InputStream in, final byte[] out) throws Exception {
    try {
      int v;
      byte b, num;
      int index = 0;
      while ((v = in.read()) != -1) {
        num = (byte) v;
        if (num == 0) {
          break;
        }
        if (num == 1) {
          throw new Exception();
        }
        if (num > 0) {
          if ((v = in.read()) == -1) {
            throw new Exception();
          }
          b = (byte) v;
          for (int i = 0; i < num; ++i, ++index) {
            if (index >= out.length) {
              throw new Exception();
            }
            out[index] = b;
          }
        }
        else {
          for (int i = 0; i < -num; ++i, ++index) {
            if ((v = in.read()) == -1) {
              throw new Exception();
            }
            b = (byte) v;
            if (index >= out.length) {
              throw new Exception();
            }
            out[index] = b;
          }
        }
      }
      if (v == -1) {
        throw new Exception();
      }
    }
    catch (final IOException e) {
      e.printStackTrace();
      throw new Exception();
    }
  }

  private static void decodeDelta(final byte[] out) throws Exception {
    byte last = 0;
    for (int i = 0; i < out.length; ++i) {
      last += out[i];
      out[i] = last;
    }
  }
}
