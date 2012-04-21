package es.ld23.util;

import es.ld23.Game;
import java.util.Random;

public class Noise {

	private static class Grad {

		double x, y, z, w;

		Grad(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		Grad(double x, double y, double z, double w) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}
	}
	private static final double F3 = 1.0 / 3.0;
	private static final double G3 = 1.0 / 6.0;
	private static short perm[] = new short[512];
	private static short permMod12[] = new short[512];
	private static Grad grad3[] = {new Grad(1, 1, 0), new Grad(-1, 1, 0), new Grad(1, -1, 0), new Grad(-1, -1, 0),
		new Grad(1, 0, 1), new Grad(-1, 0, 1), new Grad(1, 0, -1), new Grad(-1, 0, -1),
		new Grad(0, 1, 1), new Grad(0, -1, 1), new Grad(0, 1, -1), new Grad(0, -1, -1)};
	private static short p[] = {81,129,164,208,15,203,64,12,87,39,71,254,14,142,105,52,123,72,115,136,65,100,174,191,73,170,226,132,64,13,50,189,236,73,169,232,66,218,242,186,222,109,28,215,27,173,210,193,246,105,111,170,177,80,221,15,50,166,59,150,4,33,212,91,82,194,196,128,237,226,39,173,47,39,136,49,111,139,35,37,86,61,150,184,81,81,144,64,9,130,72,150,71,159,240,176,68,36,30,231,141,185,84,245,169,117,248,152,52,235,250,158,184,97,92,130,236,127,1,42,163,120,66,0,29,156,12,102,104,96,165,135,142,20,20,35,166,136,55,163,179,115,149,35,188,215,147,150,240,87,221,73,105,133,214,233,210,199,150,234,245,227,214,21,59,137,145,152,139,189,222,137,253,204,91,136,229,56,177,75,64,57,148,49,243,127,109,138,80,126,170,186,111,88,32,144,22,156,88,66,146,200,7,177,164,220,113,31,83,23,245,227,178,31,89,176,162,113,229,248,196,86,22,135,253,207,106,165,16,73,32,60,198,105,186,185,238,167,223,107,85,21,235,64,164,25,72,15,159,215,80,154,6,113,57,150};

	static {
		for (int i = 0; i < 512; i++) {
			perm[i] = p[i & 255];
			permMod12[i] = (short) (perm[i] % 12);
		}
	}

	public static void randomize() {
		for (int i = 0; i < p.length; i++) {
			p[i] = (short) Game.random.nextInt(256);
		}
		for (int i = 0; i < 512; i++) {
			perm[i] = p[i & 255];
			permMod12[i] = (short) (perm[i] % 12);
		}
	}

	public static void randomize(long seed) {
		for (int i = 0; i < p.length; i++) {
			p[i] = (short) Game.random.nextInt(256);
		}
		for (int i = 0; i < 512; i++) {
			perm[i] = p[i & 255];
			permMod12[i] = (short) (perm[i] % 12);
		}
	}

	// This method is a *lot* faster than using (int)Math.floor(x)
	private static int fastfloor(double x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}

	private static double dot(Grad g, double x, double y, double z) {
		return g.x * x + g.y * y + g.z * z;
	}

	// 3D simplex noise
	public static double noise(double xin, double yin, double zin) {
		double n0, n1, n2, n3; // Noise contributions from the four corners
		// Skew the input space to determine which simplex cell we're in
		double s = (xin + yin + zin) * F3; // Very nice and simple skew factor for 3D
		int i = fastfloor(xin + s);
		int j = fastfloor(yin + s);
		int k = fastfloor(zin + s);
		double t = (i + j + k) * G3;
		double X0 = i - t; // Unskew the cell origin back to (x,y,z) space
		double Y0 = j - t;
		double Z0 = k - t;
		double x0 = xin - X0; // The x,y,z distances from the cell origin
		double y0 = yin - Y0;
		double z0 = zin - Z0;
		// For the 3D case, the simplex shape is a slightly irregular tetrahedron.
		// Determine which simplex we are in.
		int i1, j1, k1; // Offsets for second corner of simplex in (i,j,k) coords
		int i2, j2, k2; // Offsets for third corner of simplex in (i,j,k) coords
		if (x0 >= y0) {
			if (y0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} // X Y Z order
			else if (x0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} // X Z Y order
			else {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} // Z X Y order
		} else { // x0<y0
			if (y0 < z0) {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} // Z Y X order
			else if (x0 < z0) {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} // Y Z X order
			else {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} // Y X Z order
		}
		// A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
		// a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
		// a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
		// c = 1/6.
		double x1 = x0 - i1 + G3; // Offsets for second corner in (x,y,z) coords
		double y1 = y0 - j1 + G3;
		double z1 = z0 - k1 + G3;
		double x2 = x0 - i2 + 2.0 * G3; // Offsets for third corner in (x,y,z) coords
		double y2 = y0 - j2 + 2.0 * G3;
		double z2 = z0 - k2 + 2.0 * G3;
		double x3 = x0 - 1.0 + 3.0 * G3; // Offsets for last corner in (x,y,z) coords
		double y3 = y0 - 1.0 + 3.0 * G3;
		double z3 = z0 - 1.0 + 3.0 * G3;
		// Work out the hashed gradient indices of the four simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int kk = k & 255;
		int gi0 = permMod12[ii + perm[jj + perm[kk]]];
		int gi1 = permMod12[ii + i1 + perm[jj + j1 + perm[kk + k1]]];
		int gi2 = permMod12[ii + i2 + perm[jj + j2 + perm[kk + k2]]];
		int gi3 = permMod12[ii + 1 + perm[jj + 1 + perm[kk + 1]]];
		// Calculate the contribution from the four corners
		double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
		if (t0 < 0) {
			n0 = 0.0;
		} else {
			t0 *= t0;
			n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0);
		}
		double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
		if (t1 < 0) {
			n1 = 0.0;
		} else {
			t1 *= t1;
			n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1);
		}
		double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
		if (t2 < 0) {
			n2 = 0.0;
		} else {
			t2 *= t2;
			n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2);
		}
		double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
		if (t3 < 0) {
			n3 = 0.0;
		} else {
			t3 *= t3;
			n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3);
		}
		// Add contributions from each corner to get the final noise value.
		// The result is scaled to stay just inside [-1,1]
		return 32.0 * (n0 + n1 + n2 + n3);
	}
	
	
	public static double noiseNormalizado(double xin, double yin, int size) {
		double res = 0.0;
		double h = 1.0, r = 0.6;

		for (int n = 0; n < size; n++) {
			h /= r;
			res += noise(xin, h, yin);
		}
		return (res / size + 1.0) / 2.0;
	}
}
