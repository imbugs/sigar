package net.hyperic.sigar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.EOFException;
import java.io.File;

/**
 * This class provides Emacs style key bindings, history and tab completion
 * for command shell applications.
 *
 * This class is a Java interface to the getline.c package:
 * Copyright (C) 1991, 1992 by Chris Thewalt (thewalt@ce.berkeley.edu)
 */
public class Getline {

    private static boolean useNative =
        ! "false".equals(System.getProperty("sigar.getline.native"));

    private BufferedReader in = null;

    private String prompt = "> ";

    public Getline() { }

    public Getline(String prompt) {
        this.prompt = prompt;
    }

    public native static void setCompleter(GetlineCompleter completer);

    public native void redraw();

    public native void reset();

    private native void histadd(String line);

    private native void histinit(String file);

    private native String getline(String prompt)
        throws IOException, EOFException;

    public String getLine()
        throws IOException, EOFException {

        return getLine(this.prompt, true);
    }

    public String getLine(String prompt)
        throws IOException, EOFException {

        return getLine(prompt, true);
    }

    public String getLine(String prompt, boolean addToHistory)
        throws IOException, EOFException {

        if (useNative) {
            String line = getline(prompt);
            if (addToHistory) {
                addToHistory(line);
            }
            return line;
        }
        else {
            if (this.in == null) {
                this.in =
                    new BufferedReader(new InputStreamReader(System.in));
            }
            System.out.print(prompt);
            return this.in.readLine();
        }
    }

    public void initHistoryFile(File file)
        throws IOException {

        histinit(file.getCanonicalPath());
    }

    public void addToHistory(String line) {
        if ((line == null) ||
            (line.length() == 0))
        {
            return;
        }
        histadd(line);
    }
}
