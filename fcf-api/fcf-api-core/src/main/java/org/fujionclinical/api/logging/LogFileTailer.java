/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This Source Code Form is also subject to the terms of the Health-Related
 * Additional Disclaimer of Warranty and Limitation of Liability available at
 *
 *      http://www.fujionclinical.org/licensing/disclaimer
 *
 * #L%
 */
package org.fujionclinical.api.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.common.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * A file tailer is designed to monitor a file (i.e. log file) and send notifications when new lines
 * are added to the file. This class has a notification strategy similar to a SAX parser: implement
 * the <code>FileTailerListener</code> interface, create a <code>FileTailer</code> to tail your
 * file, add yourself as a listener, and start the <code>FileTailer</code>.
 * <p>
 * It is somewhat equivalent to the unix command <code>tail -f fileToTail</code>
 * <p>
 * It is your job to interpret the results, build meaningful sets of data, etc. This tailer simply
 * fires notifications containing new file lines, one at a time.
 * 
 */
public class LogFileTailer implements Runnable {
    
    
    private static final Log log = LogFactory.getLog(LogFileTailer.class);
    
    /**
     * How frequently to check for file changes; defaults to 5 seconds
     */
    private long interval = 5000;
    
    /**
     * Maximum duration <code>this</code> can run. Default set to 120 seconds <i>Note: 1000=1
     * second</i>
     */
    private long maxActiveInterval = 120000;
    
    /**
     * The file to tail
     */
    private File file;
    
    /**
     * Defines whether the file tailer should include the entire contents of the existing file or
     * tail from the end of the file when the tailer starts
     */
    private boolean startAtBeginning;
    
    /**
     * Is the tailer currently tailing? TODO: If the variable were not declared volatile (and
     * without other synchronization), then it would be legal for the thread running the loop to
     * cache the value of the variable at the start of the loop and never read it again. If you
     * don't like infinite loops, this is undesirable.
     */
    private volatile boolean tailing;
    
    /**
     * Set of listeners
     */
    private final Set<LogFileTailerListener> listeners = new HashSet<>();
    
    /**
     * Creates a new file tailer that tails an existing file and checks the file for updates every
     * 5000ms
     * 
     * @param file File
     */
    public LogFileTailer(File file) {
        this.file = file;
    }
    
    /**
     * Creates a new file tailer
     * 
     * @param file The file to tail
     * @param interval How often to check for updates to the file (default = 5000ms)
     * @param startAtBeginning Should the tailer simply tail or should it process the entire file
     *            and continue tailing (true) or simply start tailing from the end of the file
     * @throws FileNotFoundException When no file found
     */
    public LogFileTailer(File file, long interval, boolean startAtBeginning) throws FileNotFoundException {
        if (file == null) {
            throw new NullPointerException("File argument cannot be null");
        } else if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + " does not exist");
        }
        this.startAtBeginning = startAtBeginning;
        this.interval = interval;
        this.file = file;
    }
    
    /**
     * Adds a FileTailerListener to the list of listeners
     * 
     * @param l FileTailerListener
     */
    public void addFileTailerListener(LogFileTailerListener l) {
        this.listeners.add(l);
    }
    
    /**
     * Removes a FileTailerListener from the list of listeners
     * 
     * @param l FileTailListener
     */
    public void removeFileTailerListener(LogFileTailerListener l) {
        this.listeners.remove(l);
    }
    
    /**
     * @param line Data read from the <code>file</code>
     */
    protected void fireNewFileLine(String line) {
        for (LogFileTailerListener fileTailerListener : this.listeners) {
            fileTailerListener.newFileLine(line);
        }
    }
    
    protected void fireMaxActiveIntervalExceeded() {
        for (LogFileTailerListener fileTailerListener : this.listeners) {
            fileTailerListener.tailerTerminated();
        }
    }
    
    /**
     * Stops <code>this</code> from tailing a file
     */
    public void stopTailing() {
        this.tailing = false;
    }
    
    /**
     * Attempts to change the file that is being tailed by <code>this</code> Note that if
     * <code>this</code> is currently tailing then an exception is thrown
     * 
     * @param file File to change
     * @throws NullPointerException If the <code>file</code> argument is <code>null</code>
     * @throws IllegalStateException Thrown when <code>this</code> is in a running state (i.e.
     *             tailing)
     * @throws FileNotFoundException Thrown when <code>file</code> argument does not exist
     */
    public void changeFile(File file) throws FileNotFoundException {
        Assert.state(!isTailing(), "Cannot change FileTailer.file while current instance is tailing");
        Assert.notNull(file, "changeFile (File argument) cannot be null");

        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file.getAbsolutePath());
        }

        this.file = file;
    }
    
    /**
     * Returns whether <code>this</code> is currently tailing a file
     * 
     * @return boolean
     */
    public boolean isTailing() {
        return this.tailing;
    }
    
    /**
     * Typically executed via a <code>new Thread(FileTailer).start()</code>
     */
    @Override
    public void run() {
        // The file pointer keeps track of where we are in the file
        long filePointer;
        long startTime = new Date().getTime();
        
        // Determine start point
        if (this.startAtBeginning) {
            filePointer = 0;
        } else {
            filePointer = this.file.length();
        }
        
        try {
            // Start tailing
            this.tailing = true;
            RandomAccessFile file = new RandomAccessFile(this.file, "r");
            while (isTailing()) {
                //check to see if maxActiveInterval has been exceeded
                if (new Date().getTime() - startTime > this.maxActiveInterval) {
                    if (log.isWarnEnabled()) {
                        log.warn("FileTailer exceeded maxActiveInterval: " + this.maxActiveInterval);
                    }
                    stopTailing();
                    fireMaxActiveIntervalExceeded();
                }
                try {
                    // Compare the length of the file to the file pointer
                    long fileLength = this.file.length();
                    if (fileLength < filePointer) {
                        // file must have been rotated or deleted;
                        // reopen the file and reset the file pointer
                        file = new RandomAccessFile(this.file, "r");
                        filePointer = 0;
                    }
                    
                    if (fileLength > filePointer) {
                        // There is data to read
                        file.seek(filePointer);
                        String line = file.readLine();
                        while (line != null) {
                            fireNewFileLine(line);
                            line = file.readLine();
                        }
                        filePointer = file.getFilePointer();
                    }
                    
                    // Sleep for the specified interval
                    Thread.sleep(this.interval);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            
            // Close the file that we are tailing
            file.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * Auto generated method comment
     * 
     * @return long max active interval
     */
    public long getMaxActiveInterval() {
        return this.maxActiveInterval;
    }
    
    /**
     * Set the maximum duration <code>this</code> can run. In seconds.
     * 
     * @param maxActiveInterval amount of time tailer is running (in seconds)
     */
    public void setMaxActiveInterval(long maxActiveInterval) {
        this.maxActiveInterval = maxActiveInterval;
    }
    
}
