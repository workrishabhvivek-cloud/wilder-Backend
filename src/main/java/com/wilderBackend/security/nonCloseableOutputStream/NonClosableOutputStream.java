package com.wilderBackend.security.nonCloseableOutputStream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NonClosableOutputStream extends FilterOutputStream {
    public NonClosableOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void close() {
        // do not close underlying servlet stream; just flush
        try { flush(); } catch (IOException ignored) {}
    }
}
