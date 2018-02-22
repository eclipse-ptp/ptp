// Copyright (c) 2013 IBM Corporation and others. All rights reserved. 
// This program and the accompanying materials are made available under the 
// terms of the Eclipse Public License v1.0s which accompanies this distribution, 
// and is available at http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.ptp.rm.ibm.lsf.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ptp.rm.ibm.lsf.ui.widgets.Messages;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteProcess;
import org.eclipse.remote.core.IRemoteProcessBuilder;
import org.eclipse.remote.core.IRemoteProcessService;

/**
 * This class implements invocation of LSF commands as a job running on a non-UI
 * thread with the ability for the user to cancel the job.
 */
public class LSFCommand implements IRunnableWithProgress {

	/**
	 * Read data from remote stdout or stderr I/O stream and build an array
	 * of strings, one per line of output. The original code seemed to block
	 * in BufferedReader.readLine(), so read data directly from the InputStream
	 */
	protected class RemoteStreamReader {
		private static final float REALLOC_FACTOR = 1.5f;
		private static final int INITIAL_ALLOCATION = 100000;
		private InputStream remoteStream;
		private byte data[];

		/**
		 * Create the stream reader
		 * 
		 * @param stream
		 *            Input stream to read the data from
		 */
		public RemoteStreamReader(InputStream stream) {
			remoteStream = stream;
			data = new byte[INITIAL_ALLOCATION];
		}

		/**
		 * Read data from input stream and return an array of strings, one per
		 * line of text
		 */
		public ArrayList<String> getData(IProgressMonitor monitor) {
			int bytesReady;
			int currentOffset = 0;
			try {
				for (;;) {
					if (monitor.isCanceled()) {
						break;
					}
					/*
					 * Read as many bytes of data as are currently available. In order to detect EOF for the
					 * stream, at least 1 byte of data must be read, which will result in a blocking read
					 * if no data is available.
					 */
					bytesReady = Math.max(1, remoteStream.available());
					if ((currentOffset + bytesReady) >= data.length) {
						/*
						 * Reallocate the data array at least large enough to hold data from current read
						 */
						data = Arrays.copyOf(data, Math.max((int) (data.length * REALLOC_FACTOR), currentOffset + bytesReady));
					}
					int byteCount = remoteStream.read(data, currentOffset, bytesReady);
					if (byteCount < 0) {
						break;
					}
					currentOffset = currentOffset + byteCount;

				}
			} catch (IOException e) {
				// Do nothing
			}
			/*
			 * Process the stdout data array, converting the array into a set of newline-delimited
			 * lines of text.
			 */
			ArrayList<String> streamOutput = new ArrayList<String>();
			currentOffset = 0;
			while ((currentOffset < data.length) && (data[currentOffset] != 0)) {
				int nlIndex = currentOffset;
				while ((nlIndex < data.length) && (data[nlIndex] != '\n') && (data[nlIndex] != 0)) {
					nlIndex = nlIndex + 1;
				}
				String s = new String(data, currentOffset, nlIndex - currentOffset);
				streamOutput.add(s);
				currentOffset = nlIndex + 1;
			}
			return streamOutput;
		}
	}

	/**
	 * Internal class to read the stdout stream from the requested command. The reader runs on
	 * an independent thread in order to avoid blocking command execution because output buffers are full
	 */
	protected class StdoutReader extends Thread {
		protected IRemoteProcess targetProcess;
		protected IProgressMonitor monitor;
		protected String columnLabels[];
		protected Vector<String[]> commandResponse;
		protected IStatus resultStatus;
		protected RemoteStreamReader reader;
		protected InputStream stdoutStream;

		/**
		 * Constructor for StdoutReader class
		 * 
		 * @param proc
		 *            The remote process running the command
		 * @param m
		 *            Progress monitor used to track command completion
		 */
		public StdoutReader(IRemoteProcess proc, IProgressMonitor m) {
			setName("LSF stdout reader"); //$NON-NLS-1$
			targetProcess = proc;
			monitor = m;
			resultStatus = new Status(IStatus.OK, Activator.PLUGIN_ID, OK, Messages.OkMessage, null);
		}

		@Override
		public void run() {
			String columnData[];
			boolean headerLine;
			/*
			 * Read stdout and tokenize each line of data into an array of
			 * blank-delimited strings. The first line of output is the column
			 * headings. Subsequent lines are command response.
			 */
			stdoutStream = targetProcess.getInputStream();
			reader = new RemoteStreamReader(stdoutStream);
			ArrayList<String> stdoutData = reader.getData(monitor);
			if (monitor.isCanceled()) {
				resultStatus = new Status(IStatus.CANCEL, Activator.PLUGIN_ID, CANCELED, Messages.CommandCancelMessage,
						null);
				return;
			}
			headerLine = true;
			/*
			 * Create an array of lines of stdout output, parsing output data into one or more blank delimited
			 * columns per line. Also look for error messages that may appear in stdout text.
			 */
			commandResponse = new Vector<String[]>();
			for (String data : stdoutData) {
				if (headerLine) {
					if (data.equals("No reservation found")) { //$NON-NLS-1$
						resultStatus = new Status(IStatus.INFO, Activator.PLUGIN_ID, NO_DATA, Messages.NoReservationMessage,
								null);
					} else {
						columnLabels = data.split(" +"); //$NON-NLS-1$
						headerLine = false;
					}
				} else {
					data = data.replaceAll(" +/ +", "/"); //$NON-NLS-1$ //$NON-NLS-2$
					columnData = data.split(" +"); //$NON-NLS-1$
					commandResponse.add(columnData);
				}
				if (monitor.isCanceled()) {
					resultStatus = new Status(IStatus.CANCEL, Activator.PLUGIN_ID, CANCELED, Messages.CommandCancelMessage,
							null);
					break;
				}
			}
		}

		/**
		 * Close the stdout stream
		 */
		public void closeReader() {
			if (stdoutStream != null) {
				try {
					stdoutStream.close();
				} catch (IOException e) {
					// Do nothing
				}
			}
		}

		/**
		 * Get completion status for reading stdout
		 * 
		 * @return stdout reader completion status
		 */
		public IStatus getResultStatus() {
			return resultStatus;
		}

		/**
		 * Get column headings obtained from command output
		 * 
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
		 * 
		 * @return command output
		 */
		public Vector<String[]> getCommandResponse() {
			if (commandResponse == null) {
				commandResponse = new Vector<String[]>();
			}
			return commandResponse;
		}
	}

	/**
	 * Internal class to read the stderr stream from the requested command. The reader runs on
	 * an independent thread in order to avoid blocking command execution because output buffers are full
	 */
	private class StderrReader extends Thread {
		private IRemoteProcess targetProcess;
		private IProgressMonitor monitor;
		private IStatus resultStatus;
		private RemoteStreamReader reader;
		private InputStream stderrStream;
		private ArrayList<String> stderrData;

		/**
		 * Constructor for StderrReader class
		 * 
		 * @param proc
		 *            The remote process running the command
		 * @param m
		 *            Progress monitor used to track command completion
		 */
		public StderrReader(IRemoteProcess proc, IProgressMonitor m) {
			setName("LSF stderr reader"); //$NON-NLS-1$
			targetProcess = proc;
			monitor = m;
			resultStatus = new Status(IStatus.OK, Activator.PLUGIN_ID, OK, Messages.OkMessage, null);
		}

		@Override
		public void run() {
			boolean headerLine;
			/*
			 * Get the text written to the stderr stream
			 */
			stderrStream = targetProcess.getErrorStream();
			reader = new RemoteStreamReader(stderrStream);
			stderrData = reader.getData(monitor);
			if (monitor.isCanceled()) {
				resultStatus = new Status(IStatus.CANCEL, Activator.PLUGIN_ID, CANCELED, Messages.CommandCancelMessage,
						null);
			} else {
				/*
				 * Examine output looking for specific LSF error messages, and return a status object if that text
				 * is found
				 */
				headerLine = true;
				for (String data : stderrData) {
					if (headerLine) {
						if (data.equals("No application profiles found.")) { //$NON-NLS-1$
							resultStatus = new Status(IStatus.INFO,
									Activator.PLUGIN_ID, NO_DATA,
									Messages.NoProfileMessage, null);
						}
						headerLine = false;
					}
				}
			}
		}

		/**
		 * Get completion status for reading stderr
		 * 
		 * @return stderr reader completion status
		 */
		public IStatus getResultStatus() {
			return resultStatus;
		}

		/**
		 * Get the text written to the stderr stream as a single string containing
		 * one or more lines of text.
		 * 
		 * @return Stderr output text
		 */
		public String getErrorText() {
			StringBuilder errorText = new StringBuilder();
			for (String s : stderrData) {
				errorText.append(s);
				errorText.append("\n"); //$NON-NLS-1$
			}
			return errorText.toString();
		}

		/**
		 * Close the stderr I/O stream
		 */
		public void closeReader() {
			if (stderrStream != null) {
				try {
					stderrStream.close();
				} catch (IOException e) {
					// Do nothing
				}
			}
		}
	}

	private static final int THREAD_TIMEOUT = 10000;
	private final String command[];
	private final String commandDescription;
	private final IRemoteConnection remoteConnection;
	protected Vector<String[]> commandResponse;
	protected String columnLabels[];
	public static final int OK = 0;
	public static final int COMMAND_ERROR = 3;
	public static final int CANCELED = 2;
	public static final int NO_DATA = 3;
	protected IRemoteProcess process;
	protected IStatus runStatus;
	private StdoutReader stdoutReader;
	private StderrReader stderrReader;

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
	public LSFCommand(String name, IRemoteConnection connection, String cmd[]) {
		commandDescription = name;
		remoteConnection = connection;
		command = cmd;
		commandResponse = new Vector<String[]>();
	}

	/**
	 * Run the LSF command specified when this object was created.
	 * 
	 * @param monitor
	 *            - Progress monitor used to monitor and control job execution
	 */
	@Override
	public void run(IProgressMonitor monitor) {
		monitor.beginTask(commandDescription, IProgressMonitor.UNKNOWN);
		runStatus = runCommand(monitor);
		monitor.done();
	}

	/**
	 * Run the requested LSF command and get command output
	 * 
	 * @param monitor
	 *            Progress monitor used to track command completion
	 */
	protected IStatus runCommand(IProgressMonitor monitor) {
		IRemoteProcessService processService = remoteConnection.getService(IRemoteProcessService.class);
		IRemoteProcessBuilder processBuilder = processService.getProcessBuilder(command);
		process = null;
		try {
			process = processBuilder.start();
			/*
			 * Create threads to read command's stdout and stderr asynchronously
			 */
			stdoutReader = createStdoutReader(process, monitor);
			stdoutReader.setDaemon(true);
			stdoutReader.start();
			stderrReader = new StderrReader(process, monitor);
			stderrReader.setDaemon(true);
			/*
			 * Start process and wait for completion
			 */
			stderrReader.start();
			for (;;) {
				if (process.isCompleted()) {
					break;
				}
				if (monitor.isCanceled()) {
					process.destroy();
					return new Status(IStatus.CANCEL, Activator.PLUGIN_ID, CANCELED, Messages.CommandCancelMessage, null);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// Do nothing, sleep just ends early
				}
			}
			stdoutReader.closeReader();
			stderrReader.closeReader();
			int exitStatus = process.exitValue();
			/*
			 * Wait for stdout and stderr reader threads only if the process has completed. These threads should always
			 * exit, but don't wait here if process has not completed to avoid possibility of locking up the
			 * command processing.
			 */
			if (process.isCompleted()) {
				long startTime = System.currentTimeMillis();
				long waitTime = THREAD_TIMEOUT;
				long currentTime = startTime;
				/*
				 * Wait for the stdout reader only if exit status is zero and wait for the stderr reader
				 * only if exit status is non-zero. Even though we wait for the stdout reader only when exit
				 * status is zero, the code may still block for up to THREAD_TIMEOUT msec since there are
				 * cases where LSF commands exit with zero status but write nothing to stdout.
				 */
				if (exitStatus == 0) {
					while ((currentTime - startTime) < THREAD_TIMEOUT) {
						try {
							stdoutReader.join(waitTime);
							break;
						} catch (InterruptedException e) {
							currentTime = System.currentTimeMillis();
							waitTime = Math.max(0, THREAD_TIMEOUT - (currentTime - startTime));
						}
					}
				} else {
					while ((currentTime - startTime) < THREAD_TIMEOUT) {
						try {
							stderrReader.join(waitTime);
							break;
						} catch (InterruptedException e) {
							currentTime = System.currentTimeMillis();
							waitTime = Math.max(0, THREAD_TIMEOUT - (currentTime - startTime));
						}
					}
				}
			}
			/*
			 * Even if process was cancelled, get output generated by command
			 */
			columnLabels = stdoutReader.getColumnLabels();
			commandResponse = stdoutReader.getCommandResponse();
			if (exitStatus == 0) {
				/*
				 * Even with zero exit status, LSF commands may have posted an error message
				 * (and no stdout output)
				 */
				IStatus stderrStatus = stderrReader.getResultStatus();
				if (stderrStatus.getSeverity() == IStatus.INFO) {
					return stderrStatus;
				}
				return stdoutReader.getResultStatus();
			} else {
				IStatus errorStatus = stderrReader.getResultStatus();
				if (errorStatus.isOK()) {
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, COMMAND_ERROR, stderrReader.getErrorText(), null);
				} else {
					return errorStatus;
				}
			}
		} catch (IOException e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, COMMAND_ERROR, Messages.LSFCommandFailed, e);
		}
	}

	/**
	 * Create the class-specific stdout stream reader. This method creates the default stdout reader.
	 * 
	 * @param process
	 *            The remote process that generated the output
	 * @param monitor
	 *            Progress monitor used to track process completion
	 * @return StdoutReader object
	 */
	protected StdoutReader createStdoutReader(IRemoteProcess process, IProgressMonitor monitor) {
		return new StdoutReader(process, monitor);
	}

	/**
	 * Get the command response
	 * 
	 * @return Command response
	 */
	public Vector<String[]> getCommandResponse() {
		return commandResponse;
	}

	/**
	 * Get the column headings. Column headings are the first line of the
	 * command response data where each column has a blank-delimited column
	 * heading
	 * 
	 * @return Column heading data
	 */
	public String[] getColumnLabels() {
		return columnLabels;
	}

	/*
	 * Get the process completion status for hte LSF command
	 * 
	 * @return Process completion status
	 */
	public IStatus getRunStatus() {
		return runStatus;
	}
}
