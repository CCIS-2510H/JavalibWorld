package javalib.utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class NullPrintStream extends PrintStream {
  static class NullOutputStream extends OutputStream {
    @Override
    public void write(int i) throws IOException {

    }

    @Override
    public void write(byte[] bytes) throws IOException {

    }

    @Override
    public void write(byte[] bytes, int i, int i1) throws IOException {

    }
  }
  private NullPrintStream() { super(new NullOutputStream()); }
  public static final NullPrintStream INSTANCE = new NullPrintStream();
}
