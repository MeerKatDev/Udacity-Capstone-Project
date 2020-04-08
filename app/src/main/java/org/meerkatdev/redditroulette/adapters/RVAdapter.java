package org.meerkatdev.redditroulette.adapters;

import org.meerkatdev.redditroulette.data.Post;

import java.util.List;

public interface RVAdapter<T> {

    void setData(List<T> _elements);
}
