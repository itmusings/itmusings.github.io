

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * @author Raja S Kolluru
 *         <p>
 *         This implements the java.util.Comparator interface. It can be used to
 *         compare any two elements that implement the {@link Sortable}
 *         interface. It can be passed to a Collections.sort() method with
 *         {@link Sortable} objects. If the model objects in the list do not
 *         implement the Sortable interface, this class would figure out the
 *         sort criteria using reflection.
 */
public class GenericComparator implements Comparator {
    private SortCriteria sortCriteria;

    /**
     * 
     * @param sc -
     *            sort criteria stored in the
     *            {@link com.jpmc.pcs.paging.PageInfo} object.
     */
    public GenericComparator(SortCriteria sc) {
        if (sc == null)
            throw new RuntimeException("Sort Criteria not specified");
        this.sortCriteria = sc;
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     *      Implements the compare for two objects. Supports arbitrarily nested
     *      sorts. Sorts can be ascending or descending in any of the order.
     * 
     */
    public int compare(Object left, Object right) {
        for (int level = 0; level < sortCriteria.size(); level++) {
            int comp = compareCriterion(sortCriteria.get(level).getName(),
                    left, right);
            // check if this sort criterion differentiated between the objects.
            // if it did not we should consider sub sort criteria.
            if (comp != 0)
                return (sortCriteria.isAscendingOrder(level)) ? comp : (-1)
                        * comp;
        }
        return 0;
    }

    /**
     * Avoids creation of null object arrays needlessly. This can be used in all
     * the places where one is needed.
     */
    private static final Object[] nullObjectArray = new Object[] {};

    private static final int CANT_COMPARE = 10000;

    /**
     * Compares the left and the right using a sort criterion.
     * <p>
     * The left object can be "Sortable". If it is not then the method figures
     * the compareTo using reflection. We assume then that the criterion passed
     * is the name of a field in the object.
     * 
     * @param criterion
     * @param left
     * @param right
     * @return the result of compareTo() if a comparison can be made on the
     *         basis of the criteria. Returns CANT_COMPARE if the comparison
     *         cannot be made.
     */
    private int compareCriterion(String criterion, Object left, Object right) {
        // if the left item is sortable then invoke the compareTo method for it.
        if (left instanceof Sortable)
            return ((Sortable) left).compareTo(criterion, right);
        // the left item is not Sortable. So we have to see if we can compare it
        // ourselves.
        // get all the methods of the left object.
        Method[] methods = left.getClass().getMethods();

        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();
            // ignore non getters and getters that accept arguments.
            if (!methodName.startsWith("get")
                    || methods[i].getParameterTypes().length > 0)
                continue;
            String fieldName = methodName.substring(3);
            // ignore the getter if it does not pertain to current criterion.
            if (!fieldName.equalsIgnoreCase(criterion))
                continue;
            // extract the sort criterion property from the left object using
            // the getter.
            try {
                Object leftProperty = methods[i].invoke(left, nullObjectArray);
                // see if the right object has the same method and property.
                Method m = right.getClass().getMethod(methods[i].getName(),
                        methods[i].getParameterTypes());
                if (m == null)
                    return CANT_COMPARE; // does not exist in right.. cant
                // compare.
                Object rightProperty = m.invoke(right, nullObjectArray);
                return doNaturalComparison(leftProperty, rightProperty);
            } catch (Throwable t) {
                // can come here if the getter bombs or for such "unexpected"
                // situations.
                return CANT_COMPARE;
            }
        }
        return CANT_COMPARE; // dont know how to determine it .. return an
        // arbitrary number.
    }

    /**
     * Compares the left and the right object in their "natural" order. and
     * returns the compareTo value.
     * <p>
     * The natural order is the order defined by the compareTo method that the
     * class implements. This automatically takes care of primitives since the
     * primitives are object wrapped automatically and the object wrappers
     * implement the Comparable interface.
     * 
     * @param left
     * @param right
     * @return
     * 
     */
    @SuppressWarnings("unchecked")
	private int doNaturalComparison(Object left, Object right) {
        if (left instanceof Comparable)
            return ((Comparable) left).compareTo(right);
        else
            return CANT_COMPARE;
    }
}
