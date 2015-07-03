package com.example.ENumbers;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y.belyaeva on 25.06.2015.
 */
@Root(name = "eNumbers")
public class ENumbersCollection {
    @ElementList(required = true, inline = true)
    private List<ENumber> list;

    public ENumbersCollection()
    {
        list =  new ArrayList<ENumber>();
    }

    public List<ENumber> getList()
    {
        return list;
    }

    @Override
    public String toString()
    {
        return "Enumbers{" + "list=" + list + '}';
    }
}
