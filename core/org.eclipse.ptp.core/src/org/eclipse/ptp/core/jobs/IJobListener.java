package org.eclipse.ptp.core.jobs;

/**
 * Interface for listening to events on jobs. Currently only two types of events are handled: jobs added via a job submission and
 * job status changes.
 * 
 * @since 6.0
 */
public interface IJobListener {
	/**
	 * Handle job added notification
	 * 
	 * @param status
	 *            job status
	 * @since 6.0
	 */
	public void jobAdded(IJobStatus status);

	/**
	 * Handle job changed notification
	 * 
	 * @param status
	 *            job status
	 */
	public void jobChanged(IJobStatus status);
}
