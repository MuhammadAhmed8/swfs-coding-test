package com.smallworld.data;

import java.io.IOException;
import java.util.List;

public interface IDataReader<T> {
    IDataReader read(Class<T[]> value) throws IOException;

    List<T> getAll(Class<T[]> value) throws IOException;
}
