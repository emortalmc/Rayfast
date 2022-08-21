package dev.emortal.rayfast.vector;

import java.util.function.Function;

/**
 * Represents an immutable vector with n-dimensions
 */
public interface Vector<V extends Vector<V>> {

    /**
     * Returns the number of dimensions
     * @return the number of dimensions
     */
    int dimensions();

    /**
     * Returns the value of the specified dimension
     * @param dimension the dimension
     * @return the value of the specified dimension
     */
    double get(int dimension);

    /**
     * Creates a new vector with the specified values
     * @param values the values
     * @return a new vector with the specified values
     */
    V with(double... values);

    /**
     * Returns the sum of this vector and the specified vector
     * @param v the vector
     * @return the sum of this vector and the specified vector
     */
    default V add(V v) {
        // Add all the values
        double[] values = new double[dimensions()];
        for (int i = 0; i < values.length; i++) {
            values[i] = get(i) + v.get(i);
        }
        // Return the new vector
        return with(values);
    }

    /**
     * Returns the difference of this vector and the specified vector
     * @param v the vector
     * @return the difference of this vector and the specified vector
     */
    default V subtract(V v) {
        // Subtract all the values
        double[] values = new double[dimensions()];
        for (int i = 0; i < values.length; i++) {
            values[i] = get(i) - v.get(i);
        }
        // Return the new vector
        return with(values);
    }

    /**
     * Returns the product of this vector and the specified vector
     * @param v the vector
     * @return the product of this vector and the specified vector
     */
    default V multiply(V v) {
        // Multiply all the values
        double[] values = new double[dimensions()];
        for (int i = 0; i < values.length; i++) {
            values[i] = get(i) * v.get(i);
        }
        // Return the new vector
        return with(values);
    }

    /**
     * Returns the quotient of this vector and the specified vector
     * @param v the vector
     * @return the quotient of this vector and the specified vector
     */
    default V divide(V v) {
        // Divide all the values
        double[] values = new double[dimensions()];
        for (int i = 0; i < values.length; i++) {
            values[i] = get(i) / v.get(i);
        }
        // Return the new vector
        return with(values);
    }

    /**
     * Returns the scalar product of this vector and the specified vector
     * @param v the vector
     * @return the scalar product of this vector and the specified vector
     */
    default double dot(V v) {
        // Multiply all the values
        double result = 0;
        for (int i = 0; i < dimensions(); i++) {
            result += get(i) * v.get(i);
        }
        // Return the new vector
        return result;
    }

    /**
     * Returns the magnitude of this vector
     * @return the magnitude of this vector
     */
    default double magnitude() {
        // Multiply all the values
        double result = 0;
        for (int i = 0; i < dimensions(); i++) {
            result += get(i) * get(i);
        }
        // Return the new vector
        return Math.sqrt(result);
    }

    /**
     * Returns the unit vector of this vector
     * @return the unit vector of this vector
     */
    default V unit() {
        // Multiply all the values
        double[] values = new double[dimensions()];
        for (int i = 0; i < values.length; i++) {
            values[i] = get(i) / magnitude();
        }
        // Return the new vector
        return with(values);
    }

    /**
     * Returns the projection of this vector onto the specified vector
     * @param v the vector
     * @return the projection of this vector onto the specified vector
     */
    default V project(V v) {
        // Multiply all the values
        double[] values = new double[dimensions()];
        for (int i = 0; i < values.length; i++) {
            values[i] = get(i) * dot(v) / dot(v);
        }
        // Return the new vector
        return with(values);
    }

    /**
     * Returns the rejection of this vector onto the specified vector
     * @param v the vector
     * @return the rejection of this vector onto the specified vector
     */
    default V reject(V v) {
        // Multiply all the values
        double[] values = new double[dimensions()];
        for (int i = 0; i < values.length; i++) {
            values[i] = get(i) - dot(v) / dot(v);
        }
        // Return the new vector
        return with(values);
    }

    /**
     * Returns the cross product of this vector and the specified vector
     * @param v the vector
     * @return the cross product of this vector and the specified vector
     */
    default V cross(V v) {
        // Multiply all the values
        double[] values = new double[dimensions()];
        for (int i = 0; i < values.length; i++) {
            values[i] = get(i) * v.get(i);
        }
        // Return the new vector
        return with(values);
    }

    /**
     * Converts this vector to a string
     * @return the string
     */
    default String asString() {
        // Create the string
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < dimensions(); i++) {
            sb.append(get(i));
            if (i < dimensions() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        // Return the string
        return sb.toString();
    }
}
