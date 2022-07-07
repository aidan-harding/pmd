/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.cpp.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

class CppCharStreamTest {

    @Test
    void testContinuationUnix() throws IOException {
        CppCharStream stream = CppCharStream.newCppCharStream(new StringReader("a\\\nb"));
        assertStream(stream, "ab");
    }

    @Test
    void testContinuationWindows() throws IOException {
        CppCharStream stream = CppCharStream.newCppCharStream(new StringReader("a\\\r\nb"));
        assertStream(stream, "ab");
    }

    @Test
    void testBackup() throws IOException {
        CppCharStream stream = CppCharStream.newCppCharStream(new StringReader("a\\b\\\rc"));
        assertStream(stream, "a\\b\\\rc");
    }

    private void assertStream(CppCharStream stream, String token) throws IOException {
        char c = stream.BeginToken();
        assertEquals(token.charAt(0), c);
        for (int i = 1; i < token.length(); i++) {
            c = stream.readChar();
            assertEquals(token.charAt(i), c);
        }
        assertEquals(token, stream.GetImage());
        assertEquals(token, new String(stream.GetSuffix(token.length())));
    }
}
