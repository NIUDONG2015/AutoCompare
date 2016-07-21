package delaunay;
/**
 * 
 * @author tangcheng1
 *
 */
public class Point {

	 private double[] coordinates;          // The point's coordinates

	    /**
	     * Constructor.
	     * @param coords the coordinates
	     */
	    public Point (double... coords) {
	        // Copying is done here to ensure that Point's coords cannot be altered.
	        // This is necessary because the double... notation actually creates a
	        // constructor with double[] as its argument.
	        coordinates = new double[coords.length];
	        System.arraycopy(coords, 0, coordinates, 0, coords.length);
	    }

	    @Override
	    public String toString () {
	        if (coordinates.length == 0) return "Point()";
	        String result = "Point(" + coordinates[0];
	        for (int i = 1; i < coordinates.length; i++)
	            result = result + "," + coordinates[i];
	        result = result + ")";
	        return result;
	    }

	    @Override
	    public boolean equals (Object other) {
	        if (!(other instanceof Point)) return false;
	        Point p = (Point) other;
	        if (this.coordinates.length != p.coordinates.length) return false;
	        for (int i = 0; i < this.coordinates.length; i++)
	            if (this.coordinates[i] != p.coordinates[i]) return false;
	        return true;
	    }

	    @Override
	    public int hashCode () {
	        int hash = 0;
	        for (double c: this.coordinates) {
	            long bits = Double.doubleToLongBits(c);
	            hash = (31*hash) ^ (int)(bits ^ (bits >> 32));
	        }
	        return hash;
	    }

	    /* Points as vectors */

	    /**
	     * @return the specified coordinate of this Point
	     * @throws ArrayIndexOutOfBoundsException for bad coordinate
	     */
	    public double coord (int i) {
	        return this.coordinates[i];
	    }

	    /**
	     * @return this Point's dimension.
	     */
	    public int dimension () {
	        return coordinates.length;
	    }

	    /**
	     * Check that dimensions match.
	     * @param p the Point to check (against this Point)
	     * @return the dimension of the Points
	     * @throws IllegalArgumentException if dimension fail to match
	     */
	    public int dimCheck (Point p) {
	        int len = this.coordinates.length;
	        if (len != p.coordinates.length)
	            throw new IllegalArgumentException("Dimension mismatch");
	        return len;
	    }

	    /**
	     * Create a new Point by adding additional coordinates to this Point.
	     * @param coords the new coordinates (added on the right end)
	     * @return a new Point with the additional coordinates
	     */
	    public Point extend (double... coords) {
	        double[] result = new double[coordinates.length + coords.length];
	        System.arraycopy(coordinates, 0, result, 0, coordinates.length);
	        System.arraycopy(coords, 0, result, coordinates.length, coords.length);
	        return new Point(result);
	    }

	    /**
	     * Dot product.
	     * @param p the other Point
	     * @return dot product of this Point and p
	     */
	    public double dot (Point p) {
	        int len = dimCheck(p);
	        double sum = 0;
	        for (int i = 0; i < len; i++)
	            sum += this.coordinates[i] * p.coordinates[i];
	        return sum;
	    }

	    /**
	     * Magnitude (as a vector).
	     * @return the Euclidean length of this vector
	     */
	    public double magnitude () {
	        return Math.sqrt(this.dot(this));
	    }

	    /**
	     * Subtract.
	     * @param p the other Point
	     * @return a new Point = this - p
	     */
	    public Point subtract (Point p) {
	        int len = dimCheck(p);
	        double[] coords = new double[len];
	        for (int i = 0; i < len; i++)
	            coords[i] = this.coordinates[i] - p.coordinates[i];
	        return new Point(coords);
	    }

	    /**
	     * Add.
	     * @param p the other Point
	     * @return a new Point = this + p
	     */
	    public Point add (Point p) {
	        int len = dimCheck(p);
	        double[] coords = new double[len];
	        for (int i = 0; i < len; i++)
	            coords[i] = this.coordinates[i] + p.coordinates[i];
	        return new Point(coords);
	    }

	    /**
	     * Angle (in radians) between two Points (treated as vectors).
	     * @param p the other Point
	     * @return the angle (in radians) between the two Points
	     */
	    public double angle (Point p) {
	        return Math.acos(this.dot(p) / (this.magnitude() * p.magnitude()));
	    }

	    /**
	     * Perpendicular bisector of two Points.
	     * Works in any dimension.  The coefficients are returned as a Point of one
	     * higher dimension (e.g., (A,B,C,D) for an equation of the form
	     * Ax + By + Cz + D = 0).
	     * @param point the other point
	     * @return the coefficients of the perpendicular bisector
	     */
	    public Point bisector (Point point) {
	        dimCheck(point);
	        Point diff = this.subtract(point);
	        Point sum = this.add(point);
	        double dot = diff.dot(sum);
	        return diff.extend(-dot / 2);
	    }

	    /* Points as matrices */

	    /**
	     * Create a String for a matrix.
	     * @param matrix the matrix (an array of Points)
	     * @return a String represenation of the matrix
	     */
	    public static String toString (Point[] matrix) {
	        StringBuilder buf = new StringBuilder("{");
	        for (Point row: matrix) buf.append(" " + row);
	        buf.append(" }");
	        return buf.toString();
	    }

	    /**
	     * Compute the determinant of a matrix (array of Points).
	     * This is not an efficient implementation, but should be adequate
	     * for low dimension.
	     * @param matrix the matrix as an array of Points
	     * @return the determinnant of the input matrix
	     * @throws IllegalArgumentException if dimensions are wrong
	     */
	    public static double determinant (Point[] matrix) {
	        if (matrix.length != matrix[0].dimension())
	            throw new IllegalArgumentException("Matrix is not square");
	        boolean[] columns = new boolean[matrix.length];
	        for (int i = 0; i < matrix.length; i++) columns[i] = true;
	        try {return determinant(matrix, 0, columns);}
	        catch (ArrayIndexOutOfBoundsException e) {
	            throw new IllegalArgumentException("Matrix is wrong shape");
	        }
	    }

	    /**
	     * Compute the determinant of a submatrix specified by starting row
	     * and by "active" columns.
	     * @param matrix the matrix as an array of Points
	     * @param row the starting row
	     * @param columns a boolean array indicating the "active" columns
	     * @return the determinant of the specified submatrix
	     * @throws ArrayIndexOutOfBoundsException if dimensions are wrong
	     */
	    private static double determinant(Point[] matrix, int row, boolean[] columns){
	        if (row == matrix.length) return 1;
	        double sum = 0;
	        int sign = 1;
	        for (int col = 0; col < columns.length; col++) {
	            if (!columns[col]) continue;
	            columns[col] = false;
	            sum += sign * matrix[row].coordinates[col] *
	                   determinant(matrix, row+1, columns);
	            columns[col] = true;
	            sign = -sign;
	        }
	        return sum;
	    }

	    /**
	     * Compute generalized cross-product of the rows of a matrix.
	     * The result is a Point perpendicular (as a vector) to each row of
	     * the matrix.  This is not an efficient implementation, but should
	     * be adequate for low dimension.
	     * @param matrix the matrix of Points (one less row than the Point dimension)
	     * @return a Point perpendicular to each row Point
	     * @throws IllegalArgumentException if matrix is wrong shape
	     */
	    public static Point cross (Point[] matrix) {
	        int len = matrix.length + 1;
	        if (len != matrix[0].dimension())
	            throw new IllegalArgumentException("Dimension mismatch");
	        boolean[] columns = new boolean[len];
	        for (int i = 0; i < len; i++) columns[i] = true;
	        double[] result = new double[len];
	        int sign = 1;
	        try {
	            for (int i = 0; i < len; i++) {
	                columns[i] = false;
	                result[i] = sign * determinant(matrix, 0, columns);
	                columns[i] = true;
	                sign = -sign;
	            }
	        } catch (ArrayIndexOutOfBoundsException e) {
	            throw new IllegalArgumentException("Matrix is wrong shape");
	        }
	        return new Point(result);
	    }

	    /* Points as simplices */

	    /**
	     * Determine the signed content (i.e., area or volume, etc.) of a simplex.
	     * @param simplex the simplex (as an array of Points)
	     * @return the signed content of the simplex
	     */
	    public static double content (Point[] simplex) {
	        Point[] matrix = new Point[simplex.length];
	        for (int i = 0; i < matrix.length; i++)
	            matrix[i] = simplex[i].extend(1);
	        int fact = 1;
	        for (int i = 1; i < matrix.length; i++) fact = fact*i;
	        return determinant(matrix) / fact;
	    }

	    /**
	     * Relation between this Point and a simplex (represented as an array of
	     * Points). Result is an array of signs, one for each vertex of the simplex,
	     * indicating the relation between the vertex, the vertex's opposite facet,
	     * and this Point.
	     *
	     * <pre>
	     *   -1 means Point is on same side of facet
	     *    0 means Point is on the facet
	     *   +1 means Point is on opposite side of facet
	     * </pre>
	     *
	     * @param simplex an array of Points representing a simplex
	     * @return an array of signs showing relation between this Point and simplex
	     * @throws IllegalArgumentExcpetion if the simplex is degenerate
	     */
	    public int[] relation (Point[] simplex) {
	        /* In 2D, we compute the cross of this matrix:
	         *    1   1   1   1
	         *    p0  a0  b0  c0
	         *    p1  a1  b1  c1
	         * where (a, b, c) is the simplex and p is this Point. The result is a
	         * vector in which the first coordinate is the signed area (all signed
	         * areas are off by the same constant factor) of the simplex and the
	         * remaining coordinates are the *negated* signed areas for the
	         * simplices in which p is substituted for each of the vertices.
	         * Analogous results occur in higher dimensions.
	         */
	        int dim = simplex.length - 1;
	        if (this.dimension() != dim)
	            throw new IllegalArgumentException("Dimension mismatch");

	        /* Create and load the matrix */
	        Point[] matrix = new Point[dim+1];
	        /* First row */
	        double[] coords = new double[dim+2];
	        for (int j = 0; j < coords.length; j++) coords[j] = 1;
	        matrix[0] = new Point(coords);
	        /* Other rows */
	        for (int i = 0; i < dim; i++) {
	            coords[0] = this.coordinates[i];
	            for (int j = 0; j < simplex.length; j++)
	                coords[j+1] = simplex[j].coordinates[i];
	            matrix[i+1] = new Point(coords);
	        }

	        /* Compute and analyze the vector of areas/volumes/contents */
	        Point vector = cross(matrix);
	        double content = vector.coordinates[0];
	        int[] result = new int[dim+1];
	        for (int i = 0; i < result.length; i++) {
	            double value = vector.coordinates[i+1];
	            if (Math.abs(value) <= 1.0e-6 * Math.abs(content)) result[i] = 0;
	            else if (value < 0) result[i] = -1;
	            else result[i] = 1;
	        }
	        if (content < 0) {
	            for (int i = 0; i < result.length; i++)
	                result[i] = -result[i];
	        }
	        if (content == 0) {
	            for (int i = 0; i < result.length; i++)
	                result[i] = Math.abs(result[i]);
	        }
	        return result;
	    }

	    /**
	     * Test if this Point is outside of simplex.
	     * @param simplex the simplex (an array of Points)
	     * @return simplex Point that "witnesses" outsideness (or null if not outside)
	     */
	    public Point isOutside (Point[] simplex) {
	        int[] result = this.relation(simplex);
	        for (int i = 0; i < result.length; i++) {
	            if (result[i] > 0) return simplex[i];
	        }
	        return null;
	    }

	    /**
	     * Test if this Point is on a simplex.
	     * @param simplex the simplex (an array of Points)
	     * @return the simplex Point that "witnesses" on-ness (or null if not on)
	     */
	    public Point isOn (Point[] simplex) {
	        int[] result = this.relation(simplex);
	        Point witness = null;
	        for (int i = 0; i < result.length; i++) {
	            if (result[i] == 0) witness = simplex[i];
	            else if (result[i] > 0) return null;
	        }
	        return witness;
	    }

	    /**
	     * Test if this Point is inside a simplex.
	     * @param simplex the simplex (an arary of Points)
	     * @return true iff this Point is inside simplex.
	     */
	    public boolean isInside (Point[] simplex) {
	        int[] result = this.relation(simplex);
	        for (int r: result) if (r >= 0) return false;
	        return true;
	    }

	    /**
	     * Test relation between this Point and circumcircle of a simplex.
	     * @param simplex the simplex (as an array of Points)
	     * @return -1, 0, or +1 for inside, on, or outside of circumcircle
	     */
	    public int isCircumcircle (Point[] simplex) {
	        Point[] matrix = new Point[simplex.length + 1];
	        for (int i = 0; i < simplex.length; i++)
	            matrix[i] = simplex[i].extend(1, simplex[i].dot(simplex[i]));
	        matrix[simplex.length] = this.extend(1, this.dot(this));
	        double d = determinant(matrix);
	        int result = (d < 0)? -1 : ((d > 0)? +1 : 0);
	        if (content(simplex) < 0) result = - result;
	        return result;
	    }

	    /**
	     * Circumcenter of a simplex.
	     * @param simplex the simplex (as an array of Points)
	     * @return the circumcenter (a Point) of simplex
	     */
	    public static Point circumcenter (Point[] simplex) {
	        int dim = simplex[0].dimension();
	        if (simplex.length - 1 != dim)
	            throw new IllegalArgumentException("Dimension mismatch");
	        Point[] matrix = new Point[dim];
	        for (int i = 0; i < dim; i++)
	            matrix[i] = simplex[i].bisector(simplex[i+1]);
	        Point hCenter = cross(matrix);      // Center in homogeneous coordinates
	        double last = hCenter.coordinates[dim];
	        double[] result = new double[dim];
	        for (int i = 0; i < dim; i++) result[i] = hCenter.coordinates[i] / last;
	        return new Point(result);
	    }

	    /**
	     * Main program (used for testing).
	     */
	    public static void main (String[] args) {
	        Point p = new Point(1, 2, 3);
	        System.out.println("Point created: " + p);
	        Point[] matrix1 = {new Point(1,2), new Point(3,4)};
	        Point[] matrix2 = {new Point(7,0,5), new Point(2,4,6), new Point(3,8,1)};
	        System.out.print("Results should be -2 and -288: ");
	        System.out.println(determinant(matrix1) + " " + determinant(matrix2));
	        Point p1 = new Point(1,1); Point p2 = new Point(-1,1);
	        System.out.println("Angle between " + p1 + " and " +
	                p2 + ": " + p1.angle(p2));
	        System.out.println(p1 + " subtract " + p2 + ": " + p1.subtract(p2));
	        Point v0 = new Point(0,0), v1 = new Point(1,1), v2 = new Point(2,2);
	        Point[] vs = {v0, new Point(0,1), new Point(1,0)};
	        Point vp = new Point(.1, .1);
	        System.out.println(vp + " isInside " + toString(vs) +
	                ": " + vp.isInside(vs));
	        System.out.println(v1 + " isInside " + toString(vs) +
	                ": " + v1.isInside(vs));
	        System.out.println(vp + " isCircumcircle " + toString(vs) + ": " +
	                           vp.isCircumcircle(vs));
	        System.out.println(v1 + " isCircumcircle " + toString(vs) + ": " +
	                           v1.isCircumcircle(vs));
	        System.out.println(v2 + " isCircumcircle " + toString(vs) + ": " +
	                           v2.isCircumcircle(vs));
	        System.out.println("Circumcenter of " + toString(vs) + " is " +
	                circumcenter(vs));
	    }
	}