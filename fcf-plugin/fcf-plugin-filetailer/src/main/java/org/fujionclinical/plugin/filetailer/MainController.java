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
package org.fujionclinical.plugin.filetailer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.LocalizedMessage;
import org.fujion.component.Timer;
import org.fujion.component.*;
import org.fujionclinical.api.logging.ILogManager;
import org.fujionclinical.api.logging.LogFileTailer;
import org.fujionclinical.api.logging.LogFileTailerListener;
import org.fujionclinical.shell.plugins.PluginController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Controller class for log file tailer.
 */
public class MainController extends PluginController {

    private static final Log log = LogFactory.getLog(MainController.class);

    private static final int TAIL_INTERVAL = 2000;//1000 = 1 second

    private static final int TIMER_INTERVAL = 2000;//1000 = 1 second

    private static final LocalizedMessage MSG_TOGGLE_ON = new LocalizedMessage("fcf.filetailer.btn.toggle.on.label");

    private static final LocalizedMessage MSG_TOGGLE_OFF = new LocalizedMessage("fcf.filetailer.btn.toggle.off.label");

    private static final LocalizedMessage MSG_STOPPED = new LocalizedMessage("fcf.filetailer.msg.stopped");

    private static final LocalizedMessage MSG_NOT_FOUND = new LocalizedMessage("fcf.filetailer.error.notfound");

    private static final LocalizedMessage MSG_ILLEGAL_STATE = new LocalizedMessage("fcf.filetailer.error.illegalstate");

    private static final LocalizedMessage MSG_DURATION = new LocalizedMessage("fcf.filetailer.msg.duration");

    private final Deque<String> logFileBuffer = new ArrayDeque<>();

    private final Map<String, File> logFiles = new HashMap<>();

    @WiredComponent
    private Timer timer;

    @WiredComponent
    private Memobox txtOutput;

    @WiredComponent
    private Combobox cboLogFiles;

    @WiredComponent
    private Button btnToggle;

    @WiredComponent
    private Label lblMessage;

    private LogFileTailer tailer;

    private boolean isTailerStarted;

    private boolean isTailerTerminated;

    /**
     * FileTailListener callback interface handles processing when notified that the file being
     * tailed has new lines
     */
    private final LogFileTailerListener tailListener = new LogFileTailerListener() {

        @Override
        public void newFileLine(String line) {
            logFileBuffer.add(line.concat("\n"));
        }

        @Override
        public void tailerTerminated() {
            log.trace("TailerTerminated event");
            isTailerTerminated = true;
        }
    };

    private ILogManager logManager;

    /**
     * Initializes Controller. Loads user preferences and properties. If
     * <code>{@link ILogManager} != null</code> then current logging appenders are added to member
     * Map
     */
    @Override
    public void afterInitialized(BaseComponent comp) {
        super.afterInitialized(comp);
        log.trace("Initializing Controller");

        //Populate LogFilePaths Combobox
        if (logManager != null) {
            List<String> logFilePaths = logManager.getAllPathsToLogFiles();

            for (String pathToLogFile : logFilePaths) {
                logFiles.put(pathToLogFile, new File(pathToLogFile));
                cboLogFiles.addChild(new Comboitem(pathToLogFile));
            }

            if (cboLogFiles.getChildCount() > 0) {
                cboLogFiles.setSelectedItem((Comboitem) cboLogFiles.getChildAt(0));
                onSelect$cboLogFiles();
            }
        }
    }

    /**
     * Event handler for log file tailer
     */
    @EventHandler(value = "timer", target = "@timer")
    private void onTimer$timer() {
        log.trace("onTimer event");

        StringBuilder lines = new StringBuilder();

        synchronized (logFileBuffer) {
            for (String line : logFileBuffer) {
                lines.append(line);
            }
        }

        txtOutput.setValue(txtOutput.getValue().concat(lines.toString()));
        logFileBuffer.clear();

        //check for state change of Tailer
        if (isTailerTerminated) {
            isTailerTerminated = false;
            String msg = "Tailer was terminated, stopping client timer";
            txtOutput.setValue(txtOutput.getValue().concat(msg).concat("\n"));
            log.trace(msg);
            stopTailer();
            showMessage(msg);
        }
    }

    /**
     * Handles the Button onClick event for changing the state of the
     * {@link org.fujionclinical.api.logging.LogFileTailer}
     */
    @EventHandler(value = "click", target = "@btnToggle")
    private void onClick$btnToggle() {
        if (isTailerStarted) {
            stopTailer();
        } else {
            startTailer();
        }
    }

    /**
     * Stop the tailer if not already running.
     */
    private void stopTailer() {
        if (isTailerStarted) {
            log.trace("Stopping Tailer/Timer");
            tailer.stopTailing();
            log.trace("Tailer stopped");
            timer.stop();
            log.trace("Timer stopped");
            isTailerStarted = false;
            btnToggle.setLabel(MSG_TOGGLE_ON.toString());
            showMessage(MSG_STOPPED.toString());
        }
    }

    /**
     * Start the tailer if it is running.
     */
    private void startTailer() {
        if (!isTailerStarted) {
            log.trace("Starting file tailer");
            Comboitem selectedItem = cboLogFiles.getSelectedItem();
            String logFilePath = selectedItem == null ? null : selectedItem.getLabel();

            if (tailer == null) {
                log.trace("Creating LogFileTailer with " + logFilePath);
                try {
                    tailer = new LogFileTailer(logFiles.get(logFilePath), TAIL_INTERVAL, false);
                    tailer.addFileTailerListener(tailListener);
                } catch (FileNotFoundException e) {
                    log.error(e.getMessage(), e);
                    showMessage(MSG_NOT_FOUND.toString(logFilePath));
                    return;
                }
            } else {
                try {
                    tailer.changeFile(logFiles.get(logFilePath));
                } catch (IllegalStateException ise) {
                    log.error(ise.getMessage(), ise);
                    showMessage(MSG_ILLEGAL_STATE.toString(logFilePath));
                    return;
                } catch (FileNotFoundException e) {
                    log.error(e.getMessage(), e);
                    showMessage(MSG_NOT_FOUND.toString(logFilePath));
                    return;
                }
            }
            showMessage(MSG_DURATION.toString(tailer.getMaxActiveInterval() / 1000));
            timer.setInterval(TIMER_INTERVAL);
            timer.start();
            log.trace("Timer started");
            new Thread(tailer).start();
            log.trace("Tailer started");
            btnToggle.setLabel(MSG_TOGGLE_OFF.toString());
            isTailerStarted = true;
        }
    }

    @EventHandler(value = "select", target = "@cboLogFiles")
    private void onSelect$cboLogFiles() {
        stopTailer();
        btnToggle.setDisabled(cboLogFiles.getSelectedItem() == null);
    }

    /**
     * Handles the Button onClick event to clear the log file tailer output.
     */
    @EventHandler(value = "click", target = "btnClear")
    private void onClick$btnClear() {
        log.trace("Clearing LogFileTailer output and LogFileBuffer");
        logFileBuffer.clear();
        txtOutput.setValue(null);
    }

    /**
     * Displays message to client
     *
     * @param message Message to display to client.
     */
    private void showMessage(String message) {
        lblMessage.setLabel(message);
        lblMessage.setVisible(message != null);
    }

    public void setLogManager(ILogManager logManager) {
        this.logManager = logManager;
    }

}
