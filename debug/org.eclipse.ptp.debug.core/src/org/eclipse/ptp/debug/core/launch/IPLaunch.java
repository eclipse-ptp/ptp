package org.eclipse.ptp.debug.core.launch;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.ptp.core.jobs.IJobControl;
import org.eclipse.ptp.debug.core.TaskSet;
import org.eclipse.ptp.debug.core.model.IPDebugTarget;

/**
 * Extend ILaunch to support parallel debug jobs
 * 
 */
public interface IPLaunch extends ILaunch {
	/**
	 * Get the IPDebugTarget responsible for the procId.
	 * 
	 * @param procId
	 *            process we're interested in
	 * @return IPDebugTarget responsible for procId
	 */
	public IPDebugTarget getDebugTarget(int procId);

	/**
	 * Get the IPDebugTarget responsible for the processes in procs
	 * 
	 * @param tasks
	 *            TaskSet containing processes we're interested in
	 * @return IPDebugTarget responsible for processes
	 * @since 4.0
	 */
	public IPDebugTarget getDebugTarget(TaskSet tasks);

	/**
	 * Get the controller used to launch the job
	 * 
	 * @return controller used to launch the job
	 * @since 6.0
	 */
	public IJobControl getJobControl();

	/**
	 * Get the job ID associated with this launch
	 * 
	 * @return job id
	 * @since 5.0
	 */
	public String getJobId();

	/**
	 * Set the controller used to launch the job
	 * 
	 * @param control
	 * @since 6.0
	 */
	public void setJobControl(IJobControl control);

	/**
	 * Set the job ID associated with this launch
	 * 
	 * @param jobId
	 * @since 5.0
	 */
	public void setJobId(String jobId);
}
