package com.smallworld;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JsonDataReader<T> implements IDataReader<T> {
    private List<T> data;
    private final String path;

    public JsonDataReader(String path) {
        this.path = path;
    }

    public List<T> Data() {
        return data;
    }

    public JsonDataReader<T> read(Class<T[]> value) throws IOException {
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON array to list of transactions
            this.data = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), value));

            return this;
    }


}
