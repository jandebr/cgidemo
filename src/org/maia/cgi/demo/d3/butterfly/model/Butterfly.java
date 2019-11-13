package org.maia.cgi.demo.d3.butterfly.model;

import java.util.List;

import org.maia.cgi.model.d3.CoordinateFrame;
import org.maia.cgi.model.d3.object.MultipartObject3D;

public class Butterfly extends MultipartObject3D<ButterflyWing> {

	Butterfly(List<ButterflyWing> wings) {
		super(wings);
	}

	public boolean overlaps(Butterfly other) {
		if (getBoundingBox(CoordinateFrame.WORLD, null).overlaps(other.getBoundingBox(CoordinateFrame.WORLD, null))) {
			for (ButterflyWing myWing : getParts()) {
				for (ButterflyWing otherWing : other.getParts()) {
					if (myWing.overlaps(otherWing))
						return true;
				}
			}
		}
		return false;
	}

}
