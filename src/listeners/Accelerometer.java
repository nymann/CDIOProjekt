package listeners;

import de.yadrone.base.navdata.AcceleroListener;
import de.yadrone.base.navdata.AcceleroPhysData;
import de.yadrone.base.navdata.AcceleroRawData;
import gui.FullGUI;

/**
 * @author sAkkermans
 */
public class Accelerometer implements AcceleroListener {

	public AcceleroPhysData acchysd;
	public AcceleroRawData accrawd;

	private FullGUI gui;
	private boolean calibrating = false;
	private int calibrationCountRaw = 0;
	private int calibrationCountPhys = 0;
	private float[] physBase = new float[]{0.0F, 0.0F, 0.0F};
	private int[] rawBase = new int[]{0, 0, 0};

	public Accelerometer(FullGUI gui) {
		this.gui = gui;
	}

	@Override
	public void receivedPhysData(AcceleroPhysData arg0) {
		if (calibrating) {
			calibrationCountPhys++;
			for (int i = 0; i < arg0.getPhysAccs().length; i++) {
				physBase[i] += arg0.getPhysAccs()[i];
			}
		} else {
			this.acchysd = arg0;
		}
	}

	@Override
	public void receivedRawData(AcceleroRawData arg1) {
		if (calibrating) {
			calibrationCountRaw++;
			for (int i = 0; i < arg1.getRawAccs().length; i++) {
				rawBase[i] += arg1.getRawAccs()[i];
			}
		} else {
			this.accrawd = arg1;
			int[] raw = new int[rawBase.length];
			for (int i = 0; i < rawBase.length; i++) {
				raw[i] = this.accrawd.getRawAccs()[i] - this.rawBase[i];
			}
			gui.setAcceleration(raw);
		}
	}

	public void calibration(boolean calibrate) {
		if (this.calibrating == calibrate) {
			return;
		}

		if (calibrate) {
			this.calibrating = true;
			this.calibrationCountRaw = 0;
			this.calibrationCountPhys = 0;
			this.physBase = new float[]{0.0F, 0.0F, 0.0F};
			this.rawBase = new int[]{0, 0, 0};
		} else {
			this.calibrating = false;
			for (int i = 0; i < 3; i++) {
				this.physBase[i] = this.physBase[i] / this.calibrationCountPhys;
				this.rawBase[i] = this.rawBase[i] / this.calibrationCountRaw;
			}
			this.calibrationCountRaw = 0;
			this.calibrationCountPhys = 0;
		}

	}

	public int[] getCalibratedRaw() throws Exception {
		if (this.calibrating) {
			throw new Exception("Accelerometer is in calibration mode");
		}
		int[] raw = new int[rawBase.length];
		for (int i = 0; i < rawBase.length; i++) {
			raw[i] = this.accrawd.getRawAccs()[i] - this.rawBase[i];
		}
		return raw;
	}

	public float[] getCalibratedPhys() throws Exception {
		if (this.calibrating) {
			throw new Exception("Accelerometer is in calibration mode");
		}
		float[] phys = new float[rawBase.length];
		for (int i = 0; i < rawBase.length; i++) {
			phys[i] = this.acchysd.getPhysAccs()[i] - this.physBase[i];
		}
		return phys;
	}
}
