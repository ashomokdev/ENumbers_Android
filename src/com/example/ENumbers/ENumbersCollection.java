package com.example.eNumbers;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Iterator;
import java.util.List;

/**
 * Created by y.belyaeva on 25.06.2015.
 */
@Root(name = "root")
public class ENumbersCollection
        implements Iterable<ENumber> {

    @ElementList(name="eNumbers", required = true)
    private List<ENumber> list;

    public List getEnumbers()
    {
        return list;
    }

    /**
     * Returns an {@link Iterator} for the elements in this object.
     *
     * @return An {@code Iterator} instance.
     */
    @Override
    public Iterator<ENumber> iterator() {

        Iterator<ENumber> iterator = list.iterator();

        return iterator;
    }
}
