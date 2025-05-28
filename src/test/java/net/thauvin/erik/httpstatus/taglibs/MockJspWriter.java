/*
 * MockJspWriter.java
 *
 * Copyright 2015-2025 Erik C. Thauvin (erik@thauvin.net)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *   Neither the name of this project nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.thauvin.erik.httpstatus.taglibs;

import jakarta.servlet.jsp.JspWriter;
import org.jetbrains.annotations.NotNull;

public class MockJspWriter extends JspWriter {
    @SuppressWarnings("PMD.AvoidStringBufferField")
    private final StringBuilder buffer;

    public MockJspWriter() {
        super(0, false); // bufferSize=0, autoFlush=false
        this.buffer = new StringBuilder();
    }

    @SuppressWarnings("unused")
    public MockJspWriter(StringBuilder buffer) {
        super(0, false);
        this.buffer = buffer != null ? buffer : new StringBuilder();
    }

    @SuppressWarnings("unused")
    public StringBuilder getBuffer() {
        return buffer;
    }

    // Get the accumulated content
    public String getContent() {
        return buffer.toString();
    }

    @Override
    public void newLine() {
        buffer.append(System.lineSeparator());
    }

    @Override
    public void print(boolean b) {
        buffer.append(b);
    }

    @Override
    public void print(char c) {
        buffer.append(c);
    }

    @Override
    public void print(int i) {
        buffer.append(i);
    }

    @Override
    public void print(long l) {
        buffer.append(l);
    }

    @Override
    public void print(float f) {
        buffer.append(f);
    }

    @Override
    public void print(double d) {
        buffer.append(d);
    }

    @Override
    public void print(char[] s) {
        buffer.append(s);
    }

    @Override
    public void print(String s) {
        if (s != null) {
            buffer.append(s);
        }
    }

    @Override
    public void print(Object obj) {
        buffer.append(obj != null ? obj.toString() : "null");
    }

    @Override
    public void println() {
        buffer.append(System.lineSeparator());
    }

    @Override
    public void println(boolean b) {
        buffer.append(b).append(System.lineSeparator());
    }

    @Override
    public void println(char c) {
        buffer.append(c).append(System.lineSeparator());
    }

    @Override
    public void println(int i) {
        buffer.append(i).append(System.lineSeparator());
    }

    @Override
    public void println(long l) {
        buffer.append(l).append(System.lineSeparator());
    }

    @Override
    public void println(float f) {
        buffer.append(f).append(System.lineSeparator());
    }

    @Override
    public void println(double d) {
        buffer.append(d).append(System.lineSeparator());
    }

    @Override
    public void println(char[] s) {
        buffer.append(s).append(System.lineSeparator());
    }

    @Override
    public void println(String s) {
        if (s != null) {
            buffer.append(s);
        }
        buffer.append(System.lineSeparator());
    }

    @Override
    public void println(Object obj) {
        buffer.append(obj != null ? obj.toString() : "null")
                .append(System.lineSeparator());
    }

    // Clear the buffer
    @Override
    public void clear() {
        buffer.setLength(0);
    }

    @Override
    public void clearBuffer() {
        buffer.setLength(0);
    }

    @Override
    public void flush() {
        // Nothing to flush for StringBuilder
    }

    @Override
    public void close() {
        // Nothing to close for StringBuilder
    }

    @Override
    public int getRemaining() {
        return Integer.MAX_VALUE; // Unlimited capacity
    }

    @Override
    public void write(int c) {
        buffer.append((char) c);
    }

    @Override
    public void write(char @NotNull [] cbuf, int off, int len) {
        if (off < 0 || len < 0 || off + len > cbuf.length) {
            throw new IndexOutOfBoundsException();
        }
        buffer.append(cbuf, off, len);
    }

    @Override
    public void write(@NotNull String str) {
        buffer.append(str);
    }

    @Override
    public void write(@NotNull String str, int off, int len) {
        buffer.append(str, off, off + len);
    }
}