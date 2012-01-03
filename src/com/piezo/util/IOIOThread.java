package com.piezo.util;

import ioio.lib.api.IOIO;
import ioio.lib.api.IOIOFactory;
import ioio.lib.api.exception.ConnectionLostException;
import android.util.Log;

public abstract class IOIOThread extends Thread {
	/** Subclasses should use this field for controlling the IOIO. */
	protected IOIO ioio_;
	private boolean abort_ = false;

	/** Not relevant to subclasses. */
	@Override
	public final void run() {
		super.run();
		while (true) {
			try {
//				System.out.println("in ioio thread");
				synchronized (this) {
					if (abort_) {
						break;
					}
					ioio_ = IOIOFactory.create();
				}
//				System.out.println("before waiting thread");
				ioio_.waitForConnect();
				setup();
				while (true) {
					loop();
				}
			} catch (ConnectionLostException e) {
//				System.out.println("in the connection lot");
				if (abort_) {
//					System.out.println("abort exception");
					break;
				}
			} catch (Exception e) {
				Log.e("AbstractIOIOActivity",
						"Unexpected exception caught", e);
				ioio_.disconnect();
				break;
			} finally {
				try {
//					System.out.println("in the final");
					ioio_.waitForDisconnect();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * Subclasses should override this method for performing operations to
	 * be done once as soon as IOIO communication is established. Typically,
	 * this will include opening pins and modules using the openXXX()
	 * methods of the {@link #ioio_} field.
	 */
	protected void setup() throws ConnectionLostException {
	}

	/**
	 * Subclasses should override this method for performing operations to
	 * be done repetitively as long as IOIO communication persists.
	 * Typically, this will be the main logic of the application, processing
	 * inputs and producing outputs.
	 */
	protected void loop() throws ConnectionLostException {
	}

	/** Not relevant to subclasses. */
	public synchronized final void abort() {
		abort_ = true;
		if (ioio_ != null) {
			ioio_.disconnect();
		}
	}
}
