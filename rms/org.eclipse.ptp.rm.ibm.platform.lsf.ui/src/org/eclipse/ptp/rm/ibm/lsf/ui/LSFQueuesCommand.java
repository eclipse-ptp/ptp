// Copyright (c) 2013 IBM Corporation and others. All rights reserved. 
// This program and the accompanying materials are made available under the 
// terms of the Eclipse Public License v1.0s which accompanies this distribution, 
// and is available at http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.ptp.rm.ibm.lsf.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ptp.rm.ibm.lsf.ui.LSFCommand.RemoteStreamReader;
import org.eclipse.ptp.rm.ibm.lsf.ui.widgets.Messages;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteProcess;

/**
 * This class implements invocation of LSF commands as a job running on a non-UI
 * thread with the ability for the user to cancel the job.
 */
public class LSFQueuesCommand extends LSFCommand {
	/**
	 * Internal class to read the stdout stream from the requested command. The reader runs on
	 * an independent thread in order to avoid blocking command execution because output buffers are full
	 */
	protected class QueueStdoutReader extends StdoutReader {

		/**
		 * Constructor for StdoutReader class
		 * @param proc
		 *        The remote process running the command
		 * @param m
		 *        Progress monitor used to track command completion
		 */
		public QueueStdoutReader(IRemoteProcess proc, IProgressMonitor m) {
			super(proc, m);
			setName("LSF bqueues stdout reader"); //$NON-NLS-1$
		}

		@Override
		public void run() {
			RemoteStreamReader reader;
			String columnData[];
			String queueData;
			String queueName;
			boolean headerProcessed;
			boolean headerNext;
			boolean queueDataNext;
			boolean batchQueueAllowed;
			boolean interactiveQueueAllowed;
			try {
				/*
				 * Read stdout and tokenize each line of data to build an array of
				 * queue data. Output for each queue consists of several lines of
				 * text where this method extracts the queue name, queue statistics
				 * and scheduling restrictions from the text to build an entry for
				 * each queue. Queue data is also filtered to include queues that
				 * can be used by this target system configuration, for example
				 * excluding batch only queues when the target system configuration
				 * is an interactive one
				 */
				stdoutStream = targetProcess.getInputStream();
				reader = new RemoteStreamReader(stdoutStream);
				ArrayList<String> stdoutData = reader.getData(monitor);
				if (monitor.isCanceled()) {
					resultStatus = new Status(IStatus.CANCEL, Activator.PLUGIN_ID, CANCELED, Messages.CommandCancelMessage,
							null);
				} 
				commandResponse = new Vector<String[]>();
				headerProcessed = false;
				headerNext = false;
				queueDataNext = false;
				batchQueueAllowed = true;
				interactiveQueueAllowed = true;
				queueName = ""; //$NON-NLS-1$
				queueData = null;
				for (String data : stdoutData) {
					if (data.startsWith("QUEUE:")) { //$NON-NLS-1$
						// Queue name is in a line like "QUEUE: normal"
						if ((queueData != null)
								&& ((isInteractiveSession && interactiveQueueAllowed) || ((!isInteractiveSession) && batchQueueAllowed))) {
							queueData = queueData.replaceAll(" +/ +", "/"); //$NON-NLS-1$ //$NON-NLS-2$
							columnData = queueData.split(" +"); //$NON-NLS-1$
							commandResponse.add(columnData);
						}
						queueName = data.substring(7).trim();
						headerNext = false;
						queueDataNext = false;
						batchQueueAllowed = true;
						interactiveQueueAllowed = true;
					} else if (data.equals("PARAMETERS/STATISTICS")) { //$NON-NLS-1$
						headerNext = true;
					} else if (headerNext) {
						if (!headerProcessed) {
							data = "QUEUE_NAME " + data; //$NON-NLS-1$
							columnLabels = data.split(" +"); //$NON-NLS-1$
						}
						headerNext = false;
						headerProcessed = true;
						queueDataNext = true;
					} else if (queueDataNext) {
						queueData = queueName + " " + data; //$NON-NLS-1$
						queueDataNext = false;
					} else if (data.startsWith("SCHEDULING POLICIES:")) { //$NON-NLS-1$
						if (data.contains("NO_INTERACTIVE")) { //$NON-NLS-1$
							interactiveQueueAllowed = false;
						} else if (data.contains("ONLY_INTERACTIVE")) { //$NON-NLS-1$
							batchQueueAllowed = false;
						}
					}
				}
				if ((queueData != null)
						&& ((isInteractiveSession && interactiveQueueAllowed) || ((!isInteractiveSession) && batchQueueAllowed))) {
					queueData = queueData.replaceAll(" +/ +", "/"); //$NON-NLS-1$ //$NON-NLS-2$
					columnData = queueData.split(" +"); //$NON-NLS-1$
					commandResponse.add(columnData);
				}
				stdoutStream.close();
			} catch (IOException e) {
				resultStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, COMMAND_ERROR, Messages.LSFQueuesCommand_7, e);
			}
			resultStatus = new Status(IStatus.OK, Activator.PLUGIN_ID, OK, Messages.OkMessage, null);
		}

		/**
		 * Get completion status for reading stdout
		 * @return stdout reader completion status
		 */
		public IStatus getResultStatus() {
			return resultStatus;
		}
		
		/**
		 * Get column headings obtained from command output
		 * @return column headings
		 */
		public String[] getColumnLabels() {
			if (columnLabels == null) {
				columnLabels = new String[0];
			}
			return columnLabels;
		}
		
		/**
		 * Get the command output resulting from running the command
		 * @return command output
		 */
		public Vector<String[]> getCommandResponse() {
			if (commandResponse == null) {
				commandResponse = new Vector<String[]>();
			}
			return commandResponse;
		}
	}

	private final boolean isInteractiveSession;

	/**
	 * Constructor for creating a job to run a LSF command
	 * 
	 * @param name
	 *            - Name of the job
	 * @param connection
	 *            The remote connection where the job will be run
	 * @param cmd
	 *            Array containing command name and command parameters
	 */
	public LSFQueuesCommand(String name, IRemoteConnection connection, String cmd[], boolean isInteractive) {
		super(name, connection, cmd);
		isInteractiveSession = isInteractive;
	}
	
	/**
	 * Create the stdout data reader to handle bqueues command outout
	 * @param process
	 *        The remote process that generated the stdout data
	 * @param monitor
	 *        Progress monitor used to track process completion
	 * @return Stdout data reader
	 */
	protected StdoutReader createStdoutReader(IRemoteProcess process, IProgressMonitor monitor) {
		return new QueueStdoutReader(process, monitor);
	}
}
