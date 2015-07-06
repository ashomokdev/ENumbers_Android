package com.example.ENumbers;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y.belyaeva on 25.06.2015.
 */
@Root(name = "root")
public class ENumbersCollection {
    @ElementList(name="eNumbers", required = true)
    private List<ENumber> list;

    public List getEnumbers()
    {
        return list;
    }

}
